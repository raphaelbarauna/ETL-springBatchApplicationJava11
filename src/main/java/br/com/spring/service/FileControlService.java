package br.com.spring.service;

import br.com.spring.domain.AtlysFileGroup;
import br.com.spring.domain.FileControl;
import br.com.spring.repository.FileControlRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class FileControlService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FileControlRepository fileControlRepository;


    /**
     * Metodo para validar se os arquivos encontrados no diretorio estao cadastrados no banco de dados
     *
     * @param paths recebe uma lista com todos os arquivos no diretorio de busca
     * @return uma lista dos arquivos listados no diretorio que não
     * foram cadastrados na tabela TB_CONTEST_CONTROLE
     */
    public List<String> unregisteredFiles(List<Path> paths) {

        List<String> pathList = paths.stream().map(Path::getFileName).map(Object::toString).collect(Collectors.toList());

        List<FileControl> arquivosCadastrados = registeredFiles(pathList);

        return pathList.stream().filter(obj1 -> arquivosCadastrados.stream().noneMatch(obj2 -> obj2.getName().equals(obj1))).collect(Collectors.toList());

    }

    /**
     * Metodo para validar se os arquivos encontrados no diretorio já se encontram no banco de dados
     *
     * @param pathList uma lista contendo todos os arquivos encontrados no diretorio de busca.
     * @return List<FileControl> dos arquivos que tem o status do JOB diferente de Completado
     * e a data de execucao for diferente do dia atual
     */
    public List<FileControl> registeredFiles(List<String> pathList) {

        List<FileControl> registeredFiles = fileControlRepository.findByNameIn(pathList);

        return registeredFiles.stream().filter(item ->
                item.getStatusJob().equals("COMPLETED")).collect(Collectors.toList());

    }

    /**
     * Metodo para criar os grupos com os Estados encontrados na pasta
     *
     * @param fileList Lista contendo os arquivos no diretorio de busca.
     * @return AtlysFileGroup
     */
    public List<AtlysFileGroup> checkPathIsPartOfGroup(List<String> fileList) {

        List<AtlysFileGroup> atlysFileGroupList = new ArrayList<>();

        String[] estados = {"AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MT",
                "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RO", "RS", "RR", "SC",
                "SE", "SP", "TO"};

        for (String estado : estados) {

            AtlysFileGroup atlysFileGroup = new AtlysFileGroup();
            atlysFileGroup.setEstado(estado);

            List<String> stringList = new ArrayList<>(filterListByStateAndName(fileList, estado));
            atlysFileGroup.setPath(stringList);
            atlysFileGroupList.add(atlysFileGroup);

        }
        return atlysFileGroupList.stream().filter(item -> !item.getPath().isEmpty()).collect(Collectors.toList());

    }

    /**
     * Metodo para validar os arquivos na pasta se contem rcvbl, adj e estado
     *
     * @param lista  com os arquivos encontrados no diretorio
     * @param estado estado que esta sendo verificado
     * @return List<String>
     */
    public List<String> filterListByStateAndName(List<String> lista, String estado) {

        return lista.stream()
                .filter(item -> ((item.startsWith("adj") || item.startsWith("rcvbl")) && item.contains(estado)))
                .collect(Collectors.toList());
    }

    /**
     * Metodo para listar as pastas do diretorio
     *
     * @param path diretorio a ser lido
     * @return List<String> com as pastas encontradas
     */
    public List<String> getFolders(String path) {
        File directory = new File(path);
        return Arrays.stream(Objects.requireNonNull(directory.list())).sorted().collect(Collectors.toList());

    }

    /**
     * Metodo para listar os arquivos encontrados no diretorio
     *
     * @param path diretorio da busca, cadastrado no properties
     * @return List<Path> com todos os arquivos encontrados no diretorio
     * @throws IOException em caso de falha na leitura dos arquivos
     */
    public List<Path> getFiles(Path path) throws IOException {
        try (Stream<Path> walk = Files.walk(path)) {
            return walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Metodo que sera executado no final do job deve chamar a procedure, cadastrar na tabela controle,
     * zipar,mover e deletar linhas em caso de falha.
     *
     * @param jobExecution Contem o job executado
     * @param filePath     Diretorio do arquivo
     */
    public BatchStatus afterJob(JobExecution jobExecution, String filePath) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        AtomicReference<String> fileName = new AtomicReference<>();
        AtomicReference<String> targetDir = new AtomicReference<>();

        if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
            log.info("Iniciou a procedure: " + sdf.format(new Date()));
            Map<String, Object> procedureResult = callProcedureToJoin();
            targetDir.set("processados");

            if (procedureResult.get("p_codretorno").equals(1)) {
                jobExecution.setStatus(BatchStatus.FAILED);
            }
            log.info("Finalizou procedure: " + sdf.format(new Date()));
        }
        jobExecution.getStepExecutions().forEach(
                stepExecution -> {

                    if (stepExecution.getStepName().equals("recebivelStep"))
                        fileName.set(stepExecution.getJobParameters().getString("recebivelFileName"));
                    else
                        fileName.set(stepExecution.getJobParameters().getString("detalheFileName"));
                    try {

                        if (jobExecution.getStatus().equals(BatchStatus.FAILED)) {
                            deleteLinesWithError(stepExecution);
                            targetDir.set("bads");
                            log.error("Não foi possível carregar os arquivos . Verifique os logs para mais informações.");
                        }
                        saveToFileControl(stepExecution, jobExecution.getStatus());
                        moveFiles(targetDir.toString(), filePath, fileName.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        log.info("Apos mover e salvar na tabela de arquivos after job" + sdf.format(new Date()));
        return jobExecution.getStatus();
    }

    /**
     * Metodo para deletar as linhas em caso de a leitura do arquivo falhar pela metade
     *
     * @param stepExecution contem a execução do JOB
     */
    public void deleteLinesWithError(StepExecution stepExecution) {

        AtomicReference<String> sql = new AtomicReference<>("");

        if (stepExecution.getStepName().equals("detalheStep")) {
            sql.set("DELETE FROM TB_ATLYS_ADJ WHERE JOB_EXECUTION_ID = ?");
        } else {
            sql.set("DELETE FROM TB_ATLYS_RCBL WHERE JOB_EXECUTION_ID = ?");
        }
        Object[] args = new Object[]{stepExecution.getJobExecution().getJobId()};
        jdbcTemplate.update(sql.get(), args);

    }

    /**
     * Metodo para salvar o arquivo na tabela de controle
     *
     * @param stepExecution contem a execução do Step
     * @param jobStatus     qual o status do JOB
     */
    public void saveToFileControl(StepExecution stepExecution, BatchStatus jobStatus) {
        String fileName;
        if (stepExecution.getStepName().equals("recebivelStep"))
            fileName = stepExecution.getJobParameters().getString("recebivelFileName");
        else
            fileName = stepExecution.getJobParameters().getString("detalheFileName");

        String filePath = stepExecution.getJobParameters().getString("path");
        StringBuilder exitMsg = new StringBuilder();
        List<Throwable> exceptions = stepExecution.getFailureExceptions();
        exceptions.forEach(exception -> exitMsg.append(exception.toString()));

        if (jobStatus.equals(BatchStatus.FAILED) && exitMsg.toString().equals("")) {
            exitMsg.append("O arquivo:  ")
                    .append(fileName)
                    .append(" falhou no carregamento");
        } else {
            exitMsg.append("Inserido na TB_CONTEST_BILLING com sucesso.");
        }
        try {
            FileControl fileControl = new FileControl();
            fileControl.setExecutionTime(new Date());
            fileControl.setFileSize(getFileSize(filePath + fileName));
            fileControl.setName(fileName);
            fileControl.setPath(filePath);
            fileControl.setStatusStep(stepExecution.getStatus().toString());
            fileControl.setStatusJob(stepExecution.getJobExecution().getStatus().toString());
            fileControl.setStepExecutionId(stepExecution.getId());
            fileControl.setJobExecutionId(stepExecution.getJobExecutionId());
            fileControl.setExitMessage(exitMsg.toString());
            fileControlRepository.save(fileControl);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo para pegar o tamanho do arquivo
     *
     * @param filePath diretorio do arquivo a ser analisado
     * @return Long contendo o tamanho do arquivo
     * @throws IOException em caso de erro na leitura do arquivo
     */
    private Long getFileSize(String filePath) throws IOException {
        boolean fileExists = Files.exists(Paths.get(filePath));
        if (fileExists) {
            return (Long) Files.getAttribute(Paths.get(filePath), "size");
        } else {
            throw new NoSuchFileException("Arquivo nao encontrado no diretorio: " + filePath);
        }
    }

    /**
     * Metodo para mover um arquivo para pasta processados ou bads
     *
     * @param targetDir contem a pasta que sera movido o arquivo
     * @param filePath  contem o diretorio do arquivo
     * @throws IOException em caso de erro no tratamento do arquivo
     */
    public void moveFiles(String targetDir, String filePath, String fileName) throws IOException {
        Path target = Paths.get(filePath.replace("a_processar", targetDir));

        if (Files.notExists(target)) Files.createDirectories(target);
        Path path = Paths.get(filePath + fileName);
        Path compressedFile = compress(path);
        Files.move(compressedFile, target.resolve(compressedFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        Files.delete(path);
    }

    /**
     * Metodo para criar um zip com os arquivos analisados
     *
     * @param fileToCompress contem o diretorio do arquivo a ser comprimido
     * @return Path com o diretorio do arquivo que sera movido.
     */
    private Path compress(Path fileToCompress) {
        String fileName = fileToCompress.getFileName().toString().split("\\.")[0] + ".tar.gz";
        File targetCompress = Paths.get(fileToCompress.getParent().toString() + "/" + fileName).toFile();
        try (TarArchiveOutputStream archive = new TarArchiveOutputStream
                (new GzipCompressorOutputStream(new FileOutputStream(targetCompress)))) {
            TarArchiveEntry entry = new TarArchiveEntry (fileToCompress.toFile(), fileToCompress.getFileName().toString());
            archive.putArchiveEntry(entry);
            FileInputStream fs = new FileInputStream(fileToCompress.toFile());
            IOUtils.copy(fs, archive);
            archive.closeArchiveEntry();
            fs.close();
            archive.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return targetCompress.toPath();
    }

    /**
     * Método para chamar a stored procedure sp_insert_atlys(código de retorno OUT, mensagem de retorno OUT).
     *
     * @return {@code Map} em que a chave representa o código de retorno da procedure e o valor a mensagem de retorno.
     */
//    public Map<Integer, String> callProcedureToJoin() {
//        Map<Integer, String> procedureResult = new HashMap<>();
//        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
//             CallableStatement cs = connection.prepareCall("{call pkg_contestacao_billing.sp_insert_atlys(?, ?)}")) {
//            cs.registerOutParameter(1, Types.INTEGER);
//            cs.registerOutParameter(2, Types.VARCHAR);
//            cs.execute();
//            procedureResult.put(cs.getInt(1), cs.getString(2));
//        } catch (SQLException | NullPointerException e) {
//
//            e.printStackTrace();
//        }
//        return procedureResult;
//    }
    public Map<String, Object> callProcedureToJoin() {

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
        simpleJdbcCall.withCatalogName("pkg_contestacao_billing")
                .withProcedureName("sp_insert_atlys")
                .declareParameters(
                        new SqlOutParameter("p_codretorno", Types.INTEGER),
                        new SqlOutParameter("p_msgretorno", Types.VARCHAR));

        return simpleJdbcCall.execute();
    }
}
