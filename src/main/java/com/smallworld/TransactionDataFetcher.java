package com.smallworld;

import com.smallworld.data.IDataReader;
import com.smallworld.domain.Transaction;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TransactionDataFetcher {
    private IDataReader<Transaction> reader;

    public TransactionDataFetcher(IDataReader<Transaction> reader) throws IOException {
        this.reader = reader;
        load();
    }

    private void load() throws IOException {
        this.reader.read(Transaction[].class);
    }

    private Stream<Transaction> getUniques() {
        Set<Integer> uniqueTransactionMtns = new HashSet<>();

        return this.reader.Data()
                .stream()
                .filter(transaction -> uniqueTransactionMtns.add(transaction.getMtn()));
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        return this.getUniques()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        return this.getUniques()
                .filter(transaction -> transaction.getSenderFullName() == senderFullName)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        return this.getUniques()
                .mapToDouble(Transaction::getAmount)
                .max()
                .orElse(0.0);
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        return this.getUniques()
                .flatMap(transaction -> Stream.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName()))
                .distinct()
                .count();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return this.getUniques()
                .filter(transaction -> transaction.hasClient(clientFullName))
                .anyMatch(transaction -> !transaction.isIssueSolved());
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        return this.getUniques()
                .collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        return this.getUniques()
                .filter(transaction -> transaction.hasIssue() && !transaction.isIssueSolved())
                .map(Transaction::getIssueId)
                .collect(Collectors.toSet());    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        return this.getUniques()
                .filter(transaction -> transaction.isIssueSolved())
                .map(Transaction::getIssueMessage)
                .toList();
    }

    /**
     * Returns the 3 transactions with highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        return this.reader.Data().stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Returns the sender with the most total sent amount
     */
    public Optional<Object> getTopSender() {
        throw new UnsupportedOperationException();
    }

}
