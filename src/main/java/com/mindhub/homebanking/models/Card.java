package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private  int  cvv;
    private LocalDateTime fromDate;
    private LocalDateTime truDate;
    private boolean isActive;
    private double quota;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;


    public Card() {
    }

    public Card(String cardHolder,double quota, CardType cardType, CardColor cardColor, String number, int cvv, LocalDateTime fromDate, LocalDateTime truDate, Client client, Account account,boolean isActive) {
        this.cardHolder = cardHolder;
        this.quota= quota;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.truDate = truDate;
        this.client = client;
        this.account = account;
        this.isActive = isActive;
    }

    public Card(String cardHolder,double quota, CardType cardType, CardColor cardColor, String number, int cvv, LocalDateTime fromDate, LocalDateTime truDate, Client client, boolean isActive) {
        this.cardHolder = cardHolder;
        this.quota = quota;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.truDate = truDate;
        this.client = client;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getTruDate() {
        return truDate;
    }

    public void setTruDate(LocalDateTime truDate) {
        this.truDate = truDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public double getQuota() {
        return quota;
    }

    public void setQuota(double quota) {
        this.quota = quota;
    }
}
