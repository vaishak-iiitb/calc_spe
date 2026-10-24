package com.vaishak.calculator.service;

import com.vaishak.calculator.exception.DivisionByZeroException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculatorServiceTest {

    private CalculatorService service;

    @BeforeEach
    void setUp() {
        service = new CalculatorService();
    }

    @ParameterizedTest
    @CsvSource({
            "2, 3, 5",
            "-2, 3, 1",
            "-5, -7, -12",
            "0, 0, 0",
            "10.5, 4.5, 15"
    })
    void testAdd(String a, String b, String expected) {
        BigDecimal result = service.add(new BigDecimal(a), new BigDecimal(b));
        assertThat(result).isEqualByComparingTo(new BigDecimal(expected));
    }

    @Test
    void testAddPrecisionDecimal() {
        BigDecimal result = service.add(new BigDecimal("0.1"), new BigDecimal("0.2"));
        assertThat(result).isEqualByComparingTo(new BigDecimal("0.3"));
    }

    @ParameterizedTest
    @CsvSource({
            "5, 3, 2",
            "3, 5, -2",
            "-5, -3, -2",
            "0, 7, -7",
            "10.5, 0.5, 10"
    })
    void testSubtract(String a, String b, String expected) {
        BigDecimal result = service.subtract(new BigDecimal(a), new BigDecimal(b));
        assertThat(result).isEqualByComparingTo(new BigDecimal(expected));
    }

    @ParameterizedTest
    @CsvSource({
            "2, 3, 6",
            "-2, 3, -6",
            "-4, -5, 20",
            "7, 0, 0",
            "0, 9, 0",
            "2.5, 4, 10"
    })
    void testMultiply(String a, String b, String expected) {
        BigDecimal result = service.multiply(new BigDecimal(a), new BigDecimal(b));
        assertThat(result).isEqualByComparingTo(new BigDecimal(expected));
    }

    @ParameterizedTest
    @CsvSource({
            "10, 4, 2.5",
            "10, 2, 5",
            "-10, 2, -5",
            "10, -2, -5",
            "-10, -2, 5",
            "0, 5, 0"
    })
    void testDivide(String a, String b, String expected) {
        BigDecimal result = service.divide(new BigDecimal(a), new BigDecimal(b));
        assertThat(result).isEqualByComparingTo(new BigDecimal(expected));
    }

    @Test
    void testDivideRepeating() {
        BigDecimal result = service.divide(new BigDecimal("1"), new BigDecimal("3"));
        assertThat(result.toString()).startsWith("0.3333333333333333");
    }

    @Test
    void testDivideByZeroThrowsException() {
        assertThatThrownBy(() -> service.divide(BigDecimal.TEN, BigDecimal.ZERO))
                .isInstanceOf(DivisionByZeroException.class)
                .hasMessageContaining("zero");
    }
}
