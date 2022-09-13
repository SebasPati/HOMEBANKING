package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.repositories.CardRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate ;
    private double balance;

    private AccountType type;
    private boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account",fetch= FetchType.EAGER)
    Set<Transaction> transaction = new HashSet<>();


    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    Set<Card> cards = new HashSet<>();


    public long getId() {
        return id;
    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Account() {
    }

    public Account(AccountType type, String number, LocalDateTime creationDate, double balance, Client client, boolean isActive) {
        this.type = type;
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client=client;
        this.isActive=isActive;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public Set<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(Set<Transaction> transaction) {
        this.transaction = transaction;
    }

    public Set<Card> getCards() {

        return cards.stream().filter(Card::isActive).collect(Collectors.toSet());
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }


}
