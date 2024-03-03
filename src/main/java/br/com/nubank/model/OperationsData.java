package br.com.nubank.model;

import java.math.BigDecimal;

public class OperationsData {

    private static OperationsData operationsData;

    public static OperationsData getInstance() {
        if (operationsData == null) {
            operationsData = new OperationsData();
        }

        return operationsData;
    }

    private OperationsData() {
        this.averagePrice = BigDecimal.ZERO;
        this.loss = BigDecimal.ZERO;
        this.stocksAmount = 0;
    }

    private BigDecimal averagePrice;
    private Integer stocksAmount;
    private BigDecimal loss;

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Integer getStocksAmount() {
        return stocksAmount;
    }

    public void setStocksAmount(Integer stocksAmount) {
        this.stocksAmount = stocksAmount;
    }

    public BigDecimal getLoss() {
        return loss;
    }

    public void setLoss(BigDecimal loss) {
        this.loss = loss;
    }

}
