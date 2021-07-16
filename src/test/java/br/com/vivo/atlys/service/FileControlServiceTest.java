package br.com.vivo.atlys.service;

import br.com.vivo.atlys.builder.AtlysBuilder;
import br.com.vivo.atlys.domain.AtlysFileGroup;
import br.com.vivo.atlys.domain.FileControl;
import br.com.vivo.atlys.repository.FileControlRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.batch.core.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileControlService.class)
public class FileControlServiceTest {

    @Mock
    FileControlRepository fileControlRepository;

    @Mock
    private List<Path> pathList;

    @Mock
    JdbcTemplate jdbcTemplate;

    @Mock
    private Connection connection;
    @Mock
    private DatabaseMetaData metaData;
    @Mock
    private DataSource dataSource;
    @Mock
    private SimpleJdbcCall simpleJdbcCall;

    @InjectMocks
    FileControlService fileControlService;

    @Before
    public void init() throws SQLException {

        when(connection.getMetaData()).thenReturn(metaData);
        when(dataSource.getConnection()).thenReturn(connection);

    }

    @After
    public void cleanUp() throws IOException {
        Stream<Path> paths = Files.list(Paths.get("src/test/resources/2021/202103/"));

        paths.forEach(path -> {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private StepExecution createStepExecution(JobExecution jobExecution, BatchStatus status, String stepName) {
        StepExecution stepExecution = mock(StepExecution.class);

        when(stepExecution.getStatus()).thenReturn(status);
        when(stepExecution.getStepName()).thenReturn(stepName);
        when(stepExecution.getJobParameters()).thenReturn(defaultJobParameters());
        when(stepExecution.getJobExecution()).thenReturn(jobExecution);
        when(stepExecution.getId()).thenReturn(1L);
        when(stepExecution.getJobExecutionId()).thenReturn(2L);
        return stepExecution;
    }

    private JobParameters defaultJobParameters() {
        return new JobParametersBuilder()
                .addString("detalheFileName", "adj_MS_20210301_20210331.rpt")
                .addString("recebivelFileName", "rcvbl_MS_20210301_20210331.rpt")
                .addString("path", "src/test/resources/2021/202103/")
                .toJobParameters();
    }

    @Test
    public void testMethodUnregisteredFiles() {

        List<FileControl> fileControls = Collections.emptyList();

        when(fileControlRepository.findByNameIn(new ArrayList<>())).thenReturn(fileControls);

        List<String> stringList = fileControlService.unregisteredFiles(pathList);

        assertNotNull(stringList);
    }

    @Test
    public void testMethodCheckPathIsPartOfGroup() {
        List<String> fileList = new ArrayList<>();
        fileList.add("adj_AC_20210301_20210331.rpt");
        fileList.add("rcvbl_AC_20210301_20210331.rpt");

        List<AtlysFileGroup> groupList = fileControlService.checkPathIsPartOfGroup(fileList);

        assertEquals("AC", groupList.get(0).getEstado(), "AC");
        assertTrue(groupList.get(0).getPath().get(0).startsWith("adj"));

    }

    @Test
    public void testMethodGetFoldersPassingValidPath() {

        List<String> folderList = fileControlService.getFolders("src/test/resources/2021/202103/");

        assertNotNull(folderList);
    }

    @Test
    public void testMethodGetFilesPassingValidPath() throws IOException {
        AtlysBuilder atisBuilder = new AtlysBuilder();
        atisBuilder.createFiles();
        List<Path> pathList = fileControlService.getFiles(Paths.get("src/test/resources/2021/"));

        assertNotNull(pathList);
    }

    @Test
    public void testMethodAfterJobPassingJobFailedWithRecebivelStep() throws IOException {
        AtlysBuilder atisBuilder = new AtlysBuilder();
        atisBuilder.createFiles();

        JobExecution jobExecution = new JobExecution(1L);
        jobExecution.setStatus(BatchStatus.FAILED);
        StepExecution stepExecution = createStepExecution(jobExecution, BatchStatus.FAILED, "recebivelStep");

        jobExecution.addStepExecutions(Collections.singletonList(stepExecution));
        when(fileControlRepository.save(any(FileControl.class))).thenReturn(new FileControl());
        when(jdbcTemplate.update(anyString(), ArgumentMatchers.<String>any())).thenReturn(1);

        BatchStatus status = fileControlService.afterJob(jobExecution, "src/test/resources/2021/202103/");

        assertEquals(BatchStatus.FAILED, status);
    }

    @Test
    public void testMethodAfterJobPassingJobCompleted() throws Exception {
        AtlysBuilder atisBuilder = new AtlysBuilder();
        atisBuilder.createFiles();

        JobExecution jobExecution = new JobExecution(1L);
        jobExecution.setStatus(BatchStatus.COMPLETED);
        StepExecution stepExecution = createStepExecution(jobExecution, BatchStatus.COMPLETED, "recebivelStep");

        jobExecution.addStepExecutions(Collections.singletonList(stepExecution));
        when(fileControlRepository.save(any(FileControl.class))).thenReturn(new FileControl());
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);

        when(simpleJdbcCall.withCatalogName(Mockito.anyString())).thenReturn(simpleJdbcCall);
        when(simpleJdbcCall.withProcedureName(Mockito.anyString())).thenReturn(simpleJdbcCall);
        whenNew(SimpleJdbcCall.class).withArguments(jdbcTemplate).thenReturn(simpleJdbcCall);

        Map<String, Object> procedureResult = new HashMap<>();
        procedureResult.put("p_codretorno", 0);

        when(fileControlService.callProcedureToJoin()).thenReturn(procedureResult);

        BatchStatus status = fileControlService.afterJob(jobExecution, "src/test/resources/2021/202103/");

        assertEquals(BatchStatus.COMPLETED, status);
    }

    @Test
    public void testMethodAfterJobPassingJobFailedWithDetalheStep() throws IOException {

        AtlysBuilder atisBuilder = new AtlysBuilder();
        atisBuilder.createFiles();

        JobExecution jobExecution = new JobExecution(1L);
        jobExecution.setStatus(BatchStatus.FAILED);
        StepExecution stepExecution = createStepExecution(jobExecution, BatchStatus.FAILED, "detalheStep");

        jobExecution.addStepExecutions(Collections.singletonList(stepExecution));
        when(fileControlRepository.save(any(FileControl.class))).thenReturn(new FileControl());
        when(jdbcTemplate.update(anyString(), ArgumentMatchers.<String>any())).thenReturn(1);

        BatchStatus status = fileControlService.afterJob(jobExecution, "src/test/resources/2021/202103/");

        assertEquals(BatchStatus.FAILED, status);
    }

    @Test
    public void testcallProcedureToJoin() throws Exception {

        Map<String, Object> procedureResult2;

        when(simpleJdbcCall.withCatalogName(Mockito.anyString())).thenReturn(simpleJdbcCall);
        when(simpleJdbcCall.withProcedureName(Mockito.anyString())).thenReturn(simpleJdbcCall);

        whenNew(SimpleJdbcCall.class).withArguments(jdbcTemplate).thenReturn(simpleJdbcCall);

        procedureResult2 = fileControlService.callProcedureToJoin();
        assertEquals(0, procedureResult2.size());
        verify(simpleJdbcCall).execute();
    }



}
