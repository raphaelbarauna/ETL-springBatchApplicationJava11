package br.com.spring.builder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AtlysBuilder {

    public String recebivelBuilder() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CLIENTE_ID|NOMECLIENTE|CNPJ_CPF|CONTA_CLIENTE|CLASSE_RECEBER|OPERADORA|CODIGO_OPERADORA|NUMERO_FATURA|MES_ANO_REFERENTE|VALOR_DOCTO_ORIGINAL|FATURA_ATRIBUIDA|MESANOFATURA|VALOR_FATURA|SALDO_DEVIDO_ORIG|DATA_EMISSAO_FATURA|VALOR_TOTAL_PAGAMENTO|DATA_FEBRABAN|VALOR_ATRIBUICAO|UF|DATA_ATRIBUICAO_PGTO|HORA_ATRIBUICAO_PGTO\n");
        stringBuilder.append("TESTEEEEE|TESTE TESTE TESTEO|67854519117|0225765959|" +
                "Demonstrativo TESTE|TESTE 02.558.157/0135-74|TD||122014|" +
                "135,47|0120141201|122014|135,47|1,00|20141204|99,60|20210308|1,00|RJ|20210309|08402985");

        return stringBuilder.toString();
    }

    public String detalheBuilder() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CLIENTE_ID|NOTA_FISCAL|DTEMISSAONF|VALOR_ORIG_NFST|SERIE|CONTA_CLIENTE|CNPJ_CPF|NOMECLIENTE|TERMINAL|VALOR_CONTEST_AJUSTE|" +
                "VALOR_ATRIBUIDO|DATA_ATRIBUICAO|HORA_ATRIBUICAO|DATA_AJUSTE|HORA_AJUSTE|OPERADORA|CODIGO OPERADORA|CODMOTIVOAJUSTES|DESCMOTIVOAJUSTE|" +
                "DATAABERTURAIMPU|DATAFECHIMPUG|DSPUT_ID|TP_NOTA_FISCAL|CLASSE_RECEBER|CONTACONTABIL|DESCRCONTACONTABIL|NUMERO_FATURA|VALOR_DOCTO_ORIGINAL|" +
                "MES_ANO_REFERENTE|DATAVENCFATUR|VALOR_PAGAMENTO|DATA_ATRIBUICAO_PGTO|FATU_ATRIBUIDA|DTEMISSAOFATURA|MESANOFATURA|VALOR_FATURA|SALDO_DEVEDOR|UF|" +
                "OPERADORA_REAL|SALDO_FATURA_INI|SALDO_FATURA_FIM|SEQ_RECEBIVEL\n");

        stringBuilder.append("AWN28L6LESK53EQ|12002084|20200624|29,00|C|1452144744|14559803234|TESTE TESTE SILVA|" +
                "|2,08|2,08|20210301|09041929|20210301|09041917|TESTE|00|8B|" +
                "Cobrança Externa - TESTE cobrança||||NF00MS|TESTE Fiscal 00 MS|" +
                "DEFINCAMCOBR000|DESCONTOS CONCEDIDOS S|1200208406/2020|42,12|062020|20200706|37,91|" +
                "20210227|2020200620|20200624|062020|42,12|0,00|MS|00|4,21|0,00|4W7NKNRM9D0K02T\n");

        stringBuilder.append("AWN28L6LESK53EQ|12002084|20200624|29,00|C|1452144744|14559803234|TESTE TESTE SILVA|" +
                "|2,08|2,08|20210301|09041929|20210301|09041917|TESTE|00|PZ|" +
                "TESTE TESTE - TESTE cobrança|||||NF00MS|TESTE Fiscal 00 MS|" +
                "TESTE000|DESCONTOS CONCEDIDOS S|1200208406/2020|42,12|062020|20200706|37,91|" +
                "20210227|2020200620|20200624|062020|42,12|0,00|MS|00|4,21|0,00|4W7NKNRM9D0K02T");

        return stringBuilder.toString();
    }

    public void createFiles() throws IOException {

        Files.createDirectories(Paths.get("src/test/resources/2021/202103/"));

        Path recebivelFile = Files.createFile(Paths.get("src/test/resources/2021/202103/rcvbl_MS_20210301_20210331.rpt"));
        Path detalheFile = Files.createFile(Paths.get("src/test/resources/2021/202103/adj_MS_20210301_20210331.rpt"));

        String recebivelRegistry = new AtlysBuilder().recebivelBuilder();

        String detalheRegistry = new AtlysBuilder().detalheBuilder();

        BufferedWriter recebivelWriter = new BufferedWriter(new FileWriter(recebivelFile.toString()));
        recebivelWriter.write(recebivelRegistry);
        recebivelWriter.close();

        BufferedWriter detalheWriter = new BufferedWriter(new FileWriter(detalheFile.toString()));
        detalheWriter.write(detalheRegistry);
        detalheWriter.close();

    }
}
