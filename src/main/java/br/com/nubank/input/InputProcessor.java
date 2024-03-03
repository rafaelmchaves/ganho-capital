package br.com.nubank.input;

import br.com.nubank.model.Operation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputProcessor {

    public List<Operation[]> getInputData() {
        final List<String> lines = readInputLines();
        return getOperationsFromJson(lines);
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

    private static List<Operation[]> getOperationsFromJson(List<String> lines) {
        ObjectMapper objectMapper = new ObjectMapper();
        return lines.stream().map(line -> {
            try {
                return objectMapper.readValue(line, Operation[].class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
