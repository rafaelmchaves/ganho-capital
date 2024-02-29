package br.com.nubank.refactor;

import br.com.nubank.Transaction;

import java.math.BigDecimal;

public interface OperationProcessor {

    void processTransaction(Transaction transaction);

    BigDecimal calculateTax(Transaction transaction);

    default BigDecimal calculateOperationValue(BigDecimal unitCost, int quantity) {
        return unitCost.multiply(new BigDecimal(quantity));
    }
}
