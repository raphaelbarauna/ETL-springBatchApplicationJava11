package br.com.spring.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Parser {
    public Date toDate(String stringDate, String format) {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(format);

        if (stringDate.isEmpty())
            return null;

        try {
            calendar.setTime(df.parse(stringDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date(calendar.getTimeInMillis());
    }

    public BigDecimal toBigDecimal(String stringValue) {

        if (stringValue.isEmpty()) {
            return new BigDecimal("0.0");
        } else {
            return new BigDecimal(stringValue.replace(",", "."));
        }


    }
}
