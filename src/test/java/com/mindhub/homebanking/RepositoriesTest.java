package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.mindhub.homebanking.models.CardType.DEBITO;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)

public class RepositoriesTest {



    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    TransactionRepository transactionRepository;


    @Test

    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }



    @Test

    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("personal"))));

    }
    @Test

    public void existClient(){

        List<Client> clients = clientRepository.findAll();

        assertThat(clients,is(not(empty())));

    }



    @Test

    public void existClientById(){

        Optional<Client> client = clientRepository.findById(Long.parseLong("1"));

        assertThat(client,notNullValue());

    }

    @Test

    public void existAccount(){

        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts,is(not(empty())));

    }



    @Test

    public void existAccountNumber(){

        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts, hasItem(hasProperty("number", is("VIN0001"))));

    }

    @Test

    public void existCard(){

        List<Card> cards = cardRepository.findAll();

        assertThat(cards,is(not(empty())));

    }



    @Test

    public void existDebitCard(){
        Account account = accountRepository.findByNumber("VIN0001");

        Card card = cardRepository.findByAccount(account);

        assertThat(card, hasProperty("cardType", is(DEBITO)));

    }



    @Test

    public void existTransaction(){

        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions,is(not(empty())));

    }



    @Test

    public void existTransactions(){
        Account account = accountRepository.findByNumber("VIN0001");
        List<Transaction> transactions= transactionRepository.findByAccount(account);
        assertThat(transactions,hasSize(4));

    }


}
