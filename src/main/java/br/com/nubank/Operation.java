package br.com.nubank;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Operation {

    @JsonProperty("operation")
    private String operationType;

    @JsonProperty("unit-cost")
    private BigDecimal unitCost;
    private int quantity;

    public Operation(String operationType, BigDecimal unitCost, int quantity) {
        this.operationType = operationType;
        this.unitCost = unitCost;
        this.quantity = quantity;
    }

    public Operation() {
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
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
