package br.com.nubank.input;

import br.com.nubank.model.Operation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputProcessor {

    public List<Operation[]> getInputData() throws IOException {
        System.out.println("passou2");
        return readInputLines();
    }

    private static List<Operation[]> readInputLines() throws IOException {
        List<String> lines = new ArrayList<>();
        var test = new String(System.in.readAllBytes());
        System.out.println("passou3: " + test);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Operation[]> operations = new ArrayList<>();
        operations.add(objectMapper.readValue(test, Operation[].class));

        return operations;
//        while (scanner.hasNextLine()) {
//            final var text = scanner.nextLine();
//            if (text.isEmpty()) {
//                break;
//            }
//            lines.add(text);
//        }
//
//        scanner.close();
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
