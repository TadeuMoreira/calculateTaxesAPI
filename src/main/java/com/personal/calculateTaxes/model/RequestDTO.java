package com.personal.calculateTaxes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**Classe representando o modelo de request a ser recebido para o cálculo de taxas*
 * @author TadeuMoreira
 * @since 1.0.0
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    @JsonProperty
    private OperationDTO[] operations;
}
