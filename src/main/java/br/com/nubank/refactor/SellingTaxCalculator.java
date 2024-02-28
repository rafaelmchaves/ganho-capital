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

        final var newLoss = calculateLoss(profit, loss);
        final var newProfit = updateProfit(profit, loss);

        operationsData.setLoss(newLoss);
        operationsData.setStocksAmount( operationsData.getStocksAmount() - transaction.getQuantity());

        final var operationValue = calculateOperationValue(transaction.getUnitCost(), transaction.getQuantity());
        return calculateTax(newProfit, operationValue);
    }

    private BigDecimal calculateLoss(BigDecimal profit, BigDecimal currentLoss) {
        BigDecimal newLoss = BigDecimal.ZERO;

        if (profit.compareTo(BigDecimal.ZERO) < 0) {
            newLoss = currentLoss.add(profit.abs());
        } else {
            if (currentLoss.compareTo(profit) >= 0) {
                newLoss = currentLoss.subtract(profit);
            }
        }

        return newLoss;
    }

    private BigDecimal updateProfit(BigDecimal profit, BigDecimal loss) {
        if (profit.compareTo(BigDecimal.ZERO) >= 0) {
            if (loss.compareTo(profit) < 0) {
                return profit.subtract(loss);
            }
        }

        return BigDecimal.ZERO;
    }

    private static BigDecimal calculateTax(BigDecimal profit, BigDecimal operationValue) {
        BigDecimal tax = BigDecimal.ZERO;
        if (operationValue.compareTo(SELL_TAX_FREE_PROFIT) >= 0 && profit.compareTo(BigDecimal.ZERO) > 0) {
            tax = profit.multiply(STOCKS_SELL_PROFIT_TAX_PERCENTAGE);
        }
        return tax;
    }


}
