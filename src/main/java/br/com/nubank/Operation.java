package br.com.nubank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Operation {
    private String operation;

    @JsonProperty("unit-cost")
    private double unitCost;
    private int quantity;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
