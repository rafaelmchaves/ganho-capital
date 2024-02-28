package br.com.nubank.refactor;

import java.util.HashMap;
import java.util.Map;

public enum OperationType {

    BUY ("buy", new BuyingTaxCalculator()),
    SELL("sell", new SellingTaxCalculator());

    OperationType(String name, TaxCalculator taxCalculator) {
        this.name = name;
        this.taxCalculator = taxCalculator;
    }
    private final String name;
    private final TaxCalculator taxCalculator;

    private static final Map<String,OperationType> map;

    static {
        map = new HashMap<>();
        for (OperationType v : OperationType.values()) {
            map.put(v.name, v);
        }
    }

    public TaxCalculator getTransactionProcessing() {
        return taxCalculator;
    }


    public static OperationType findByName(String name) {
        return map.get(name);
    }
}
