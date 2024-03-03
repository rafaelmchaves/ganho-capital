package br.com.nubank.refactor;

import br.com.nubank.Tax;
import br.com.nubank.Operation;

import java.util.ArrayList;
import java.util.List;

public class OperationsProcessor {

    public List<Tax> getTaxes(Operation[] operations) {

        List<Tax> taxes = new ArrayList<>();
        for (Operation operation : operations) {
            final var transactionProcessor = OperationType.findByName(operation.getOperationType()).getTransactionProcessing();
            transactionProcessor.process(operation);
            taxes.add(new Tax(transactionProcessor.calculateTax(operation)));
        }

        return taxes;
    }
}
