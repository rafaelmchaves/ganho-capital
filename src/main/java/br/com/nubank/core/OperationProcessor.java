package br.com.nubank.core;

import br.com.nubank.model.Operation;

import java.math.BigDecimal;

public interface OperationProcessor {

    void process(Operation operation);

    BigDecimal calculateTax(Operation operation);

    default BigDecimal calculateOperationValue(BigDecimal unitCost, int quantity) {
        return unitCost.multiply(new BigDecimal(quantity));
    }
}
