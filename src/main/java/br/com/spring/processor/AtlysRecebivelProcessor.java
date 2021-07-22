package br.com.spring.processor;

import br.com.spring.domain.RecebivelAtlysLayout;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("atlysRecebivelProcessor")
@StepScope
public class AtlysRecebivelProcessor implements ItemProcessor<RecebivelAtlysLayout, RecebivelAtlysLayout> {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public RecebivelAtlysLayout process(final RecebivelAtlysLayout recebivelAtlysLayout) throws Exception {

        recebivelAtlysLayout.setStepExecutionId(stepExecution.getId());
        recebivelAtlysLayout.setJobExecutionId(stepExecution.getJobExecutionId());

        return recebivelAtlysLayout;
    }


}
