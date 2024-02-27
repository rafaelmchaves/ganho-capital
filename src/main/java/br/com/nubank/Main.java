package br.com.nubank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        List<String> lines = readInputLines();

        final var operationLines = getOperations(lines);
        operationLines.forEach(Main::processOperations);

    }

    private static void processOperations(Operation[] operations) {

        var averagePrice = BigDecimal.ZERO;
        var stocksAmount = 0;
        var profit = BigDecimal.ZERO;

        final var taxes = new ArrayList<BigDecimal>();

//        Arrays.stream(operations).forEach(operation ->
//        {
//            if (operation.getOperation().equals("buy")) {
//              nova-media-ponderada = ((quantidade-de-acoes-atual * media-ponderada-
//atual) + (quantidade-de-acoes * valor-de-compra)) / (quantidade-de-acoes-atual + quantidade-
//de-acoes-compradas)
//               profit = profit + operation.quantity * unitCost
//                averagePrice = (averagePrice.multiply(new BigDecimal(stocksAmount)));
//                taxes.add(BigDecimal.ZERO);
//            } else {
//
//            }
//        });
    }

    private static List<Operation[]> getOperations(List<String> lines) {
        ObjectMapper objectMapper = new ObjectMapper();
        return lines.stream().map(line -> {
            try {
                return objectMapper.readValue(line, Operation[].class);
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