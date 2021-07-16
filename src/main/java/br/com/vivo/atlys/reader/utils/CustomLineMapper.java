package br.com.vivo.atlys.reader.utils;

import br.com.vivo.atlys.domain.AdjAtlysLayout;
import br.com.vivo.atlys.mapper.AdjAtlysFieldSetMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class CustomLineMapper implements LineMapper<AdjAtlysLayout> {

    private final LineTokenizer lineTokenizer = adjTokenizer();

    @Override
    public AdjAtlysLayout mapLine(String line, int i) throws Exception {

        List<String> fields = new ArrayList<>(Arrays.asList(line.split("\\|")));
        if (fields.size() > 42) {
            StringBuilder stringBuilder = new StringBuilder();
            if (fields.stream().anyMatch(field -> (field.equals("PZ") || field.equals("PX")))) {
                fields.remove(21);
                fields.forEach(field -> {
                    if (!field.equals(fields.get(41)))
                        stringBuilder.append(field).append("|");
                    else
                        stringBuilder.append(field);

                });
                FieldSet fieldSet = lineTokenizer.tokenize(stringBuilder.toString());
                return new AdjAtlysFieldSetMapper().mapFieldSet(fieldSet);
            }
        }
        return new AdjAtlysFieldSetMapper().mapFieldSet(lineTokenizer.tokenize(line));
    }

    public LineTokenizer adjTokenizer() {
        CustomDelimitedLineTokenizer lineTokenizer = new CustomDelimitedLineTokenizer();
        lineTokenizer.setDelimiter("|");
        lineTokenizer.setNames("idCliente", "notaFiscal", "dtEmissaoNf", "vlrOrigNfst",
                "serie", "contaCliente", "cnpjCpf", "nomCliente", "numTerminal",
                "valorContestAjuste", "vlrAtribuido", "dataAtribuicao", "horaAtribuicao", "dataAjuste",
                "horaAjuste", "operadora", "codigoOperadora", "codMotivoAjustes", "descMotivoAjuste",
                "dataAberturaImpu", "dataFechImpug", "dsputId", "tpNotaFiscal", "classeReceber",
                "contaContabil", "descrContaContabil", "numeroFatura", "valorDoctoOriginal",
                "mesAnoReferente", "dataVencFatura", "valorPagamento", "dataAtribuicaoPgto", "faturaAtribuida",
                "dataEmissaoFatura", "mesAnoFatura", "valorFatura", "saldoDevedor", "uf", "operadoraReal",
                "saldoFaturaIni", "saldoFaturaFim", "seqRecebivel");
        return lineTokenizer;
    }
}
