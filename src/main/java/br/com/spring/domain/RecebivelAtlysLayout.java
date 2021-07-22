package br.com.spring.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class RecebivelAtlysLayout {

    @Column(name = "ID_CLIENTE")
    private String idCliente;

    @Column(name = "NOME_CLIENTE")
    private String nomeCliente;

    @Column(name = "CNPJ_CPF")
    private String cnpjCpf;

    @Column(name = "CONTA_CLIENTE")
    private String contaCliente;

    @Column(name = "CLASSE_RECEBER")
    private String classeReceber;

    @Column(name = "OPERADORA")
    private String operadora;

    @Column(name = "CODIGO_OPERADORA")
    private String codigoOperadora;

    @Column(name = "NUMERO_FATURA")
    private String numeroFatura;

    @Column(name = "MES_ANO_REFERENTE")
    private String mesAnoReferente;

    @Column(name = "VALOR_DOCTO_ORIGINAL")
    private BigDecimal valorDoctoOriginal;

    @Column(name = "FATURA_ATRIBUIDA")
    private String faturaAtribuida;

    @Column(name = "MES_ANO_FATURA")
    private String mesAnoFatura;

    @Column(name = "VALOR_FATURA")
    private BigDecimal valorFatura;

    @Column(name = "SALDO_DEVIDO_ORIG")
    private BigDecimal saldoDevidoOrig;

    @Column(name = "DATA_EMISSAO_FATURA")
    private Date dtEmissaoFatura;

    @Column(name = "VALOR_TOTAL_PAGAMENTO")
    private BigDecimal valorTotalPagamento;

    @Column(name = "DATA_FEBRABAN")
    private Date dataFebraban;

    @Column(name = "VALOR_ATRIBUICAO")
    private BigDecimal vlrAtribuicao;

    @Column(name = "UF")
    private String uf;

    @Column(name = "DATA_ATRIBUICAO_PGTO")
    private Date dtAtribuicao;

    @Column(name = "HORA_ATRIBUICAO_PGTO")
    private String hrAtribuicao;

    @Column(name = "STEP_EXECUTION_ID")
    private Long stepExecutionId;

    @Column(name = "JOB_EXECUTION_ID")
    private Long jobExecutionId;

}
