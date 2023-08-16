package com.smallworld;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public void totalTransactionSumShouldBe600() {

        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 100.0, "Sender A", 30, "Receiver X", 25, null, false, null));
        mockTransactions.add(new Transaction(2, 200.0, "Sender B", 28, "Receiver Y", 27, null, false, null));
        mockTransactions.add(new Transaction(3, 300.0, "Sender C", 35, "Receiver Z", 40, null, false, null));

        when(mockReader.Data()).thenReturn(mockTransactions);

        double expectedTotal = 100.0 + 200.0 + 300.0;
        double actualTotal = dataFetcher.getTotalTransactionAmount();
        assertEquals(expectedTotal, actualTotal);
    }
}