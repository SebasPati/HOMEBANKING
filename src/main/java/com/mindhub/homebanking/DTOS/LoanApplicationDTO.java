package com.mindhub.homebanking.DTOS;

public class LoanApplicationDTO {
    private long loanId;
    private double amount;
    private int payments;
    private String numberAccount;


    public long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }
    public int   getPayments() {
        return payments;
    }
    public String getNumberAccount() {
        return numberAccount;
    }

}
