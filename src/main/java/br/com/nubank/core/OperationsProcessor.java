package br.com.nubank.core;

import br.com.nubank.model.Operation;
import br.com.nubank.model.OperationType;
import br.com.nubank.model.OperationsData;
import br.com.nubank.model.Tax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OperationsProcessor {

    OperationsData operationsData = OperationsData.getInstance();
    public List<Tax> getTaxes(Operation[] operations) {

        cleanOperationData();
        List<Tax> taxes = new ArrayList<>();
        for (Operation operation : operations) {
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
