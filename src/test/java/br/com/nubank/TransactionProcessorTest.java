package br.com.nubank;


import br.com.nubank.model.Operation;
import br.com.nubank.model.Tax;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TransactionProcessorTest {

    private OperationsProcessor transactionsProcessor;
    private final OperationsData operationsData = OperationsData.getInstance();

    @BeforeEach
    public void init() {
        transactionsProcessor = new OperationsProcessor();
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
        Operation[] operations = new Operation[] {
                new Operation(OperationType.BUY.getName(), BigDecimal.TEN, 100),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(15), 50),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(15), 50)
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO));

        assertTheTransactionsWithTaxes(operations, taxes);

    }

    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, " +
            "{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000}, {\"operation\":\"sell\", \"unit-cost\":5.00," +
            " \"quantity\": 5000}] " +
            "then Output: [{\"tax\": 0.00},{\"tax\": 10000.00},{\"tax\": 0.00}]")
    @Test
    public void getTaxes_sellPartOfSharesAtAProfit_returnTaxEquals10000ForTheSecondOperation() {
        Operation[] operations = new Operation[] {
                new Operation(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(20), 5000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(5), 5000)
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(new BigDecimal("10000.00")), new Tax(BigDecimal.ZERO));

        assertTheTransactionsWithTaxes(operations, taxes);
    }
    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}," +
            " {\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}, {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 3000}] " +
            "then Output: [{\"tax\": 0.00},{\"tax\": 0.00},{\"tax\": 1000.00}]")
    @Test
    public void getTaxes_sellSharesAtAProfitAfterLoss_returnTaxEquals1000ForTheThirdOperation() {
        Operation[] operations = new Operation[] {
                new Operation(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(5), 5000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(20), 3000)
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO), new Tax(new BigDecimal("1000.00")));

        assertTheTransactionsWithTaxes(operations, taxes);
    }

    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, " +
            "{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000}, {\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}] " +
            "then Output: [{\"tax\": 0},{\"tax\": 0},{\"tax\": 0}]")
    @Test
    public void getTaxes_buySharesTwiceAndSellOnTheAveragePrice_returnZeroTaxes() {
        Operation[] operations = new Operation[] {
                new Operation(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Operation(OperationType.BUY.getName(), BigDecimal.valueOf(25), 5000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(15), 10000)
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO));

        assertTheTransactionsWithTaxes(operations, taxes);
    }

    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, " +
            "{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000}, {\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}," +
            " {\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 5000}] " +
            "then Output: [{\"tax\": 0.00},{\"tax\": 0.00},{\"tax\": 0.00},{\"tax\": 10000.00}]")
    @Test
    public void getTaxes_buySharesTwiceAndSellOnTheAveragePriceAndSellAtAProfit_payTaxesInTheLastOperation() {
        Operation[] operations = new Operation[] {
                new Operation(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Operation(OperationType.BUY.getName(), BigDecimal.valueOf(25), 5000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(15), 10000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(25), 5000)
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO),
                new Tax(new BigDecimal("10000.00")));

        assertTheTransactionsWithTaxes(operations, taxes);
    }

    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, " +
            "{\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000}, {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000}," +
            " {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000}, {\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000}] " +
            "then Output: [{\"tax\": 0.00},{\"tax\": 0.00},{\"tax\": 0.00},{\"tax\": 0.00},{\"tax\": 3000.00}]")
    @Test
    public void getTaxes_sellShareAtALossAndSubsequentsProfits_payTaxesInTheLastOperation() {
        Operation[] operations = new Operation[] {
                new Operation(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(2), 5000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(20), 2000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(20), 2000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(25), 1000)
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO),
                new Tax(BigDecimal.ZERO), new Tax(new BigDecimal("3000.00")));

        assertTheTransactionsWithTaxes(operations, taxes);
    }

    @DisplayName("Given the follow input: [{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, {\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000}" +
            ", {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000}, {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000}," +
            " {\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000}, {\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000}, " +
            "{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 5000}, {\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 4350}, " +
            "{\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 650}] " +
            "then Output: [{\"tax\":0.00}, {\"tax\":0.00}, {\"tax\":0.00}, {\"tax\":0.00}, {\"tax\":3000.00}, {\"tax\":0.00}," +
            " {\"tax\":0.00}, {\"tax\":3700.00}, {\"tax\":0.00}]")
    @Test
    public void getTaxes_buyMoreSharesAfterSellAllShares_payTaxesTwice() {
        Operation[] operations = new Operation[] {
                new Operation(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(2), 5000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(20), 2000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(20), 2000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(25), 1000),
                new Operation(OperationType.BUY.getName(), BigDecimal.valueOf(20), 10000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(15), 5000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(30), 4350),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(30), 650)
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO),
                new Tax(BigDecimal.ZERO), new Tax(new BigDecimal("3000.00")), new Tax(BigDecimal.ZERO), new Tax(BigDecimal.ZERO),
                new Tax(new BigDecimal("3700.00")), new Tax(BigDecimal.ZERO));

        assertTheTransactionsWithTaxes(operations, taxes);
    }

    @DisplayName("Given the follow input: {\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, " +
            "{\"operation\":\"sell\", \"unit-cost\":50, \"quantity\": 10000}, {\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000}, " +
            " {\"operation\":\"sell\", \"unit-cost\":50 , \"quantity\": 10000}] " +
            "then Output: [{\"tax\":0.00},{\"tax\":80000.00},{\"tax\":0.00},{\"tax\":60000.00}]")
    @Test
    public void getTaxes_SellSharesAtABigProfit_payAHighTax() {
        Operation[] operations = new Operation[] {
                new Operation(OperationType.BUY.getName(), BigDecimal.TEN, 10000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(50), 10000),
                new Operation(OperationType.BUY.getName(), BigDecimal.valueOf(20), 10000),
                new Operation(OperationType.SELL.getName(), BigDecimal.valueOf(50), 10000),
        };

        List<Tax> taxes = Arrays.asList(new Tax(BigDecimal.ZERO), new Tax(new BigDecimal("80000.00")), new Tax(BigDecimal.ZERO),
                new Tax(new BigDecimal("60000.00")));

        assertTheTransactionsWithTaxes(operations, taxes);
    }

    private void assertTheTransactionsWithTaxes(Operation[] operations, List<Tax> taxes) {

        var result = transactionsProcessor.getTaxes(operations);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(result.size(), taxes.size());
            for (int i = 0; i < result.size(); i++) {
                Assertions.assertEquals(result.get(i).getTax(), taxes.get(i).getTax());
            }
        });

    }
}
