package br.com.spring.mapper;

import br.com.spring.domain.AdjAtlysLayout;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class AdjAtlysFieldSetMapper implements FieldSetMapper<AdjAtlysLayout> {

    @Override
    public AdjAtlysLayout mapFieldSet(FieldSet fieldSet) throws BindException {

        Parser parser = new Parser();
        String dateFormat = "yyyyMMdd";

        AdjAtlysLayout adjAtlysLayout = new AdjAtlysLayout();

        adjAtlysLayout.setIdCliente(fieldSet.readString(0));
        adjAtlysLayout.setNotaFiscal(fieldSet.readString(1));
        adjAtlysLayout.setDtEmissaoNf(parser.toDate(fieldSet.readString(2), dateFormat));
        adjAtlysLayout.setVlrOrigNfst(parser.toBigDecimal(fieldSet.readString(3)));
        adjAtlysLayout.setSerie(fieldSet.readString(4));
        adjAtlysLayout.setContaCliente(fieldSet.readString(5));
        adjAtlysLayout.setCnpjCpf(fieldSet.readString(6));
        adjAtlysLayout.setNomCliente(fieldSet.readString(7));
        adjAtlysLayout.setNumTerminal(fieldSet.readString(8));
        adjAtlysLayout.setValorContestAjuste(parser.toBigDecimal(fieldSet.readString(9)));
        adjAtlysLayout.setVlrAtribuido(parser.toBigDecimal(fieldSet.readString(10)));
        adjAtlysLayout.setDataAtribuicao(parser.toDate(fieldSet.readString(11), dateFormat));
        adjAtlysLayout.setHoraAtribuicao(fieldSet.readString(12));
        adjAtlysLayout.setDataAjuste(parser.toDate(fieldSet.readString(13), dateFormat));
        adjAtlysLayout.setHoraAjuste(fieldSet.readString(14));
        adjAtlysLayout.setOperadora(fieldSet.readString(15));
        adjAtlysLayout.setCodigoOperadora(fieldSet.readString(16));
        adjAtlysLayout.setCodMotivoAjustes(fieldSet.readString(17));
        adjAtlysLayout.setDescMotivoAjuste(fieldSet.readString(18));
        adjAtlysLayout.setDataAberturaImpu(parser.toDate(fieldSet.readString(19), dateFormat));
        adjAtlysLayout.setDataFechImpug(parser.toDate(fieldSet.readString(20), dateFormat));
        adjAtlysLayout.setDsputId(fieldSet.readString(21));
        adjAtlysLayout.setTpNotaFiscal(fieldSet.readString(22));
        adjAtlysLayout.setClasseReceber(fieldSet.readString(23));
        adjAtlysLayout.setContaContabil(fieldSet.readString(24));
        adjAtlysLayout.setDescrContaContabil(fieldSet.readString(25));
        adjAtlysLayout.setNumeroFatura(fieldSet.readString(26));
        adjAtlysLayout.setValorDoctoOriginal(parser.toBigDecimal(fieldSet.readString(27)));
        adjAtlysLayout.setMesAnoReferente(fieldSet.readString(28));
        adjAtlysLayout.setDataVencFatura(parser.toDate(fieldSet.readString(29), dateFormat));
        adjAtlysLayout.setValorPagamento(parser.toBigDecimal(fieldSet.readString(30)));
        adjAtlysLayout.setDataAtribuicaoPgto(parser.toDate(fieldSet.readString(31), dateFormat));
        adjAtlysLayout.setFaturaAtribuida(fieldSet.readString(32));
        adjAtlysLayout.setDataEmissaoFatura(parser.toDate(fieldSet.readString(33), dateFormat));
        adjAtlysLayout.setMesAnoFatura(fieldSet.readString(34));
        adjAtlysLayout.setValorFatura(parser.toBigDecimal(fieldSet.readString(35)));
        adjAtlysLayout.setSaldoDevedor(parser.toBigDecimal(fieldSet.readString(36)));
        adjAtlysLayout.setUf(fieldSet.readString(37));
        adjAtlysLayout.setOperadoraReal(fieldSet.readString(38));
        adjAtlysLayout.setSaldoFaturaIni(parser.toBigDecimal(fieldSet.readString(39)));
        adjAtlysLayout.setSaldoFaturaFim(parser.toBigDecimal(fieldSet.readString(40)));
        adjAtlysLayout.setSeqRecebivel(fieldSet.readString(41));

        return adjAtlysLayout;
    }
}
