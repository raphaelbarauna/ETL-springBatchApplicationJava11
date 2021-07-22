package br.com.spring.mapper;

import br.com.spring.domain.RecebivelAtlysLayout;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class RecebivelAtlysFieldSetMapper implements FieldSetMapper<RecebivelAtlysLayout> {

    @Override
    public RecebivelAtlysLayout mapFieldSet(FieldSet fieldSet) throws BindException {

        String dateFormat = "yyyyMMdd";
        Parser parser = new Parser();

        RecebivelAtlysLayout recebivelAtlysLayout = new RecebivelAtlysLayout();

        recebivelAtlysLayout.setIdCliente(fieldSet.readString(0));
        recebivelAtlysLayout.setNomeCliente(fieldSet.readString(1));
        recebivelAtlysLayout.setCnpjCpf(fieldSet.readString(2));
        recebivelAtlysLayout.setContaCliente(fieldSet.readString(3));
        recebivelAtlysLayout.setClasseReceber(fieldSet.readString(4));
        recebivelAtlysLayout.setOperadora(fieldSet.readString(5));
        recebivelAtlysLayout.setCodigoOperadora(fieldSet.readString(6));
        recebivelAtlysLayout.setNumeroFatura(fieldSet.readString(7));
        recebivelAtlysLayout.setMesAnoReferente(fieldSet.readString(8));
        recebivelAtlysLayout.setValorDoctoOriginal(parser.toBigDecimal(fieldSet.readString(9)));
        recebivelAtlysLayout.setFaturaAtribuida(fieldSet.readString(10));
        recebivelAtlysLayout.setMesAnoFatura(fieldSet.readString(11));
        recebivelAtlysLayout.setValorFatura(parser.toBigDecimal(fieldSet.readString(12)));
        recebivelAtlysLayout.setSaldoDevidoOrig(parser.toBigDecimal(fieldSet.readString(13)));
        recebivelAtlysLayout.setDtEmissaoFatura(parser.toDate(fieldSet.readString(14), dateFormat));
        recebivelAtlysLayout.setValorTotalPagamento(parser.toBigDecimal(fieldSet.readString(15)));
        recebivelAtlysLayout.setDataFebraban(parser.toDate(fieldSet.readString(16), dateFormat));
        recebivelAtlysLayout.setVlrAtribuicao(parser.toBigDecimal(fieldSet.readString(17)));
        recebivelAtlysLayout.setUf(fieldSet.readString(18));
        recebivelAtlysLayout.setDtAtribuicao(parser.toDate(fieldSet.readString(19), dateFormat));
        recebivelAtlysLayout.setHrAtribuicao(fieldSet.readString(20));

        return recebivelAtlysLayout;
    }
}
