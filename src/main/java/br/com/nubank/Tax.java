package br.com.nubank;

import java.math.BigDecimal;

public class Tax {

    private BigDecimal tax;

    public BigDecimal getTax() {
        return tax;
    }

    private static final BigDecimal STOCKS_SELL_PROFIT_TAX_PERCENTAGE = BigDecimal.valueOf(0.2);
    private static final BigDecimal SELL_TAX_FREE_PROFIT = BigDecimal.valueOf(20000);

    public void calculate(String operation, BigDecimal operationValue, BigDecimal profit) {

        tax = BigDecimal.ZERO;
        if (operation.equals("sell")) {
            if (operationValue.compareTo(SELL_TAX_FREE_PROFIT) >= 0 && profit.compareTo(BigDecimal.ZERO) > 0) {
                tax = (profit).multiply(STOCKS_SELL_PROFIT_TAX_PERCENTAGE);
            }
        }

    }


}
