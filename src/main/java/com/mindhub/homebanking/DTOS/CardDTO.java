package com.mindhub.homebanking.DTOS;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDateTime;

public class CardDTO {
    private long id;
    private String cardHolder;

    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private Integer cvv;
    private LocalDateTime fromDate;
    private LocalDateTime truDate;

    private double quota;

    private boolean isActive;

    public CardDTO() {
    }



    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.quota = card.getQuota();
        this.cardType=card.getCardType();
        this.cardColor =card.getCardColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.truDate = card.getTruDate();
        this.isActive = card.isActive();
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

    public boolean isActive() {
        return isActive;
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

    public void setCvv(Integer cvv) {
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

    public double getQuota() {
        return quota;
    }
}
