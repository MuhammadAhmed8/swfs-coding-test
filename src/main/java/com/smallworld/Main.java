package com.smallworld;

import com.smallworld.data.JsonDataReader;
import com.smallworld.domain.Transaction;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Provide the path to the transactions.json file
            String jsonFilePath = "transactions.json";
            TransactionDataFetcher dataFetcher = new TransactionDataFetcher(new JsonDataReader<Transaction>(jsonFilePath));
            double totalAmount = dataFetcher.getTotalTransactionAmount();
            System.out.println("Total Transaction Amount: " + totalAmount);

        } catch (UncheckedIOException e) {
            System.err.println("Error loading data from JSON file: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
