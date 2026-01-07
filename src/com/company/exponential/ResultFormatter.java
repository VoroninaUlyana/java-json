package com.company.exponential;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ResultFormatter {
    private final DecimalFormat df;

    public ResultFormatter() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        this.df = new DecimalFormat("0.000E0", dfs);
        df.setRoundingMode(java.math.RoundingMode.HALF_UP);
    }

    public String formatResult(BigDecimal value) {
        return df.format(value);
    }

    public BigDecimal parseFormatted(String formatted) {
        return new BigDecimal(formatted.replace("E", "E+"));
    }

    public void printResults(BigDecimal approximateValue, BigDecimal realValue,
                             BigDecimal epsilon, int iterations) {
        String approximateStr = formatResult(approximateValue);
        String realStr = formatResult(realValue);

        BigDecimal approximateRounded = parseFormatted(approximateStr);
        BigDecimal realRounded = parseFormatted(realStr);
        BigDecimal difference = approximateRounded.subtract(realRounded).abs();
        String differenceStr = formatResult(difference);

        System.out.println("=== РЕЗУЛЬТАТЫ ВЫЧИСЛЕНИЙ ===");
        System.out.printf("Приближенное значение e^x: %s%n", approximateStr);
        System.out.printf("Значение через Math.exp(x): %s%n", realStr);
        System.out.printf("Разница: %s%n", differenceStr);
        System.out.printf("Точность (epsilon): %s%n", epsilon.toPlainString());
        System.out.printf("Количество итераций: %d%n", iterations);
        System.out.println("==============================");
    }
}
