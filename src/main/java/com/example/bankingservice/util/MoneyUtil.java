package com.example.bankingservice.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneyUtil {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public static String formatCentsAsEuros(long cents) {
        BigDecimal euros = convertCentsToEuros(cents);

        return DECIMAL_FORMAT.format(euros);
    }

    public static BigDecimal convertCentsToEuros(long cents) {
        return new BigDecimal(cents).movePointLeft(2);
    }

    public static long convertEurosToCents(BigDecimal euros) {
        return euros.movePointRight(2).longValueExact();
    }

}