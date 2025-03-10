package com.personal.calculateTaxes.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal.calculateTaxes.model.OperationDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceImplTest {

    @InjectMocks
    CalculatorServiceImpl service;

    @Test
    void shouldNotBeNullWhenCalculateTaxes() throws JsonProcessingException {
        OperationDTO[] operations = new OperationDTO[9];
        operations[0] = OperationDTO.builder().operation("buy").quantity(10000L).unitCost(BigDecimal.valueOf(10)).build();
        operations[1] = OperationDTO.builder().operation("sell").quantity(5000L).unitCost(BigDecimal.valueOf(2)).build();
        operations[2] = OperationDTO.builder().operation("sell").quantity(2000L).unitCost(BigDecimal.valueOf(20)).build();
        operations[3] = OperationDTO.builder().operation("sell").quantity(2000L).unitCost(BigDecimal.valueOf(20)).build();
        operations[4] = OperationDTO.builder().operation("sell").quantity(1000L).unitCost(BigDecimal.valueOf(30)).build();
        operations[5] = OperationDTO.builder().operation("buy").quantity(10000L).unitCost(BigDecimal.valueOf(20)).build();
        operations[6] = OperationDTO.builder().operation("sell").quantity(5000L).unitCost(BigDecimal.valueOf(15)).build();
        operations[7] = OperationDTO.builder().operation("sell").quantity(4350L).unitCost(BigDecimal.valueOf(30)).build();
        operations[8] = OperationDTO.builder().operation("sell").quantity(650L).unitCost(BigDecimal.valueOf(30)).build();
        assertEquals("[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":4000.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":3700.00},{\"tax\":0.00}]", service.calculateTaxes(operations));
    }

}
