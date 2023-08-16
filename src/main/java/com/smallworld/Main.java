package com.smallworld;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Provide the path to the transactions.json file
            String jsonFilePath = "transactions.json";
            TransactionDataFetcher dataFetcher = new TransactionDataFetcher(new JsonDataReader<Transaction>(jsonFilePath));
            List<Transaction> transactions = dataFetcher.getAll();

            transactions.forEach(System.out::println);
            double totalAmount = dataFetcher.getTotalTransactionAmount();
            System.out.println("Total Transaction Amount: " + totalAmount);
//
//            double amountSentByAlice = dataFetcher.getTotalTransactionAmountSentBy("Alice");
//            System.out.println("Total Amount Sent by Alice: " + amountSentByAlice);

            // Perform similar operations using other methods
        } catch (UncheckedIOException e) {
            System.err.println("Error loading data from JSON file: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
