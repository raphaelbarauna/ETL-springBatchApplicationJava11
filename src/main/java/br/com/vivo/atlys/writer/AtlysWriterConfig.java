package br.com.vivo.atlys.writer;

import br.com.vivo.atlys.domain.AdjAtlysLayout;
import br.com.vivo.atlys.domain.RecebivelAtlysLayout;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AtlysWriterConfig {

    @Bean
    public ItemWriter<RecebivelAtlysLayout> recebivelAtlysWriter(@Qualifier("springDataSource") DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<RecebivelAtlysLayout>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO TB_ATLYS_RCBL"
                        + "(ID_CLIENTE, NOME_CLIENTE, CNPJ_CPF, CONTA_CLIENTE, "
                        + "CLASSE_RECEBER, OPERADORA, CODIGO_OPERADORA, "
                        + "NUMERO_FATURA, MES_ANO_REFERENTE, VALOR_DOCTO_ORIGINAL, "
                        + "FATURA_ATRIBUIDA, MES_ANO_FATURA, VALOR_FATURA, SALDO_DEVIDO_ORIG, "
                        + "DATA_EMISSAO_FATURA, VALOR_TOTAL_PAGAMENTO, DATA_FEBRABAN, "
                        + "VALOR_ATRIBUICAO, UF, DATA_ATRIBUICAO_PGTO, "
                        + "HORA_ATRIBUICAO_PGTO, STEP_EXECUTION_ID, JOB_EXECUTION_ID) "
                        + "VALUES ("
                        + ":idCliente, :nomeCliente, :cnpjCpf, :contaCliente, "
                        + ":classeReceber, :operadora, :codigoOperadora, "
                        + ":numeroFatura, :mesAnoReferente, :valorDoctoOriginal, "
                        + ":faturaAtribuida, :mesAnoFatura, :valorFatura, :saldoDevidoOrig, "
                        + ":dtEmissaoFatura, :valorTotalPagamento, :dataFebraban, "
                        + ":vlrAtribuicao, :uf, :dtAtribuicao, "
                        + ":hrAtribuicao, :stepExecutionId, :jobExecutionId)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public ItemWriter<AdjAtlysLayout> adjAtlysWriter(@Qualifier("springDataSource") DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AdjAtlysLayout>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(""
                        + "INSERT INTO TB_ATLYS_ADJ"
                        + "(ID_CLIENTE, NOTA_FISCAL, DT_EMISSAO_NF, VLR_ORIG_NFST, "
                        + "SERIE, CONTA_CLIENTE, CNPJ_CPF, NOM_CLIENTE, NUM_TERMINAL, "
                        + "VLR_CONTEST_AJUSTE, VLR_ATRIBUIDO, DT_ATRIBUICAO, "
                        + "HR_ATRIBUICAO, DT_AJUSTE, HR_AJUSTE, OPERADORA, CODIGO_OPERADORA, "
                        + "COD_MOTIVO_AJUSTES, DESC_MOTIVO_AJUSTE, DATA_ABERTURA_IMPU, "
                        + "DATA_FECH_IMPUG, DSPUT_ID, TP_NOTA_FISCAL, CLASSE_RECEBER, "
                        + "CONTA_CONTABIL, DESCR_CONTA_CONTABIL, NUMERO_FATURA, VALOR_DOCTO_ORIGINAL, "
                        + "MES_ANO_REFERENTE, DATA_VENC_FATUR, VALOR_PAGAMENTO, DATA_ATRIBUICAO_PGTO, "
                        + "FATU_ATRIBUIDA, DT_EMISSAO_FATURA, MES_ANO_FATURA, VALOR_FATURA, SALDO_DEVEDOR, "
                        + "UF, OPERADORA_REAL, SALDO_FATURA_INI, SALDO_FATURA_FIM, SEQ_RECEBIVEL, "
                        + "DT_REFERENCIA, STEP_EXECUTION_ID, JOB_EXECUTION_ID) "
                        + "VALUES ("
                        + ":idCliente, :notaFiscal, :dtEmissaoNf, :vlrOrigNfst, :serie, :contaCliente,  "
                        + ":cnpjCpf, :nomCliente, :numTerminal, :valorContestAjuste, :vlrAtribuido, "
                        + ":dataAtribuicao, :horaAtribuicao, :dataAjuste, :horaAjuste, :operadora,  "
                        + ":codigoOperadora, :codMotivoAjustes, :descMotivoAjuste, :dataAberturaImpu, "
                        + ":dataFechImpug, :dsputId, :tpNotaFiscal, :classeReceber, :contaContabil, "
                        + ":descrContaContabil, :numeroFatura, :valorDoctoOriginal, :mesAnoReferente, "
                        + ":dataVencFatura, :valorPagamento, :dataAtribuicaoPgto, :faturaAtribuida, "
                        + ":dataEmissaoFatura, :mesAnoFatura, :valorFatura, :saldoDevedor, :uf, "
                        + ":operadoraReal, :saldoFaturaIni, :saldoFaturaFim, :seqRecebivel, "
                        + ":dtReferencia, :stepExecutionId, :jobExecutionId)")
                .dataSource(dataSource)
                .build();
    }
}