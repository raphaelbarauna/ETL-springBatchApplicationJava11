package br.com.spring.builder;

import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobLauncherTestUtilsBuilder {

    @Bean
    public JobLauncherTestUtils getNotPaidjobLauncherTestUtils() {
        return new JobLauncherTestUtils() {
            @Override
            @Autowired
            public void setJob (@Qualifier("atisNotPaidStepsJob") Job job){
                super.setJob(job);
            }
        } ;
    }

    @Bean
    public JobLauncherTestUtils getPaidjobLauncherTestUtils() {
        return new JobLauncherTestUtils() {
            @Override
            @Autowired
            public void setJob(@Qualifier("atisPaidStepsJob") Job job) {
                super.setJob(job);
            }
        };
    }
}
