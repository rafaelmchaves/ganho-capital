package br.com.nubank;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transaction {
    private String operation;

    @JsonProperty("unit-cost")
    private BigDecimal unitCost;
    private int quantity;

    public Transaction(String operation, BigDecimal unitCost, int quantity) {
        this.operation = operation;
        this.unitCost = unitCost;
        this.quantity = quantity;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
