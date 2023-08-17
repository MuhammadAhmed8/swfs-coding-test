# Solution

## Domain Model
- Created a `Transaction` class to represent transaction data.
- Included attributes such as MTN, amount, sender name, beneficiary name, issue details, etc.
- Created a TopSender value object to represent top sender details and to prevent the primitive obsession

## Data Fetching
- Implemented a `TransactionDataFetcher` class to manage transaction data.
- Created the `IDataReader` interface, and a generic `JsonDataReader` to read data from a JSON file

## Unit Tests:
- Implemented unit tests for core methods using the JUnit framework.
- Followed naming convention `testMethodName_StateUnderTest_ExpectedBehavior`.

## Implemented Methods:
- `getTotalTransactionAmount`: Calculates and displays the total amount of all transactions.
- `getTotalTransactionAmountSentBy`: Calculates and displays the total amount sent by a specified sender.
- `getMaxTransactionAmount`: Retrieves and displays the highest transaction amount.
- `countUniqueClients`: Counts and displays the number of unique clients involved in transactions.
- `hasOpenComplianceIssues`: Checks and displays if a client has unresolved compliance issues.
- `getUnsolvedIssueIds`: Retrieves and displays the identifiers of unsolved compliance issues.
- `getAllSolvedIssueMessages`: Retrieves and displays all solved issue messages.
- `getTop3TransactionsByAmount`: Retrieves and displays the top 3 transactions by amount.
- `getTopSender`: Retrieves and displays the sender with the highest total sent amount.

## Main Application
- Developed a `MainApp` class to run the application.
- Utilized the `TransactionDataFetcher` class to analyze transaction data.

# Welcome to our coding test!

Your solution to this coding test will be evaluated based on its:
* Adherence to best coding practices
* Correctness
* Efficiency

Take your time to fully understand the problem and formulate a plan before starting to code, and don't hesitate to ask any questions if you have doubts.

# Objective

Since we are a money transfer company this test will revolve around a (very) simplified transaction model. Our aim is to implement the methods listed in `com.smallworld.TransactionDataFetcher`, a component that allows us to get some insight into the transactions our system has.

A battery of test transactions is stored in `transactions.json` that is going to be used as a datasource for all our data mapping needs.

Each entry in `transactions.json` consists of:
* mtn: unique identifier of the transaction
* amount
* senderFullName, senderAge: sender information
* beneficiaryFullName, beneficiaryAge: beneficiary information
* issueId, issueSolved, issueMessage: issue information. Transactions can:
  * Contain no issues: in this case, issueId = null.
  * Contain a list of issues: in this case, the transaction information will be repeated in different entries in `transactions.json` changing the issue related information.

Each method to be implemented includes a brief description of what's expected of it.

The parameters and return types of each method can be modified to fit the model that contains the transaction information.

Have fun!# swfs-test
