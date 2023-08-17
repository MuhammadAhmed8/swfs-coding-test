package com.smallworld.domain;

public class TopSender {
    private String senderName;
    private double amount;

    public TopSender(String senderName, double amount) {
        this.senderName = senderName;
        this.amount = amount;
    }

    public String getSenderName() {
        return senderName;
    }

    public double getAmount() {
        return amount;
    }
}
