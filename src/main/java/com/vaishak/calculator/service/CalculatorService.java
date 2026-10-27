package com.vaishak.calculator.service;

import com.vaishak.calculator.exception.DivisionByZeroException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CalculatorService {

    private static final MathContext DIVISION_CONTEXT = MathContext.DECIMAL64;

    public BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b).stripTrailingZeros();
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b).stripTrailingZeros();
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b).stripTrailingZeros();
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (b.signum() == 0) {
            throw new DivisionByZeroException();
        }
        return a.divide(b, DIVISION_CONTEXT).stripTrailingZeros();
    }
}
