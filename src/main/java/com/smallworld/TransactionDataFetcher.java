package com.smallworld;

import com.smallworld.infra.IDataReader;
import com.smallworld.domain.TopSender;
import com.smallworld.domain.Transaction;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TransactionDataFetcher {
    private IDataReader<Transaction> reader;

    public TransactionDataFetcher(IDataReader<Transaction> reader) throws IOException {
        this.reader = reader;
    }

    public List<Transaction> getAll() {
        try {
            return this.reader.getAll(Transaction[].class);
        }
        catch (IOException e){
            System.err.println("Error loading data from JSON file: " + e.getMessage());
        }

        return null;
    }

    private Stream<Transaction> getDistinct() {
            Set<Integer> uniqueTransactionMtns = new HashSet<>();

            return this.getAll()
                    .stream()
                    .filter(transaction -> uniqueTransactionMtns.add(transaction.getMtn()));
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        return this.getDistinct()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        return this.getDistinct()
                .filter(transaction -> senderFullName.equals(transaction.getSenderFullName()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        return this.getDistinct()
                .mapToDouble(Transaction::getAmount)
                .max()
                .orElse(0.0);
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        return this.getDistinct()
                .flatMap(transaction -> Stream.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName()))
                .distinct()
                .count();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return this.getAll()
                .stream()
                .filter(transaction -> transaction.hasClient(clientFullName))
                .anyMatch(transaction -> transaction.hasOpenIssue());
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        return this.getDistinct()
                .collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        return this.getAll()
                .stream()
                .filter(transaction -> transaction.hasOpenIssue())
                .map(Transaction::getIssueId)
                .collect(Collectors.toSet());    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        return this.getAll()
                .stream()
                .filter(transaction -> transaction.hasIssue() && transaction.isIssueSolved())
                .map(Transaction::getIssueMessage)
                .toList();
    }

    /**
     * Returns the 3 transactions with highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        return this.getDistinct()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Returns the sender with the most total sent amount
     */
    public Optional<TopSender> getTopSender() {

        Optional<Map.Entry<String, Double>> topSenderEntry = this.getTotalAmountsBySenderFullName()
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        return Optional.ofNullable(topSenderEntry.map(entry ->
                new TopSender(entry.getKey(), entry.getValue())
        ).orElse(null));

    }

    private Map<String, Double> getTotalAmountsBySenderFullName() {
        return this.getDistinct()
                .collect(Collectors.groupingBy(
                        Transaction::getSenderFullName,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

}
