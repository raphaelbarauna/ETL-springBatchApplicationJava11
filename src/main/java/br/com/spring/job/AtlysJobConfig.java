package br.com.spring.job;

import br.com.spring.listener.AtlysJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@EnableBatchProcessing
@Configuration
public class AtlysJobConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    AtlysJobListener atlysJobListener;

    @Bean
    public Job atlysDoubleStepsJob(Step detalheStep, Step recebivelStep) {
        return jobBuilderFactory
                .get("atlysDoubleStepsJob")
                .listener(atlysJobListener)
                .start(detalheStep)
                .next(recebivelStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

}
