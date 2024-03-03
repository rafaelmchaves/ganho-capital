package br.com.nubank.refactor;

import br.com.nubank.Tax;
import br.com.nubank.Operation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OperationsProcessor {

    OperationsData operationsData = OperationsData.getInstance();
    public List<Tax> getTaxes(Operation[] operations) {

        List<Tax> taxes = new ArrayList<>();
        for (Operation operation : operations) {
            cleanOperationData();
            final var transactionProcessor = OperationType.findByName(operation.getOperationType()).getTransactionProcessing();
            transactionProcessor.process(operation);
            taxes.add(new Tax(transactionProcessor.calculateTax(operation)));
        }

        return taxes;
    }

    private void cleanOperationData() {
        operationsData.setLoss(BigDecimal.ZERO);
        operationsData.setAveragePrice(BigDecimal.ZERO);
        operationsData.setStocksAmount(0);
    }
}
