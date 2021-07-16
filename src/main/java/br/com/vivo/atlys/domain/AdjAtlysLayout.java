package br.com.vivo.atlys.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class AdjAtlysLayout {

    @Column(name = "ID_CLIENTE")
    private String idCliente;

    @Column(name = "NOTA_FISCAL")
    private String notaFiscal;

    @Column(name = "DT_EMISSAO_NF")
    private Date dtEmissaoNf;

    @Column(name = "VLR_ORIG_NFST")
    private BigDecimal vlrOrigNfst;

    @Column(name = "SERIE")
    private String serie;

    @Column(name = "CONTA_CLIENTE")
    private String contaCliente;

    @Column(name = "CNPJ_CPF")
    private String cnpjCpf;

    @Column(name = "NOM_CLIENTE")
    private String nomCliente;

    @Column(name = "NUM_TERMINAL")
    private String numTerminal;

    @Column(name = "VLR_CONTEST_AJUSTE")
    private BigDecimal valorContestAjuste;

    @Column(name = "VLR_ATRIBUIDO")
    private BigDecimal vlrAtribuido;

    @Column(name = "DT_ATRIBUICAO")
    private Date dataAtribuicao;

    @Column(name = "HR_ATRIBUICAO")
    private String horaAtribuicao;

    @Column(name = "DT_AJUSTE")
    private Date dataAjuste;

    @Column(name = "HR_AJUSTE")
    private String horaAjuste;

    @Column(name = "OPERADORA")
    private String operadora;

    @Column(name = "CODIGO_OPERADORA")
    private String codigoOperadora;

    @Column(name = "COD_MOTIVO_AJUSTES")
    private String codMotivoAjustes;

    @Column(name = "DESC_MOTIVO_AJUSTE")
    private String descMotivoAjuste;

    @Column(name = "DATA_ABERTURA_IMPU")
    private Date dataAberturaImpu;

    @Column(name = "DATA_FECH_IMPUG")
    private Date dataFechImpug;

    @Column(name = "DSPUT_ID")
    private String dsputId;

    @Column(name = "TP_NOTA_FISCAL")
    private String tpNotaFiscal;

    @Column(name = "CLASSE_RECEBER")
    private String classeReceber;

    @Column(name = "CONTA_CONTABIL")
    private String contaContabil;

    @Column(name = "DESCR_CONTA_CONTABIL")
    private String descrContaContabil;

    @Column(name = "NUMERO_FATURA")
    private String numeroFatura;

    @Column(name = "VALOR_DOCTO_ORIGINAL")
    private BigDecimal valorDoctoOriginal;

    @Column(name = "MES_ANO_REFERENTE")
    private String mesAnoReferente;

    @Column(name = "DATA_VENC_FATUR")
    private Date dataVencFatura;

    @Column(name = "VALOR_PAGAMENTO")
    private BigDecimal valorPagamento;

    @Column(name = "DATA_ATRIBUICAO_PGTO")
    private Date dataAtribuicaoPgto;

    @Column(name = "FATU_ATRIBUIDA")
    private String faturaAtribuida;

    @Column(name = "DT_EMISSAO_FATURA")
    private Date dataEmissaoFatura;

    @Column(name = "MES_ANO_FATURA")
    private String mesAnoFatura;

    @Column(name = "VALOR_FATURA")
    private BigDecimal valorFatura;

    @Column(name = "SALDO_DEVEDOR")
    private BigDecimal saldoDevedor;

    @Column(name = "UF")
    private String uf;

    @Column(name = "OPERADORA_REAL")
    private String operadoraReal;

    @Column(name = "SALDO_FATURA_INI")
    private BigDecimal saldoFaturaIni;

    @Column(name = "SALDO_FATURA_FIM")
    private BigDecimal saldoFaturaFim;

    @Column(name = "SEQ_RECEBIVEL")
    private String seqRecebivel;

    @Column(name = "DT_REFERENCIA")
    private String dtReferencia;

    @Column(name = "STEP_EXECUTION_ID")
    private Long stepExecutionId;

    @Column(name = "JOB_EXECUTION_ID")
    private Long jobExecutionId;

}
