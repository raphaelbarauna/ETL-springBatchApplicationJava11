package br.com.spring.processor;

import br.com.spring.domain.AdjAtlysLayout;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component("atlysDetalheProcessor")
@StepScope
public class AtlysDetalheProcessor implements ItemProcessor<AdjAtlysLayout, AdjAtlysLayout> {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public AdjAtlysLayout process(final AdjAtlysLayout adjAtlysLayout) {

        String dtReferencia = getDtReferenciaFromFileName(Objects.requireNonNull(stepExecution.getJobParameters().getString("detalheFileName")));

        adjAtlysLayout.setDtReferencia(dtReferencia);

        adjAtlysLayout.setStepExecutionId(stepExecution.getId());
        adjAtlysLayout.setJobExecutionId(stepExecution.getJobExecutionId());

        return adjAtlysLayout;
    }

    private String getDtReferenciaFromFileName(String dtReferencia) {

        List<String> fields = new ArrayList<>(Arrays.asList(dtReferencia.split("_")));

        return fields.get(3).substring(0, 6);


    }

}
