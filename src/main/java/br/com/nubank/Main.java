package br.com.nubank;

import br.com.nubank.refactor.OperationsProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {

        final var lines = new InputProcessing().getInputData();

        final var transactionOrchestrator = new OperationsProcessor();
        final var list = lines.stream().map(transactionOrchestrator::getTaxes).toList();

        ObjectMapper objectMapper = new ObjectMapper();
        for (List<Tax> operationTaxes: list) {
            System.out.println(objectMapper.writeValueAsString(operationTaxes));
        }

    }



}