package br.com.vivo.atlys.reader.utils;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

public class CustomDelimitedLineTokenizer extends DelimitedLineTokenizer {

    @Override
    protected boolean isQuoteCharacter(char c){
        return false;
    }
}
