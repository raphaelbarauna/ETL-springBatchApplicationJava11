package br.com.vivo.atlys.step;

import br.com.vivo.atlys.domain.AdjAtlysLayout;
import br.com.vivo.atlys.domain.RecebivelAtlysLayout;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class AtlysStepConfig {

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public TaskExecutor atlysTaskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public Step recebivelStep(TaskExecutor atlysTaskExecutor,
                               ItemReader<RecebivelAtlysLayout> recebivelAtlysLayoutItemReader,
                               ItemWriter<RecebivelAtlysLayout> recebivelAtlysLayoutItemWriter,
                               ItemProcessor<RecebivelAtlysLayout, RecebivelAtlysLayout> recebivelAtlysLayoutItemProcessor) {
        return stepBuilderFactory
                .get("recebivelStep")
                .<RecebivelAtlysLayout, RecebivelAtlysLayout>chunk(10000)
                .reader(recebivelAtlysLayoutItemReader)
                .processor(recebivelAtlysLayoutItemProcessor)
                .writer(recebivelAtlysLayoutItemWriter)
                .taskExecutor(atlysTaskExecutor())
                .throttleLimit(25)
                .build();
    }

    @Bean
    public Step detalheStep(TaskExecutor atlysTaskExecutor,
                                 ItemReader<AdjAtlysLayout> adjAtlysLayoutItemReader,
                                 ItemWriter<AdjAtlysLayout> adjAtlysLayoutItemWriter,
                                 ItemProcessor<AdjAtlysLayout, AdjAtlysLayout> adjAtlysLayoutItemProcessor) {
        return stepBuilderFactory
                .get("detalheStep")
                .<AdjAtlysLayout, AdjAtlysLayout>chunk(10000)
                .reader(adjAtlysLayoutItemReader)
                .processor(adjAtlysLayoutItemProcessor)
                .writer(adjAtlysLayoutItemWriter)
                .taskExecutor(atlysTaskExecutor())
                .throttleLimit(25)
                .build();
    }
}
