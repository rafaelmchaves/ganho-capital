package br.com.nubank.refactor;

import br.com.nubank.Tax;
import br.com.nubank.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionProcessor {

    public List<Tax> execute(Transaction[] transactions) {

        List<Tax> taxes = new ArrayList<>();
        for (Transaction transaction :transactions) {
            taxes.add(new Tax(
                    OperationType.findByName(transaction.getOperation()).getTransactionProcessing().calculate(transaction))
            );
        }

        return taxes;
    }
}
