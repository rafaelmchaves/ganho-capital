package br.com.nubank.refactor;

import br.com.nubank.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SellingOperationProcessor implements OperationProcessor {

    private BigDecimal profit;

    private static final BigDecimal STOCKS_SELL_PROFIT_TAX_PERCENTAGE = BigDecimal.valueOf(0.2);
    private static final BigDecimal SELL_TAX_FREE_PROFIT = BigDecimal.valueOf(20000);

    private final OperationsData operationsData = OperationsData.getInstance();

    @Override
    public void processTransaction(Transaction transaction) {
        BigDecimal loss = operationsData.getLoss();
        var profit = (transaction.getUnitCost().subtract(operationsData.getAveragePrice()))
                .multiply(new BigDecimal(transaction.getQuantity()));

        final var newLoss = calculateLoss(profit, loss);
        this.profit = updateProfit(profit, loss);

        operationsData.setLoss(newLoss);
        operationsData.setStocksAmount( operationsData.getStocksAmount() - transaction.getQuantity());
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

    @Override
    public BigDecimal calculateTax(Transaction transaction) {
        final var operationValue = calculateOperationValue(transaction.getUnitCost(), transaction.getQuantity());
        BigDecimal tax = BigDecimal.ZERO;
        if (operationValue.compareTo(SELL_TAX_FREE_PROFIT) >= 0 && this.profit.compareTo(BigDecimal.ZERO) > 0) {
            tax = this.profit.multiply(STOCKS_SELL_PROFIT_TAX_PERCENTAGE).setScale(2, RoundingMode.HALF_UP);
        }
        return tax;
    }


}
