package br.com.nubank;


import br.com.nubank.refactor.OperationType;
import br.com.nubank.refactor.TransactionsProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class TransactionProcessorTest {

    private final TransactionsProcessor transactionsProcessor = new TransactionsProcessor();
    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 100}, " +
            "{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 50}, {\"operation\":\"sell\", \"unit-cost\":15.00," +
            " \"quantity\": 50}], " +
            "then Output: [{\"tax\": 0},{\"tax\": 0},{\"tax\": 0}]")
    @Test
    public void getTaxes_SellSharesTheSamePriceWhenBought_returnTax0() {
        Transaction[] transactions = new Transaction[] {
                new Transaction(OperationType.BUY.getName(), BigDecimal.TEN, 100),
                new Transaction(OperationType.SELL.getName(), BigDecimal.valueOf(15), 50),
                new Transaction(OperationType.SELL.getName(), BigDecimal.valueOf(15), 50)
        };

        List<Tax> taxes = transactionsProcessor.getTaxes(transactions);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(3, taxes.size());
            Assertions.assertEquals(BigDecimal.ZERO, taxes.get(0).getTax());
            Assertions.assertEquals(BigDecimal.ZERO, taxes.get(1).getTax());
            Assertions.assertEquals(BigDecimal.ZERO, taxes.get(2).getTax());
        });

    }

    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, " +
            "{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000}, {\"operation\":\"sell\", \"unit-cost\":5.00," +
            " \"quantity\": 5000}] " +
            "then Output: [{\"tax\": 0.00},{\"tax\": 10000.00},{\"tax\": 0.00}]")
    @Test
    public void getTaxes_sellPartOfSharesWithProfit_returnTaxEquals10000ForTheSecondOperation() {
        Transaction[] transactions = new Transaction[] {
                new Transaction(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Transaction(OperationType.SELL.getName(), BigDecimal.valueOf(20), 5000),
                new Transaction(OperationType.SELL.getName(), BigDecimal.valueOf(5), 5000)
        };

        List<Tax> taxes = transactionsProcessor.getTaxes(transactions);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(3, taxes.size());
            Assertions.assertEquals(BigDecimal.ZERO, taxes.get(0).getTax());
            Assertions.assertEquals(new BigDecimal("10000.00"), taxes.get(1).getTax());
            Assertions.assertEquals(BigDecimal.ZERO, taxes.get(2).getTax());
        });

    }
}
