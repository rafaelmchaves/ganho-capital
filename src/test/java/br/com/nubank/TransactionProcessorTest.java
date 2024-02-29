package br.com.nubank;


import br.com.nubank.refactor.OperationType;
import br.com.nubank.refactor.OperationsData;
import br.com.nubank.refactor.TransactionsProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TransactionProcessorTest {

    private TransactionsProcessor transactionsProcessor;
    private final OperationsData operationsData = OperationsData.getInstance();

    @BeforeEach
    public void init() {
        transactionsProcessor = new TransactionsProcessor();
        operationsData.setStocksAmount(0);
        operationsData.setAveragePrice(BigDecimal.ZERO);
        operationsData.setLoss(BigDecimal.ZERO);
    }

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

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO));

        assertTheTransactionsWithTaxes(transactions, taxes);

    }

    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, " +
            "{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000}, {\"operation\":\"sell\", \"unit-cost\":5.00," +
            " \"quantity\": 5000}] " +
            "then Output: [{\"tax\": 0.00},{\"tax\": 10000.00},{\"tax\": 0.00}]")
    @Test
    public void getTaxes_sellPartOfSharesAtAProfit_returnTaxEquals10000ForTheSecondOperation() {
        Transaction[] transactions = new Transaction[] {
                new Transaction(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Transaction(OperationType.SELL.getName(), BigDecimal.valueOf(20), 5000),
                new Transaction(OperationType.SELL.getName(), BigDecimal.valueOf(5), 5000)
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(new BigDecimal("10000.00")), new Tax(BigDecimal.ZERO));

        assertTheTransactionsWithTaxes(transactions, taxes);
    }
    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}," +
            " {\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}, {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 3000}] " +
            "then Output: [{\"tax\": 0.00},{\"tax\": 0.00},{\"tax\": 1000.00}]")
    @Test
    public void getTaxes_sellSharesAtAProfitAfterLoss_returnTaxEquals1000ForTheThirdOperation() {
        Transaction[] transactions = new Transaction[] {
                new Transaction(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Transaction(OperationType.SELL.getName(), BigDecimal.valueOf(5), 5000),
                new Transaction(OperationType.SELL.getName(), BigDecimal.valueOf(20), 3000)
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO), new Tax(new BigDecimal("1000.00")));

        assertTheTransactionsWithTaxes(transactions, taxes);
    }

    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, " +
            "{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000}, {\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}] " +
            "then Output: [{\"tax\": 0},{\"tax\": 0},{\"tax\": 0}]")
    @Test
    public void getTaxes_buySharesTwiceAndSellOnTheAveragePrice_returnZeroTaxes() {
        Transaction[] transactions = new Transaction[] {
                new Transaction(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Transaction(OperationType.BUY.getName(), BigDecimal.valueOf(25), 5000),
                new Transaction(OperationType.SELL.getName(), BigDecimal.valueOf(15), 10000)
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO));

        assertTheTransactionsWithTaxes(transactions, taxes);
    }

    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, " +
            "{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000}, {\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}," +
            " {\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 5000}] " +
            "then Output: [{\"tax\": 0.00},{\"tax\": 0.00},{\"tax\": 0.00},{\"tax\": 10000.00}]")
    @Test
    public void getTaxes_buySharesTwiceAndSellOnTheAveragePriceAndSellAtAProfit_returnPayTaxesInTheLastOperation() {
        Transaction[] transactions = new Transaction[] {
                new Transaction(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Transaction(OperationType.BUY.getName(), BigDecimal.valueOf(25), 5000),
                new Transaction(OperationType.SELL.getName(), BigDecimal.valueOf(15), 10000),
                new Transaction(OperationType.SELL.getName(), BigDecimal.valueOf(25), 5000)
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO),
                new Tax(new BigDecimal("10000.00")));

        assertTheTransactionsWithTaxes(transactions, taxes);
    }

    private void assertTheTransactionsWithTaxes(Transaction[] transactions, List<Tax> taxes) {

        var result = transactionsProcessor.getTaxes(transactions);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(result.size(), taxes.size());
            for (int i = 0; i < result.size(); i++) {
                Assertions.assertEquals(result.get(i).getTax(), taxes.get(i).getTax());
            }
        });

    }
}
