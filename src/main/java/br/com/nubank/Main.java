package br.com.nubank;

import br.com.nubank.core.OperationsProcessor;
import br.com.nubank.input.InputProcessor;
import br.com.nubank.model.Tax;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        final var lines = new InputProcessor().getInputData();

        final var operationsProcessor = new OperationsProcessor();
        final var list = lines.stream().map(operationsProcessor::getTaxes).toList();

        ObjectMapper objectMapper = new ObjectMapper();
        for (List<Tax> operationTaxes: list) {
            System.out.println(objectMapper.writeValueAsString(operationTaxes));
        }

    }

}