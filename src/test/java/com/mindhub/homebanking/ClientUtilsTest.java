package com.mindhub.homebanking;

import com.mindhub.homebanking.Utils.ClientUtils;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;



@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class ClientUtilsTest {
    @Autowired
    AccountRepository accountRepository;
    @Test
    public void clientAccountNumberIsCreated(){

        int min =00000000;
        int max =99999999;

        int accountNumber = ClientUtils.GetNumber(min,max);
        assertThat(accountNumber,is(not(0)));
    }

    @Test
    public void newAccountNumberIsNotRepeated(){
        List<Account> accounts = accountRepository.findAll();
        List<String> accountNumbers = accounts.stream().map(account -> account.getNumber()).collect(Collectors.toList());

        String number = ClientUtils.getNumber(accountNumbers);
        assertThat(number,is(not(0)));
    }

}
