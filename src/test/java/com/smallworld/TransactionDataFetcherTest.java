package com.smallworld;

import com.smallworld.data.IDataReader;
import com.smallworld.domain.TopSender;
import com.smallworld.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionDataFetcherTest {

    private TransactionDataFetcher dataFetcher;
    private IDataReader<Transaction> mockReader;

    @BeforeEach
    public void setup() throws IOException {
        mockReader = mock(IDataReader.class);

        dataFetcher = new TransactionDataFetcher(mockReader);
    }

    @Test
    public void testGetTotalTransactionAmount_OneTransactionNoIssues_TotalAmount() {

        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, null, false, null));
        mockTransactions.add(new Transaction(2, 200.0, "Sender B", 28, "Receiver Y", 27, null, false, null));
        mockTransactions.add(new Transaction(3, 300.0, "Sender C", 35, "Receiver Z", 40, null, false, null));

        when(mockReader.getAll()).thenReturn(mockTransactions);

        double expectedTotal = 100.0 + 200.0 + 300.0;
        double actualTotal = dataFetcher.getTotalTransactionAmount();
        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void testGetTotalTransactionAmount_RepeatedTransactionsWithDifferentIssues_OnlyOneAmountCounted() {

        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 500.0, "Sender A", 30, "Receiver X", 25, 1, false, null));
        mockTransactions.add(new Transaction(2, 100.0, "Sender B", 28, "Receiver Y", 27, null, false, null));
        mockTransactions.add(new Transaction(1, 500.0, "Sender C", 35, "Receiver Z", 40, 2, false, null));

        when(mockReader.getAll()).thenReturn(mockTransactions);

        double expectedTotal = 600;
        double actualTotal = dataFetcher.getTotalTransactionAmount();
        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void testGetTotalTransactionAmountSentBy_OneTransactionNoIssues_TotalAmount() {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, null, false, null));
        mockTransactions.add(new Transaction(2, 200.0, "Sender B", 28, "Receiver Y", 27, null, false, null));
        mockTransactions.add(new Transaction(3, 300.0, "Sender A", 35, "Receiver Z", 40, null, false, null));

        when(mockReader.getAll()).thenReturn(mockTransactions);

        double expectedTotal = 100.0 + 300.0; // Transactions sent by "Sender A"
        double actualTotal = dataFetcher.getTotalTransactionAmountSentBy("Sender A");
        assertEquals(expectedTotal, actualTotal, 0.001); // Allow small delta for floating point comparison
    }

    @Test
    public void testGetTotalTransactionAmountSentBy_RepeatedTransactionsWithDifferentIssues_OnlyOneAmountCounted() {
        List<Transaction> mockTransactions = new ArrayList<>();
        // Repeated transactions with different issues
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, null, false, null));
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, 1, false, "Unsolved Issue"));
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, 2, true, "Solved Issue"));

        when(mockReader.getAll()).thenReturn(mockTransactions);

        double expectedTotal = 100.0; // Only one occurrence of transaction with mtn 1
        double actualTotal = dataFetcher.getTotalTransactionAmountSentBy("Sender A");
        assertEquals(expectedTotal, actualTotal, 0.001); // Allow small delta for floating point comparison
    }

    @Test
    public void testGetMaxTransactionAmount_NoTransactions_ReturnsZero() {
        List<Transaction> mockTransactions = new ArrayList<>();
        when(mockReader.getAll()).thenReturn(mockTransactions);

        double maxAmount = dataFetcher.getMaxTransactionAmount();
        assertEquals(0.0, maxAmount, 0.001);
    }

    @Test
    public void testGetMaxTransactionAmount_HasTransactions_ReturnsMaxAmount() {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, null, false, null));
        mockTransactions.add(new Transaction(2, 200.0, "Sender B", 28, "Receiver Y", 27, null, false, null));
        mockTransactions.add(new Transaction(3, 300.0, "Sender A", 35, "Receiver Z", 40, null, false, null));
        when(mockReader.getAll()).thenReturn(mockTransactions);

        double maxAmount = dataFetcher.getMaxTransactionAmount();
        assertEquals(300.0, maxAmount, 0.001);
    }

    @Test
    public void testCountUniqueClients_NoTransactions_ReturnsZero() {
        List<Transaction> mockTransactions = new ArrayList<>();
        when(mockReader.getAll()).thenReturn(mockTransactions);

        long uniqueClients = dataFetcher.countUniqueClients();
        assertEquals(0, uniqueClients);
    }

    @Test
    public void testCountUniqueClients_HasTransactions_ReturnsCorrectCount() {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, null, false, null));
        mockTransactions.add(new Transaction(2, 200.0, "Sender B", 28, "Receiver Y", 27, null, false, null));
        mockTransactions.add(new Transaction(3, 300.0, "Sender A", 35, "Receiver Z", 40, null, false, null));
        when(mockReader.getAll()).thenReturn(mockTransactions);

        long uniqueClients = dataFetcher.countUniqueClients();
        assertEquals(5, uniqueClients);
    }

    @Test
    public void testHasOpenComplianceIssues_NoTransactions_ReturnsFalse() {
        List<Transaction> mockTransactions = new ArrayList<>();
        when(mockReader.getAll()).thenReturn(mockTransactions);

        boolean hasOpenIssues = dataFetcher.hasOpenComplianceIssues("Client A");
        assertFalse(hasOpenIssues);
    }

    @Test
    public void testHasOpenComplianceIssues_ClientHasOpenIssues_ReturnsTrue() {
        List<Transaction> mockTransactions = new ArrayList<>();
        // Transaction with an open issue
        mockTransactions.add(new Transaction(1, 100.0, "Client A", 30, "Receiver X", 25, 1, false, "Unsolved Issue"));
        when(mockReader.getAll()).thenReturn(mockTransactions);

        boolean hasOpenIssues = dataFetcher.hasOpenComplianceIssues("Client A");
        assertTrue(hasOpenIssues);
    }

    @Test
    public void testHasOpenComplianceIssues_ClientHasNoOpenIssues_ReturnsFalse() {
        List<Transaction> mockTransactions = new ArrayList<>();
        // Transaction with a solved issue
        mockTransactions.add(new Transaction(1, 100.0, "Client A", 30, "Receiver X", 25, 1, true, "Solved Issue"));
        when(mockReader.getAll()).thenReturn(mockTransactions);

        boolean hasOpenIssues = dataFetcher.hasOpenComplianceIssues("Client A");
        assertFalse(hasOpenIssues);
    }

    @Test
    public void testGetTransactionsByBeneficiaryName_NoTransactions_ReturnsEmptyMap() {
        List<Transaction> mockTransactions = new ArrayList<>();
        when(mockReader.getAll()).thenReturn(mockTransactions);

        Map<String, List<Transaction>> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();
        assertTrue(transactionsByBeneficiary.isEmpty());
    }

    @Test
    public void testGetTransactionsByBeneficiaryName_HasTransactions_ReturnsMap() {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, null, false, null));
        mockTransactions.add(new Transaction(2, 200.0, "Sender B", 28, "Receiver X", 27, null, false, null));
        mockTransactions.add(new Transaction(3, 300.0, "Sender A", 35, "Receiver Y", 40, null, false, null));
        when(mockReader.getAll()).thenReturn(mockTransactions);

        Map<String, List<Transaction>> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();

        Map<String, List<Transaction>> expectedTransactions = new HashMap<>();
        expectedTransactions.put("Receiver X", mockTransactions.subList(0, 2));
        expectedTransactions.put("Receiver Y", mockTransactions.subList(2, 3));

        assertEquals(expectedTransactions, transactionsByBeneficiary);
    }

    @Test
    public void testGetUnsolvedIssueIds_NoTransactions_ReturnsEmptySet() {
        List<Transaction> mockTransactions = new ArrayList<>();
        when(mockReader.getAll()).thenReturn(mockTransactions);

        Set<Integer> unsolvedIssueIds = dataFetcher.getUnsolvedIssueIds();
        assertTrue(unsolvedIssueIds.isEmpty());
    }

    @Test
    public void testGetUnsolvedIssueIds_HasTransactions_ReturnsSet() {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, 1, false, "Unsolved Issue"));
        mockTransactions.add(new Transaction(2, 200.0, "Sender B", 28, "Receiver X", 27, null, false, null));
        mockTransactions.add(new Transaction(3, 300.0, "Sender A", 35, "Receiver Y", 40, 2, true, "Solved Issue"));
        when(mockReader.getAll()).thenReturn(mockTransactions);

        Set<Integer> unsolvedIssueIds = dataFetcher.getUnsolvedIssueIds();
        Set<Integer> expectedIssueIds = new HashSet<>();
        expectedIssueIds.add(1);

        assertEquals(expectedIssueIds, unsolvedIssueIds);
    }

    @Test
    public void testGetAllSolvedIssueMessages_NoTransactions_ReturnsEmptyList() {
        List<Transaction> mockTransactions = new ArrayList<>();
        when(mockReader.getAll()).thenReturn(mockTransactions);

        List<String> solvedIssueMessages = dataFetcher.getAllSolvedIssueMessages();
        assertTrue(solvedIssueMessages.isEmpty());
    }

    @Test
    public void testGetAllSolvedIssueMessages_HasTransactions_ReturnsList() {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, 1, true, "Solved Issue 1"));
        mockTransactions.add(new Transaction(2, 200.0, "Sender B", 28, "Receiver X", 27, null, false, null));
        mockTransactions.add(new Transaction(3, 300.0, "Sender A", 35, "Receiver Y", 40, 2, true, "Solved Issue 2"));
        mockTransactions.add(new Transaction(4, 400.0, "Sender C", 45, "Receiver Z", 50, 3, false, "Unsolved"));
        when(mockReader.getAll()).thenReturn(mockTransactions);

        List<String> solvedIssueMessages = dataFetcher.getAllSolvedIssueMessages();
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("Solved Issue 1");
        expectedMessages.add("Solved Issue 2");

        assertEquals(expectedMessages, solvedIssueMessages);
    }

    @Test
    public void testGetTop3TransactionsByAmount_NoTransactions_ReturnsEmptyList() {
        List<Transaction> mockTransactions = new ArrayList<>();
        when(mockReader.getAll()).thenReturn(mockTransactions);

        List<Transaction> top3Transactions = dataFetcher.getTop3TransactionsByAmount();
        assertTrue(top3Transactions.isEmpty());
    }

    @Test
    public void testGetTop3TransactionsByAmount_HasTransactions_ReturnsList() {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, null, false, null));
        mockTransactions.add(new Transaction(2, 200.0, "Sender B", 28, "Receiver X", 27, null, false, null));
        mockTransactions.add(new Transaction(3, 300.0, "Sender A", 35, "Receiver Y", 40, null, false, null));
        mockTransactions.add(new Transaction(4, 400.0, "Sender C", 45, "Receiver Z", 50, null, false, null));
        when(mockReader.getAll()).thenReturn(mockTransactions);

        List<Transaction> top3Transactions = dataFetcher.getTop3TransactionsByAmount();
        assertEquals(3, top3Transactions.size());
        assertEquals(400.0, top3Transactions.get(0).getAmount(), 0.001);
        assertEquals(300.0, top3Transactions.get(1).getAmount(), 0.001);
        assertEquals(200.0, top3Transactions.get(2).getAmount(), 0.001);
    }

    @Test
    public void testGetTopSender_NoTransactions_ReturnsEmpty() {
        List<Transaction> mockTransactions = new ArrayList<>();
        when(mockReader.getAll()).thenReturn(mockTransactions);

        Optional<TopSender> topSender = dataFetcher.getTopSender();
        assertEquals(Optional.empty(), topSender);
    }

    @Test
    public void testGetTopSender_HasTransactions_ReturnsTopSender() {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, null, false, null));
        mockTransactions.add(new Transaction(2, 200.0, "Sender B", 28, "Receiver X", 27, null, false, null));
        mockTransactions.add(new Transaction(3, 400.0, "Sender A", 35, "Receiver Y", 40, null, false, null));
        mockTransactions.add(new Transaction(4, 400.0, "Sender C", 45, "Receiver Z", 50, null, false, null));
        when(mockReader.getAll()).thenReturn(mockTransactions);

        Optional<TopSender> topSender = dataFetcher.getTopSender();
        assertNotEquals(Optional.empty(), topSender);
        assertEquals("Sender A", topSender.get().getSenderName());
        assertEquals(500.0, topSender.get().getAmount());
    }
}