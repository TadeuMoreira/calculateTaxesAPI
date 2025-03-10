package com.personal.calculateTaxes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal.calculateTaxes.model.OperationDTO;
import org.springframework.stereotype.Service;

/**Serviço responsável por calcular os impostos para uma lista operaçÕes de compra e venda
 * @author TadeuMoreira
 * @since 1.0.0
 * */
@Service
public interface CalculatorService {

    /**Metódo responsável por processar uma linha contendo operações de compra e venda
     * @param operations Array de Operation contendo operaçÕes de compra e venda
     * @return String - retorna uma string em formato json com o valor dos impostos de cada operação
     * @author TadeuMoreira
     * @since 1.0.0
     */
    String calculateTaxes(OperationDTO[] operations) throws JsonProcessingException;

}