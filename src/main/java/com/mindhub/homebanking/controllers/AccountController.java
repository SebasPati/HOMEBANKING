package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOS.AccountDTO;
import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CardRepository cardRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getaccountsDTO() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }


    @GetMapping("/accounts/{id}")
    public AccountDTO accountDTO(@PathVariable Long id) {
        AccountDTO accountId = new AccountDTO(accountRepository.findById(id).orElse(null));
        return accountId;
    }

    int min = 10000000;
    int max = 99999999;

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String getStringRandomNumber() {
        int randomNumber = getRandomNumber(min, max);
        return String.valueOf(randomNumber);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType type) {

        Client client = clientRepository.findByEmail(authentication.getName());

        Set<Account> accountListSave = client.getAccounts().stream().filter(account -> account.isActive() && account.getType() == AccountType.AHORROS).collect(Collectors.toSet());

        Set<Account> accountListCurrent = client.getAccounts().stream().filter(account -> account.isActive() && account.getType() == AccountType.CORRIENTE).collect(Collectors.toSet());

        if(type == null){
            return new ResponseEntity<>("Por favor ingresa todos los campos", HttpStatus.FORBIDDEN);
        }

        if(type == AccountType.AHORROS){
            if (accountListSave.size() >= 3) {
                return new ResponseEntity<>("Ya tienes el maximo de cuentas de ahorro permitidas", HttpStatus.FORBIDDEN);
            }
        }else if (type == AccountType.CORRIENTE){
            if (accountListCurrent.size() >= 3) {
                return new ResponseEntity<>("Ya tienes el maximo de cuentas corriente permitidas", HttpStatus.FORBIDDEN);
            }
        }

        List<String> accountNumbers = accountRepository.findAll().stream().map(Account::getNumber).collect(Collectors.toList());

        int tamaño = 0;

        String numeroCuenta;

        do {
            numeroCuenta = "VIN" + getStringRandomNumber();
            String finalNumeroCuenta = numeroCuenta;
            List<String> compare = accountNumbers.stream().filter(numero -> numero.equals(finalNumeroCuenta)).collect(Collectors.toList());
            tamaño = compare.size();
        } while (tamaño != 0);


        Account account = new Account(type,numeroCuenta, LocalDateTime.now(), 0, client,true);

        accountRepository.save(account);

        return new ResponseEntity<>("Cuenta creada exitosamente", HttpStatus.CREATED);
    }

    @PatchMapping("/account")
    public ResponseEntity<Object> changeAccount(@RequestParam String numberAccount) {

        Account account = accountRepository.findByNumber(numberAccount);

        if(account.getBalance() > 0){
            return new ResponseEntity<>("No puedes eliminar una cuenta si tiene fondos activos", HttpStatus.FORBIDDEN);
        }

        account.setActive(false);
        account.getTransaction().forEach(transaction -> transaction.setActive(false));
        Set<Card> cardNumber = account.getCards().stream().filter(Card::isActive).collect(Collectors.toSet());
        cardNumber.forEach(card -> card.setActive(false));

        accountRepository.save(account);
        account.getTransaction().forEach(transaction -> transactionRepository.save(transaction));
        cardNumber.forEach(card -> cardRepository.save(card));

        return new ResponseEntity<>("Cuenta eliminada exitosamente", HttpStatus.ACCEPTED);
    }

}









