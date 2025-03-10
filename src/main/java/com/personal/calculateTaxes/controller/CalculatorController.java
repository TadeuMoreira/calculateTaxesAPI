package com.personal.calculateTaxes.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal.calculateTaxes.model.RequestDTO;
import com.personal.calculateTaxes.service.CalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**Classe responsável por mapear os endpoints da aplicação
 * @author TadeuMoreira
 * @since 1.0.0
 * */
@Slf4j
@RestController
@Tag(name = "API de cálculo de impostos", description = "API que calcula impostos baseada em uma lista de operações de compra e venda")
public class CalculatorController {

    @Autowired
    private CalculatorService service;

    /**Endpoint que recebe um RequestBody contendo uma lista de operações e retorna uma lista de impostas para as operações.
     * @param request RequestDTO: Body contendo uma lista de Operation.
     * @return String - retorna Json contendo uma lista de impostos para cada operação.
     * @author TadeuMoreira
     * @since 1.0.0
     * */
    @Operation(summary = "Cálculo de taxas de um RequestDTO.", description = "Retorna uma lista de taxas com base em uma lista de operações de compra e venda.")
    @ApiResponse(responseCode = "200", description = "Impostos calculados com sucesso")
    @PostMapping(value = "/taxes")
    public String calculateTaxes(@RequestBody RequestDTO request) throws JsonProcessingException {
        log.info("CalculatorController.calculateTaxes - Realizando cálculo de impostos a partir do request: {}", request);
        return service.calculateTaxes(request.getOperations());
    }
}
