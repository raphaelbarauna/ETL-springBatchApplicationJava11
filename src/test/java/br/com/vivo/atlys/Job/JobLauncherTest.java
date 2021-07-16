package br.com.vivo.atlys.Job;

import br.com.vivo.atlys.configuration.DataSourceTest;
import br.com.vivo.atlys.job.AtlysJobConfig;
import br.com.vivo.atlys.listener.AtlysJobListener;
import br.com.vivo.atlys.builder.AtlysBuilder;
import br.com.vivo.atlys.processor.AtlysDetalheProcessor;
import br.com.vivo.atlys.processor.AtlysRecebivelProcessor;
import br.com.vivo.atlys.reader.AtlysReaderConfig;
import br.com.vivo.atlys.step.AtlysStepConfig;
import br.com.vivo.atlys.writer.AtlysWriterConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {AtlysJobConfig.class, AtlysReaderConfig.class, DataSourceTest.class,
        AtlysStepConfig.class, AtlysJobListener.class, AtlysWriterConfig.class, AtlysDetalheProcessor.class,
        AtlysRecebivelProcessor.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@PropertySource("classpath:application-test.properties")
@Sql({"/schema-all.sql"})
class JobLauncherTest {

    private Path detalheFile;
    private Path recebivelFile;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeEach
    void init() throws IOException {

        Files.createDirectories(Paths.get("src/test/resources/2021/202103/"));

        this.recebivelFile = Files.createFile(Paths.get("src/test/resources/2021/202103/rcvbl_MS_20210301_20210331.rpt"));
        this.detalheFile = Files.createFile(Paths.get("src/test/resources/2021/202103/adj_MS_20210301_20210331.rpt"));

        String recebivelRegistry = new AtlysBuilder().recebivelBuilder();

        String detalheRegistry = new AtlysBuilder().detalheBuilder();

        BufferedWriter recebivelWriter = new BufferedWriter(new FileWriter(recebivelFile.toString()));
        recebivelWriter.write(recebivelRegistry);
        recebivelWriter.close();

        BufferedWriter detalheWriter = new BufferedWriter(new FileWriter(detalheFile.toString()));
        detalheWriter.write(detalheRegistry);
        detalheWriter.close();
    }

    @AfterEach
    public void cleanUp() throws IOException {
        jobRepositoryTestUtils.removeJobExecutions();
        Files.delete(recebivelFile);
        Files.delete(detalheFile);
    }

    @Test
    @DisplayName("Test AtlysJob for correct files then return success.")
    void testAtlysJobsSuccesfulJob() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("detalheFileName", "adj_MS_20210301_20210331.rpt")
                .addString("recebivelFileName", "rcvbl_MS_20210301_20210331.rpt")
                .addString("folderName", "202103")
                .addString("path", "src/test/resources/2021/202103/")
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        jobExecution.getStepExecutions().forEach(stepExecution -> {
            StringBuilder exitMsg = new StringBuilder("");
            List<Throwable> exceptions = stepExecution.getFailureExceptions();
            exceptions.forEach(exception -> exitMsg.append(exception.toString()));

            assertEquals("", exitMsg.toString());
        });

        assertEquals("atlysDoubleStepsJob", actualJobInstance.getJobName());
        assertEquals("COMPLETED", actualJobExitStatus.getExitCode());
    }


}
