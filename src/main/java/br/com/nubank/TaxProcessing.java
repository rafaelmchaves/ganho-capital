package br.com.nubank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class TaxProcessing {
    public List<Tax> calculateTransactionTaxes(Transaction[] transactions) {

        var averagePrice = BigDecimal.ZERO;
        var stocksAmount = 0;
        var loss = BigDecimal.ZERO;

        final var taxes = new ArrayList<Tax>();

        for (Transaction transaction :transactions) {

            BigDecimal operationValue = transaction.getUnitCost().multiply(new BigDecimal(transaction.getQuantity()));

            final var tax = new Tax();

            if (transaction.getOperation().equals("buy")) {
                BigDecimal currentPosition = averagePrice.multiply(new BigDecimal(stocksAmount));
                int totalStocks = stocksAmount + transaction.getQuantity();

                averagePrice = (currentPosition.add(operationValue))
                        .divide(new BigDecimal(totalStocks), RoundingMode.HALF_UP); //TODO check the best Rounding mode for this situation
                tax.calculate(transaction.getOperation(), BigDecimal.ZERO, operationValue);

                stocksAmount = totalStocks;

            } else {
                var profit = (transaction.getUnitCost().subtract(averagePrice)).multiply(new BigDecimal(transaction.getQuantity()));
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

                tax.calculate(transaction.getOperation(), operationValue, profit);
                stocksAmount -= transaction.getQuantity();
            }

            taxes.add(tax);
        }

        return taxes;
    }
}
