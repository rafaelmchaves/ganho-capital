package br.com.nubank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {

        final var lines = new InputProcessing().getInputData();

        final var taxProcessing = new TaxProcessing();
        final var list = lines.stream().map(taxProcessing::calculateTransactionTaxes).toList();

        ObjectMapper objectMapper = new ObjectMapper();
        for (List<Tax> operationTaxes: list) {
            System.out.println(objectMapper.writeValueAsString(operationTaxes));
        }

    }



}