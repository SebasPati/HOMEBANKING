package com.mindhub.homebanking.DTOS;

public class PaymentDTO {
    private String number;
    private int cvv;
    private double amount;
    private String description;


    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}