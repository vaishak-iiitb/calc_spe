package com.vaishak.calculator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/add?a=2&b=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation", is("add")))
                .andExpect(jsonPath("$.a", is(2)))
                .andExpect(jsonPath("$.b", is(3)))
                .andExpect(jsonPath("$.result", is(5)));
    }

    @Test
    void testSubtractEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/subtract?a=10&b=4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation", is("subtract")))
                .andExpect(jsonPath("$.a", is(10)))
                .andExpect(jsonPath("$.b", is(4)))
                .andExpect(jsonPath("$.result", is(6)));
    }

    @Test
    void testMultiplyEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/multiply?a=6&b=7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation", is("multiply")))
                .andExpect(jsonPath("$.a", is(6)))
                .andExpect(jsonPath("$.b", is(7)))
                .andExpect(jsonPath("$.result", is(42)));
    }

    @Test
    void testDivideEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/divide?a=10&b=4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation", is("divide")))
                .andExpect(jsonPath("$.a", is(10)))
                .andExpect(jsonPath("$.b", is(4)))
                .andExpect(jsonPath("$.result", is(2.5)));
    }

    @Test
    void testDivideByZeroThrows400() throws Exception {
        mockMvc.perform(get("/api/v1/divide?a=1&b=0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", containsString("zero")))
                .andExpect(jsonPath("$.path", is("/api/v1/divide")));
    }

    @Test
    void testMissingParameterReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/add?a=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", containsString("'b'")))
                .andExpect(jsonPath("$.path", is("/api/v1/add")));
    }

    @Test
    void testNonNumericParameterReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/add?a=abc&b=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", containsString("'a'")))
                .andExpect(jsonPath("$.path", is("/api/v1/add")));
    }

    @Test
    void testActuatorHealth() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    void testActuatorInfo() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app.name", is("calculator-api")));
    }
}
