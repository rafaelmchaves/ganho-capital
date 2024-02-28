package br.com.nubank.refactor;

import br.com.nubank.Transaction;

import java.math.BigDecimal;

public class SellingTaxCalculator implements TaxCalculator {

    private static final BigDecimal STOCKS_SELL_PROFIT_TAX_PERCENTAGE = BigDecimal.valueOf(0.2);
    private static final BigDecimal SELL_TAX_FREE_PROFIT = BigDecimal.valueOf(20000);

    private final OperationsData operationsData = OperationsData.getInstance();
    @Override
    public BigDecimal calculate(Transaction transaction) {

        BigDecimal loss = operationsData.getLoss();
        var profit = (transaction.getUnitCost().subtract(operationsData.getAveragePrice()))
                .multiply(new BigDecimal(transaction.getQuantity()));
        if (profit.compareTo(BigDecimal.ZERO) < 0) {
            loss = loss.add(profit.abs());
        } else {
            if (loss.compareTo(profit) >= 0) {
                loss = loss.subtract(profit);
                profit = BigDecimal.ZERO;
            } else {
                profit = profit.subtract(loss);
                loss = BigDecimal.ZERO;
            }
        }

        operationsData.setLoss(loss);
        final var operationValue = calculateOperationValue(transaction.getUnitCost(), transaction.getQuantity());
        operationsData.setStocksAmount( operationsData.getStocksAmount() - transaction.getQuantity());

        return calculateTax(profit, operationValue);
    }

    private static BigDecimal calculateTax(BigDecimal profit, BigDecimal operationValue) {
        BigDecimal tax = BigDecimal.ZERO;
        if (operationValue.compareTo(SELL_TAX_FREE_PROFIT) >= 0 && profit.compareTo(BigDecimal.ZERO) > 0) {
            tax = profit.multiply(STOCKS_SELL_PROFIT_TAX_PERCENTAGE);
        }
        return tax;
    }


}
