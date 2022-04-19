package com.threedotthree.infrastructure.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static long getNumberToLongValue(String value) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        Number number = numberFormat.parse(value);
        return number.longValue();
    }

    public static String convertHangul(String money) {
        String[] unit = {"", "만", "억"};
        int splitUnit = 10000;
        int splitSize = unit.length;
        List<String> resultList = new ArrayList<>();
        String resultString = "";

        for (int i=0; i<splitSize; i++) {
            double unitResult = (Long.parseLong(money) % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
            unitResult = Math.floor(unitResult);

            if (unitResult > 0) resultList.add(String.valueOf((int) unitResult));
            else resultList.add(null);
        }

        for (int i=0; i<resultList.size(); i++) {
            if (StringUtils.isBlank(resultList.get(i))) continue;

            String value = resultList.get(i);

            // 천 단위 숫자 중 백의 자리 이하가 없는 경우 단위표시로 수정
            if (i == 0 && Long.parseLong(value) % 1000 == 0) {
                value = String.valueOf(Long.parseLong(value) / 1000);
                resultString = value + "천" + resultString;
            } else {
                resultString = value + unit[i] + " " + resultString;
            }
        }

        return resultString.trim() + "원";
    }

}
