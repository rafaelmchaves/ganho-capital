package br.com.nubank.input;

import br.com.nubank.model.Operation;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputProcessor {

    public List<Operation[]> getInputData() throws IOException {
        return readInputLines();
    }

    private static List<Operation[]> readInputLines() throws IOException {
        List<Operation[]> operations = new ArrayList<>();
        var jsonInput = new String(System.in.readAllBytes());

        String[] lines = jsonInput.split("\n");
        for (String line :lines) {
            ObjectMapper objectMapper = new ObjectMapper();
            operations.add(objectMapper.readValue(line, Operation[].class));
        }
        return operations;
    }
}
