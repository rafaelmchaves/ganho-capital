package br.com.nubank.refactor;

import br.com.nubank.Operation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BuyingOperationProcessor implements OperationProcessor {

    private final OperationsData operationsData = OperationsData.getInstance();

    @Override
    public BigDecimal calculateTax(Operation operation) {
        return BigDecimal.ZERO;
    }

    @Override
    public void processTransaction(Operation operation) {
        BigDecimal currentPosition = operationsData.getAveragePrice().multiply(new BigDecimal(operationsData.getStocksAmount()));
        int totalStocks = operationsData.getStocksAmount() + operation.getQuantity();
        final var operationValue = calculateOperationValue(operation.getUnitCost(), operation.getQuantity());

        operationsData.setAveragePrice(calculateAveragePrice(operationValue, currentPosition, totalStocks));
        operationsData.setStocksAmount(totalStocks);
    }

    private static BigDecimal calculateAveragePrice(BigDecimal operationValue, BigDecimal currentPosition, int totalStocks) {
        return (currentPosition.add(operationValue))
                .divide(new BigDecimal(totalStocks), 2, RoundingMode.HALF_UP);
    }
}
