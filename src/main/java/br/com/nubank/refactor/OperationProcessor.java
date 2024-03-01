package br.com.nubank.refactor;

import br.com.nubank.Operation;

import java.math.BigDecimal;

public interface OperationProcessor {

    void processTransaction(Operation operation);

    BigDecimal calculateTax(Operation operation);

    default BigDecimal calculateOperationValue(BigDecimal unitCost, int quantity) {
        return unitCost.multiply(new BigDecimal(quantity));
    }
}
