package com.smallworld;

import com.smallworld.config.Constants;
import com.smallworld.infra.JsonDataReader;
import com.smallworld.domain.Transaction;

import java.io.IOException;
import java.io.UncheckedIOException;

public class SmallWorldApp {
    public static void main(String[] args) {
        try {
            String jsonFilePath = Constants.TRANSACTIONS_FILE;
            TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher(new JsonDataReader<Transaction>(jsonFilePath));

            System.out.println("Total Transaction Amount: " + transactionDataFetcher.getTotalTransactionAmount());

            String senderName = "Tom Shelby";
            System.out.println("Total Transaction Amount Sent By " + senderName + ": " + transactionDataFetcher.getTotalTransactionAmountSentBy(senderName));

            System.out.println("Max Transaction Amount: " + transactionDataFetcher.getMaxTransactionAmount());

            System.out.println("Number of Unique Clients: " + transactionDataFetcher.countUniqueClients());

            String clientFullName = "Tom Shelby"; // Replace with an actual client full name
            System.out.println("Has Open Compliance Issues for " + clientFullName + ": " + transactionDataFetcher.hasOpenComplianceIssues(clientFullName));

            System.out.println("Unsolved Issue IDs: " + transactionDataFetcher.getUnsolvedIssueIds());

            System.out.println("All Solved Issue Messages: " + transactionDataFetcher.getAllSolvedIssueMessages());

            System.out.println("Top 3 Transactions By Amount: ");
            transactionDataFetcher.getTop3TransactionsByAmount().forEach(System.out::println);
        } catch (UncheckedIOException e) {
            System.err.println("Error loading data from JSON file: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
