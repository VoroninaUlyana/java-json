package com.company.exponential;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ExponentialCalculator {
    private final MathContext mathContext;
    private final BigDecimal epsilon;
    private int iterations;
    public ExponentialCalculator(int precision) {
        this.mathContext = new MathContext(precision + 10, RoundingMode.HALF_UP);
        this.epsilon = BigDecimal.ONE.movePointLeft(precision);
        this.iterations = 0;
    }
    public BigDecimal calculateExponential(BigDecimal x) {
        BigDecimal sum = BigDecimal.ONE;
        BigDecimal term = BigDecimal.ONE;
        iterations = 1;
        while (term.abs().compareTo(epsilon) > 0) {
            term = term.multiply(x, mathContext)
                    .divide(BigDecimal.valueOf(iterations), mathContext);
            sum = sum.add(term, mathContext);
            iterations++;
        }
        return sum;
    }
    public BigDecimal getEpsilon() {
        return epsilon;
    }
    public int getIterations() {
        return iterations;
    }
}