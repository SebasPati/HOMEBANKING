package com.mindhub.homebanking.DTOS;

import java.util.List;

public class CreateLoanDTO {
    private String name;
    private double maxAmmount;
    private List<Integer> payments;
    private double interest;


    public String getName() {
        return name;
    }

    public double getMaxAmmount() {
        return maxAmmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public double getInterest() {
        return interest;
    }
}