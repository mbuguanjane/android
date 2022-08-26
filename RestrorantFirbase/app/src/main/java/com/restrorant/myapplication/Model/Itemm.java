package com.restrorant.myapplication.Model;

public class Itemm {
   Long Amount,TransactionDate,PhoneNumber;
   String MpesaReceiptNumber;
   String Balance;

    public Itemm() {
    }

    public Itemm(Long amount, Long transactionDate, Long phoneNumber, String mpesaReceiptNumber, String balance) {
        Amount = amount;
        TransactionDate = transactionDate;
        PhoneNumber = phoneNumber;
        MpesaReceiptNumber = mpesaReceiptNumber;
        Balance = balance;
    }

    public Long getAmount() {
        return Amount;
    }

    public void setAmount(Long amount) {
        Amount = amount;
    }

    public Long getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(Long transactionDate) {
        TransactionDate = transactionDate;
    }

    public Long getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getMpesaReceiptNumber() {
        return MpesaReceiptNumber;
    }

    public void setMpesaReceiptNumber(String mpesaReceiptNumber) {
        MpesaReceiptNumber = mpesaReceiptNumber;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }
}
