package com.personal.calculateTaxes.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal.calculateTaxes.model.RequestDTO;
import com.personal.calculateTaxes.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculatorControllerTest {

    @InjectMocks
    CalculatorController controller;

    @Mock
    CalculatorService service;

    @Test
    void shouldNotThrowWhenCalculateTaxes() throws JsonProcessingException {
        when(service.calculateTaxes(any())).thenReturn("");
        assertEquals("", controller.calculateTaxes(new RequestDTO()));
    }

    @Test
    void shouldThrowWhenCalculateTaxes() throws JsonProcessingException {
        when(service.calculateTaxes(any())).thenThrow(JsonProcessingException.class);
        assertThrows(JsonProcessingException.class, () -> controller.calculateTaxes(new RequestDTO()));
    }
}
