package com.threedotthree.infrastructure.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Utils {

    public static long getNumberToLongValue(String value) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        Number number = numberFormat.parse(value);
        return number.longValue();
    }

}
