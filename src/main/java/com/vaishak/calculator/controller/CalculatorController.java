package com.vaishak.calculator.controller;

import com.vaishak.calculator.dto.CalculationResponse;
import com.vaishak.calculator.service.CalculatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1")
public class CalculatorController {

    private final CalculatorService service;

    public CalculatorController(CalculatorService service) {
        this.service = service;
    }

    @GetMapping("/add")
    public CalculationResponse add(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        return new CalculationResponse("add", a, b, service.add(a, b));
    }

    @GetMapping("/subtract")
    public CalculationResponse subtract(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        return new CalculationResponse("subtract", a, b, service.subtract(a, b));
    }

    @GetMapping("/multiply")
    public CalculationResponse multiply(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        return new CalculationResponse("multiply", a, b, service.multiply(a, b));
    }

    @GetMapping("/divide")
    public CalculationResponse divide(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        return new CalculationResponse("divide", a, b, service.divide(a, b));
    }
}
