package com.personal.calculateTaxes.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.personal.calculateTaxes.model.OperationDTO;
import com.personal.calculateTaxes.model.TaxDTO;
import com.personal.calculateTaxes.service.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**Implementação do serviço responsável por calcular os impostos para uma lista operaçÕes de compra e venda
 * @author TadeuMoreira
 * @since 1.0.0
 * */
@Slf4j
@Service
public class CalculatorServiceImpl implements CalculatorService {

    private static final BigDecimal ZERO = BigDecimal.valueOf(0, 2);

    /**Metódo responsável por processar uma lista contendo operações de compra e venda
     * @param operations Array de Operation contendo operaçÕes de compra e venda
     * @return String - retorna uma string em formato json com o valor dos impostos de cada operação
     * @author TadeuMoreira
     * @since 1.0.0
     */
    public String calculateTaxes(OperationDTO[] operations) throws JsonProcessingException {
        log.info("CalculatorServiceImpl.calculateTaxes - Realizando cálculo de impostos da lista de operaçÕes: {}", (Object) operations);
        ObjectWriter ow = new ObjectMapper().writer();
        BigDecimal averagePrice = ZERO;
        BigDecimal accumulatedLoss = ZERO;
        long stockQuantity = 0;
        List<TaxDTO> result = new ArrayList<>();
        for (OperationDTO o : operations) {
            log.info("CalculatorServiceImpl.calculateTaxes - Começando cálculo de impostos sobre a operação: {}", o);
            TaxDTO tax = TaxDTO.builder().build();
            if (o.getOperation().equals("buy")) {
                log.info("CalculatorServiceImpl.calculateTaxes - Operação de compra, não gera cobrança de imposto.");
                tax.setTax(ZERO);
                averagePrice = calculateAveragePrice(o, averagePrice, stockQuantity);
                stockQuantity += o.getQuantity();
            } else if (isNotTaxable(o, averagePrice)) {
                log.info("CalculatorServiceImpl.calculateTaxes - Operação de venda não dedutível, não gera cobrança de imposto.");
                tax.setTax(ZERO);
                accumulatedLoss = addLoss(o, accumulatedLoss, averagePrice);
                stockQuantity -= o.getQuantity();
            } else {
                log.info("CalculatorServiceImpl.calculateTaxes - Operação de venda dedutível, gera cobrança de imposto.");
                BigDecimal profit = calculateProfit(o, averagePrice);
                if (profit.compareTo(accumulatedLoss) > 0) {
                    log.info("CalculatorServiceImpl.calculateTaxes - Lucro da operação de venda é superior ao prejuízo acumulado, deve ser deduzido imposto.");
                    tax.setTax(this.calculateTax(profit, accumulatedLoss));
                } else {
                    log.info("CalculatorServiceImpl.calculateTaxes - Lucro da operação de venda é inferior ao prejuízo acumulado, não deve ser deduzido imposto.");
                    tax.setTax(ZERO);
                }
                accumulatedLoss = deduceLoss(accumulatedLoss, profit);
                stockQuantity -= o.getQuantity();
            }
            log.info("CalculatorServiceImpl.calculateTaxes - Atualizando lista de impostos.");
            result.add(tax);
        }
        log.info("CalculatorServiceImpl.calculateTaxes - Retornando resultado de impostos calculados.");
        return ow.writeValueAsString(result);
    }

    /**Metódo que verifica se uma operação não é passível de imposto ou é
     * @param o Operation: operação contendo tipo (buy,sell), valor unitário e quantidade
     * @param averagePrice BigDecimal com o valor do preço médio até o momento
     * @return boolean - retorna true caso não seja passível de imposto e false caso seja
     * @author TadeuMoreira
     * @since 1.0.0
     */
    private boolean isNotTaxable(OperationDTO o, BigDecimal averagePrice) {
        log.info("CalculatorServiceImpl.isNotTaxable - Verificado se a operação {} é passível de dedução de imposto. Preço médio atual: R${}.", o, averagePrice);
        return BigDecimal.valueOf(o.getQuantity()).multiply(o.getUnitCost()).compareTo(BigDecimal.valueOf(20000)) <= 0 ||
                BigDecimal.valueOf(o.getQuantity()).multiply(o.getUnitCost()).compareTo(BigDecimal.valueOf(o.getQuantity()).multiply(averagePrice)) < 0;
    }

    /**Metódo responsável por calcular o preço médio ponderado de uma operação de compra
     * @param o Operation: operação contendo tipo (buy,sell), valor unitário e quantidade
     * @param averagePrice BigDecimal com o valor do preço médio até o momento
     * @param stockQuantity long com a quantidade de ativos em estoque até o momento
     * @return BigDecimal - retorna valor atualizado do preço médio ponderado
     * @author TadeuMoreira
     * @since 1.0.0
     */
    private BigDecimal calculateAveragePrice(OperationDTO o, BigDecimal averagePrice, long stockQuantity) {
        log.info("CalculatorServiceImpl.calculateAveragePrice - Calculando preço médio de compra para a operação: {}. Preço médio anterior: R${}. Quantidade em estoque: {}.", o, averagePrice, stockQuantity);
        return ((BigDecimal.valueOf(o.getQuantity()).multiply(o.getUnitCost())).add(BigDecimal.valueOf(stockQuantity).multiply(averagePrice))).divide(BigDecimal.valueOf(o.getQuantity() + stockQuantity), RoundingMode.HALF_UP);
    }

    /**Metódo responsável por adicionar perdas ao prejuízo acumulado
     * @param o Operation: operação contendo tipo (buy,sell), valor unitário e quantidade
     * @param accumulatedLoss BigDecimal com o valor do prejuízo acumulado até o momento
     * @param averagePrice BigDecimal com o valor do preço médio até o momento
     * @return BigDecimal - retorna valor atualizado do prejuízo acumulado
     * @author TadeuMoreira
     * @since 1.0.0
     */
    private BigDecimal addLoss(OperationDTO o, BigDecimal accumulatedLoss, BigDecimal averagePrice) {
        log.info("CalculatorServiceImpl.addLoss - Calculando prejuízo sobre operação de venda {}. Prejuízo acumulado: R${}. Preço médio atual: R${}.", o, accumulatedLoss, averagePrice);
        return accumulatedLoss.add((BigDecimal.valueOf(o.getQuantity()).multiply(averagePrice)).subtract(BigDecimal.valueOf(o.getQuantity()).multiply(o.getUnitCost())));
    }

    /**Metódo responsável por deduzir lucros do prejuízo acumulado
     * @param accumulatedLoss BigDecimal com o valor do prejuízo acumulado até o momento
     * @param profit BigDecimal com o valor do lucro da operação
     * @return BigDecimal - retorna valor atualizado do prejuízo acumulado
     * @author TadeuMoreira
     * @since 1.0.0
     */
    private BigDecimal deduceLoss(BigDecimal accumulatedLoss, BigDecimal profit) {
        log.info("CalculatorServiceImpl.deduceLoss - Reduzindo lucro de R${} do valor de prejuízo acumulado de R${}.", profit, accumulatedLoss);
        return accumulatedLoss.subtract(profit).compareTo(ZERO) > 0 ? accumulatedLoss.subtract(profit) : ZERO;
    }

    /**Metódo que calcular o valor de lucro de uma operação de venda
     * @param o Operation: operação contendo tipo (buy,sell), valor unitário e quantidade
     * @param averagePrice BigDecimal com o valor do preço médio até o momento
     * @return BigDecimal - retorna o valor do lucro da operação
     * @author TadeuMoreira
     * @since 1.0.0
     */
    private BigDecimal calculateProfit(OperationDTO o, BigDecimal averagePrice) {
        log.info("CalculatorServiceImpl.calculateProfit - Calculando lucro de operação {}. Preço médio atual: R${}.", o, averagePrice);
        return o.getUnitCost().multiply(BigDecimal.valueOf(o.getQuantity())).subtract(averagePrice.multiply(BigDecimal.valueOf(o.getQuantity())));
    }

    /**Metódo responsável por calcular os impostos de uma operação de venda
     * @param profit BigDecimal com o valor do lucro da operação
     * @param accumulatedLoss BigDecimal com o valor do prejuízo acumulado até o momento
     * @return BigDecimal - retorna valor de imposto a ser pago sobre a operação de venda
     * @author TadeuMoreira
     * @since 1.0.0
     */
    private BigDecimal calculateTax(BigDecimal profit, BigDecimal accumulatedLoss) {
        log.info("CalculatorServiceImpl.calculateTax - Calculando imposto a ser deduzido. Lucro da operação: R${}. Prejuízo acumulado: R${}.", profit, accumulatedLoss);
        return profit.subtract(accumulatedLoss).multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP);
    }
}
