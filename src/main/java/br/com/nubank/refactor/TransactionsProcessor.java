package br.com.nubank.refactor;

import br.com.nubank.Tax;
import br.com.nubank.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionsProcessor {

    public List<Tax> getTaxes(Transaction[] transactions) {

        List<Tax> taxes = new ArrayList<>();
        for (Transaction transaction :transactions) {
            final var transactionProcessor = OperationType.findByName(transaction.getOperation()).getTransactionProcessing();
            transactionProcessor.processTransaction(transaction);
            taxes.add(new Tax(transactionProcessor.calculateTax(transaction)));
        }

        return taxes;
    }
}
