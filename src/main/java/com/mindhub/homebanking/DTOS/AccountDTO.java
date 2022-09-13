package com.mindhub.homebanking.DTOS;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;


public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    private Set<TransactionDTO> transaction;

    private Set<CardDTO> debitCard;

    private boolean isActive;

    private AccountType type;

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.type = account.getType();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transaction= account.getTransaction().stream().map(TransactionDTO::new).collect(Collectors.toSet());
        this.debitCard = account.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
        this.isActive = account.isActive();
    }

    public AccountType getType() {
        return type;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<TransactionDTO> getTransaction() {
        return transaction;
    }

    public void setTransaction(Set<TransactionDTO> transaction) {
        this.transaction = transaction;
    }

    public Set<CardDTO> getDebitCard() {
        return debitCard;
    }
}