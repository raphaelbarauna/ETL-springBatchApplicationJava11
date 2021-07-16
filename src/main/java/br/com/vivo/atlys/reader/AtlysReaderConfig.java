package br.com.vivo.atlys.reader;

import br.com.vivo.atlys.domain.AdjAtlysLayout;
import br.com.vivo.atlys.domain.RecebivelAtlysLayout;
import br.com.vivo.atlys.mapper.RecebivelAtlysFieldSetMapper;
import br.com.vivo.atlys.reader.utils.CustomDelimitedLineTokenizer;
import br.com.vivo.atlys.reader.utils.CustomLineMapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;


@Configuration
public class AtlysReaderConfig {

    @StepScope
    @Bean
    @Primary
    public FlatFileItemReader<RecebivelAtlysLayout> recebivelAtlysReader(@Value("#{jobParameters['recebivelFileName']}") String fileName, @Value("#{jobParameters['path']}") String directory) {
        return new FlatFileItemReaderBuilder<RecebivelAtlysLayout>()
                .linesToSkip(1)
                .name("recebivelAtlysReader")
                .resource(new FileSystemResource(directory + fileName))
                .lineTokenizer(recebivelTokenizer())
                .fieldSetMapper(new RecebivelAtlysFieldSetMapper())
                .build();
    }

    public LineTokenizer recebivelTokenizer() {
        CustomDelimitedLineTokenizer lineTokenizer = new CustomDelimitedLineTokenizer();
        lineTokenizer.setDelimiter("|");
        lineTokenizer.setQuoteCharacter('|');
        lineTokenizer.setNames("idCliente", "nomeCliente", "cnpjCpf", "contaCliente",
                "classeReceber", "operadora", "codigoOperadora", "numeroFatura", "mesAnoReferente",
                "valorDoctoOriginal", "faturaAtribuida", "mesAnoFatura", "valorFatura", "saldoDevidoOrig",
                "dtEmissaoFatura", "valorTotalPagamento", "dataFebraban", "vlrAtribuicao", "uf",
                "dtAtribuicao", "hrAtribuicao");
        return lineTokenizer;
    }

    @StepScope
    @Bean
    public FlatFileItemReader<AdjAtlysLayout> adjAtlysReader(@Value("#{jobParameters['detalheFileName']}") String fileName, @Value("#{jobParameters['path']}") String directory) {
        return new FlatFileItemReaderBuilder<AdjAtlysLayout>()
                .linesToSkip(1)
                .name("adjAtlysReader")
                .resource(new FileSystemResource(directory + fileName))
                .lineMapper(new CustomLineMapper())
                .build();
    }

}
