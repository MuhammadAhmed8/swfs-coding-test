package com.smallworld.domain;

public class Transaction {
    private int mtn;
    private double amount;
    private String senderFullName;
    private int senderAge;
    private String beneficiaryFullName;
    private int beneficiaryAge;
    private Integer issueId;
    private boolean issueSolved;
    private String issueMessage;

    public Transaction(){}

    public Transaction(
            int mtn,
            double amount,
            String senderFullName,
            int senderAge,
            String beneficiaryFullName,
            int beneficiaryAge,
            Integer issueId,
            boolean issueSolved,
            String issueMessage
    ) {
        this.mtn = mtn;
        this.amount = amount;
        this.senderFullName = senderFullName;
        this.senderAge = senderAge;
        this.beneficiaryFullName = beneficiaryFullName;
        this.beneficiaryAge = beneficiaryAge;
        this.issueId = issueId;
        this.issueSolved = issueSolved;
        this.issueMessage = issueMessage;
    }

    public int getMtn() {
        return mtn;
    }

    public double getAmount() {
        return amount;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public int getSenderAge() {
        return senderAge;
    }

    public String getBeneficiaryFullName() {
        return beneficiaryFullName;
    }

    public int getBeneficiaryAge() {
        return beneficiaryAge;
    }

    public Integer getIssueId() {
        return issueId;
    }

    public Boolean hasIssue() {
        return this.getIssueId() != null;
    }
    public Boolean isIssueSolved() {
        return this.getIssueId() != null && issueSolved == true;
    }

    public String getIssueMessage() {
        return issueMessage;
    }

    public Boolean hasClient(String clientFullName){
        return this.getSenderFullName().equals(clientFullName)
                || this.getBeneficiaryFullName().equals(clientFullName);
    }

    @Override
    public String toString() {
        return "MTN: " + mtn +
                "\nAmount: " + amount +
                "\nSender: " + senderFullName +
                "\nBeneficiary: " + beneficiaryFullName +
                "\nIssue ID: " + issueId +
                "\nIssue Solved: " + issueSolved +
                "\nIssue Message: " + (issueMessage != null ? issueMessage : "N/A") +
                "\n--------------------------";
    }
}
