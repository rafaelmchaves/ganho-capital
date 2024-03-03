package br.com.nubank.model;

import br.com.nubank.core.BuyingOperationProcessor;
import br.com.nubank.core.OperationProcessor;
import br.com.nubank.core.SellingOperationProcessor;

import java.util.HashMap;
import java.util.Map;

public enum OperationType {

    BUY ("buy", new BuyingOperationProcessor()),
    SELL("sell", new SellingOperationProcessor());

    OperationType(String name, OperationProcessor operationProcessor) {
        this.name = name;
        this.operationProcessor = operationProcessor;
    }
    private final String name;
    private final OperationProcessor operationProcessor;

    private static final Map<String,OperationType> map;

    public String getName() {
        return name;
    }
    static {
        map = new HashMap<>();
        for (OperationType v : OperationType.values()) {
            map.put(v.name, v);
        }
    }

    public OperationProcessor getTransactionProcessing() {
        return operationProcessor;
    }


    public static OperationType findByName(String name) {
        return map.get(name);
    }
}
