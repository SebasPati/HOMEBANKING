package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private Double maxAmount;

    private double interest;


    public Loan() {
    }
    @ElementCollection
    @Column(name="payment")
    private List<Integer> payments= new ArrayList<>();

    @OneToMany(mappedBy = "loan",fetch = FetchType.EAGER)
    Set<ClientLoan> clientLoans =new HashSet<>();
    public Loan(String name, double maxAmount, List<Integer> payments,double interest) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interest = interest;
    }


    public long getId() {
        return id;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }
}
