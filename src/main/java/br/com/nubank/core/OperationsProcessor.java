package br.com.nubank.core;

import br.com.nubank.model.Operation;
import br.com.nubank.model.OperationType;
import br.com.nubank.model.OperationsData;
import br.com.nubank.model.Tax;

import java.util.ArrayList;
import java.util.List;

public class OperationsProcessor {

    OperationsData operationsData = OperationsData.getInstance();
    public List<Tax> getTaxes(Operation[] operations) {

        List<Tax> taxes = new ArrayList<>();
        for (Operation operation : operations) {
            final var operationProcessor = OperationType.findByName(operation.getOperationType()).getTransactionProcessing();
            operationProcessor.process(operation);
            taxes.add(new Tax(operationProcessor.calculateTax(operation)));
        }

        operationsData.clean();
        return taxes;
    }

}
