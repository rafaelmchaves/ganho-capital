package br.com.nubank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        List<String> lines = readInputLines();

        final var operationLines = getOperations(lines);
        final var list = operationLines.stream().map(Main::processOperations).toList();

        for (List<Tax> operationTaxes: list) {
            operationTaxes.forEach(tax -> System.out.println("tax:" + tax.getTax()));
        }

    }

    private static List<Tax> processOperations(Transaction[] transactions) {

        var averagePrice = BigDecimal.ZERO;
        var stocksAmount = 0;
        var loss = BigDecimal.ZERO;

        final var taxes = new ArrayList<Tax>();

        for (Transaction transaction :transactions) {

            BigDecimal operationValue = transaction.getUnitCost().multiply(new BigDecimal(transaction.getQuantity()));

            final var tax = new Tax();

            if (transaction.getOperation().equals("buy")) {
                BigDecimal currentPosition = averagePrice.multiply(new BigDecimal(stocksAmount));
                int totalStocks = stocksAmount + transaction.getQuantity();

                averagePrice = (currentPosition.add(operationValue))
                        .divide(new BigDecimal(totalStocks), RoundingMode.HALF_UP); //TODO check the best Rounding mode for this situation
                tax.calculate(transaction.getOperation(), BigDecimal.ZERO, operationValue);

                stocksAmount = totalStocks;

            } else {
                var profit = (transaction.getUnitCost().subtract(averagePrice)).multiply(new BigDecimal(transaction.getQuantity()));
                if (profit.compareTo(BigDecimal.ZERO) < 0) {
                    loss = loss.add(profit.abs());
                } else {
                    if (loss.compareTo(profit) >= 0) {
                        loss = loss.subtract(profit);
                        profit = BigDecimal.ZERO;
                    } else {
                        profit = profit.subtract(loss);
                        loss = BigDecimal.ZERO;
                    }

                }

                tax.calculate(transaction.getOperation(), operationValue, profit);
                stocksAmount -= transaction.getQuantity();
            }

            taxes.add(tax);
        }

        return taxes;
    }

    private static List<Transaction[]> getOperations(List<String> lines) {
        ObjectMapper objectMapper = new ObjectMapper();
        return lines.stream().map(line -> {
            try {
                return objectMapper.readValue(line, Transaction[].class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private static List<String> readInputLines() {
        final Scanner scanner = new Scanner(System.in);
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final var text = scanner.nextLine();
            if (text.isEmpty()) {
                break;
            }
            lines.add(text);
        }

        scanner.close();
        return lines;
    }
}