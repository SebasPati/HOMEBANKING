package com.mindhub.homebanking.DTOS;



import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private long id;
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;

    private double accountBalance;

    private boolean isActive;

    public TransactionDTO() {
    }

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date =transaction.getDate();
        this.type=transaction.getType();
        this.accountBalance=transaction.getAccountBalance();
        this.isActive=transaction.isActive();
    }

    public boolean isActive() {
        return isActive;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public TransactionType getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
