package com.personal.calculateTaxes.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**Classe representando o modelo de imposto com o valor a ser pago - tax
 * @author TadeuMoreira
 * @since 1.0.0
 * */
@Data
@Builder
public class TaxDTO {
    private BigDecimal tax;

}