package br.com.spring.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Log4j2
@JobScope
public class AtlysJobListener {

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {

        log.info(jobExecution.getJobInstance().getJobName() + " começou!");

    }

    @AfterJob
    public ExitStatus afterJob(JobExecution jobExecution) throws IOException {

        log.info(jobExecution.getJobInstance().getJobName() + " finalizou!");
        log.info(jobExecution.getStatus());

        return jobExecution.getExitStatus();

    }
}
