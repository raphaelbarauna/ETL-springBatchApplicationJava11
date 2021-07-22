package br.com.spring.configuration;

import br.com.spring.domain.AtlysFileGroup;
import br.com.spring.service.FileControlService;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@EnableScheduling
@Log4j2
public class CustomJobLauncher {

    @Autowired
    FileControlService fileControlService;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("atlysDoubleStepsJob")
    private Job atlysDoubleStepsJob;

    @Value("${path.file}")
    private String pathAtlys;

    private boolean finalizar = false;

    @Scheduled(cron = "*/1 * * * * *")
    public void myScheduler() throws IOException {

        if (finalizar) {
            log.info("Todos os arquivos foram carregados, a aplicacao sera finalizada.");
            System.exit(1);
        }

        List<String> foldersYearList = fileControlService.getFolders(pathAtlys);
        log.info(foldersYearList.toString());
        if (foldersYearList.isEmpty()) {
            log.info("O Diretorio :" + pathAtlys + " esta vazio.");
            return;
        }

        for (String yearFolder : foldersYearList) {

            List<String> foldersMonthList = fileControlService.getFolders(pathAtlys + yearFolder);

            if (foldersMonthList.isEmpty()) {
                log.info("O Diretorio: " + pathAtlys + yearFolder + " esta vazio e sera skipado.");
                continue;
            }

            for (String monthFolder : foldersMonthList) {

                String directory = appendDirectory(monthFolder, yearFolder);

                List<Path> paths = fileControlService.getFiles(Paths.get(directory));

                if (paths.isEmpty()) {
                    log.info("Nao existe arquivos no diretorio:" + directory);
                    continue;
                }

                List<String> unregisteredFiles = fileControlService.unregisteredFiles(paths);

                if (unregisteredFiles.isEmpty()) {
                    log.info("Todos os arquivos no diretorio" + directory + " ja foram cadastrados.");
                    continue;
                }

                List<AtlysFileGroup> atlysFileGroupList = fileControlService.checkPathIsPartOfGroup(unregisteredFiles);

                for (AtlysFileGroup atlysFileGroup : atlysFileGroupList) {

                    if (atlysFileGroup.getPath().size() < 2) {
                        log.info("O grupo composto pelo estado: " + atlysFileGroup.getEstado() + " contem apenas o arquivo" + atlysFileGroup.getPath() + " e sera ignorado.");
                        continue;
                    }

                    try {
                        log.info("Foi encontrado arquivos validos no diretorio: " + directory + " .");
                        log.info("Fazendo carga do arquivo:: " + atlysFileGroup.getPath().get(0));
                        log.info("Fazendo carga do arquivo:: " + atlysFileGroup.getPath().get(1));

                        JobParameters jobParameters = new JobParametersBuilder()
                                .addString("detalheFileName", atlysFileGroup.getPath().get(0))
                                .addString("recebivelFileName", atlysFileGroup.getPath().get(1))
                                .addString("folderName", monthFolder)
                                .addString("path", directory)
                                .addLong("time", System.currentTimeMillis())
                                .toJobParameters();

                        JobExecution jobExecution = jobLauncher.run(atlysDoubleStepsJob, jobParameters);

                        fileControlService.afterJob(jobExecution, directory);

                        log.info("Job's Status:::" + jobExecution.getStatus());

                    } catch (JobExecutionAlreadyRunningException
                            | JobInstanceAlreadyCompleteException
                            | JobParametersInvalidException
                            | JobRestartException e) {
                        e.printStackTrace();

                    }
                }
            }
        }
        finalizar = true;
    }

    private String appendDirectory(String monthFolder, String yearFolder) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pathAtlys);
        stringBuilder.append(yearFolder);
        stringBuilder.append("/");
        stringBuilder.append(monthFolder);
        stringBuilder.append("/");
        return stringBuilder.toString();
    }

}
