-- TESHUVA.TB_ATLYS_ADJ definition

CREATE TABLE TB_ATLYS_ADJ (
    ID_CLIENTE VARCHAR(32),
	NOTA_FISCAL VARCHAR(10),
	DT_EMISSAO_NF DATE,
	VLR_ORIG_NFST NUMBER(10,2),
	SERIE VARCHAR2(5),
	CONTA_CLIENTE VARCHAR(20),
	CNPJ_CPF VARCHAR2(14),
	NOM_CLIENTE VARCHAR(80),
	NUM_TERMINAL VARCHAR2(20),
	VLR_CONTEST_AJUSTE NUMBER(10,2),
	VLR_ATRIBUIDO NUMBER(10,2),
	DT_ATRIBUICAO DATE,
	HR_ATRIBUICAO VARCHAR2(10),
	DT_AJUSTE DATE,
	HR_AJUSTE VARCHAR2(10),
	OPERADORA VARCHAR2(80),
	CODIGO_OPERADORA VARCHAR2(20),
	COD_MOTIVO_AJUSTES VARCHAR2(10) NOT NULL,
	DESC_MOTIVO_AJUSTE VARCHAR2(150),
	DATA_ABERTURA_IMPU DATE,
	DATA_FECH_IMPUG DATE,
	DSPUT_ID VARCHAR2(10),
	TP_NOTA_FISCAL VARCHAR2(50),
	CLASSE_RECEBER VARCHAR2(100),
	CONTA_CONTABIL VARCHAR2(50) NOT NULL,
	DESCR_CONTA_CONTABIL VARCHAR(100),
	NUMERO_FATURA VARCHAR(32),
	VALOR_DOCTO_ORIGINAL DECIMAL(15,2),
	MES_ANO_REFERENTE VARCHAR(6),
	DATA_VENC_FATUR DATE,
	VALOR_PAGAMENTO DECIMAL(15,2),
	DATA_ATRIBUICAO_PGTO DATE,
	FATU_ATRIBUIDA VARCHAR(32),
	DT_EMISSAO_FATURA DATE,
	MES_ANO_FATURA VARCHAR(6),
	VALOR_FATURA DECIMAL(15,2),
	SALDO_DEVEDOR DECIMAL(15,2),
	UF VARCHAR2(2),
	OPERADORA_REAL VARCHAR(20),
	SALDO_FATURA_INI DECIMAL(15,2),
	SALDO_FATURA_FIM DECIMAL(15,2),
	SEQ_RECEBIVEL VARCHAR(50),
	DT_REFERENCIA VARCHAR(6),
	STEP_EXECUTION_ID DECIMAL(19,0),
	JOB_EXECUTION_ID DECIMAL(19,0)
   );

-- TESHUVA.TB_ATLYS_RCBL definition

CREATE TABLE TB_ATLYS_RCBL (
    ID_CLIENTE VARCHAR(32),
	NOME_CLIENTE VARCHAR(80),
	CNPJ_CPF VARCHAR(14),
	CONTA_CLIENTE VARCHAR(20),
	CLASSE_RECEBER VARCHAR(100),
	OPERADORA VARCHAR(80),
	CODIGO_OPERADORA VARCHAR(20),
	NUMERO_FATURA VARCHAR(32),
	MES_ANO_REFERENTE VARCHAR(6),
	VALOR_DOCTO_ORIGINAL DECIMAL(15,2),
	FATURA_ATRIBUIDA VARCHAR(32),
	MES_ANO_FATURA VARCHAR(6),
	VALOR_FATURA DECIMAL(15,2),
	SALDO_DEVIDO_ORIG DECIMAL(15,2),
	DATA_EMISSAO_FATURA DATE,
	VALOR_TOTAL_PAGAMENTO DECIMAL(15,2),
	DATA_FEBRABAN DATE,
	VALOR_ATRIBUICAO DECIMAL(15,2),
	UF VARCHAR(2),
	DATA_ATRIBUICAO_PGTO DATE,
	HORA_ATRIBUICAO_PGTO VARCHAR(10),
	STEP_EXECUTION_ID DECIMAL(19,0),
	JOB_EXECUTION_ID DECIMAL(19,0)
   );