package br.com.nubank.refactor;

import br.com.nubank.Transaction;

import java.math.BigDecimal;

public interface TaxCalculator {

    BigDecimal calculate(Transaction transaction);

    default BigDecimal calculateOperationValue(BigDecimal unitCost, int quantity) {
        return unitCost.multiply(new BigDecimal(quantity));
    }
}
