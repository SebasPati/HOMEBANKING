package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOS.PaymentDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.TransactionType.CREDITO;
import static com.mindhub.homebanking.models.TransactionType.DEBITO;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Transactional
    @PostMapping("/transaction")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                     @RequestParam Double amount,@RequestParam String description,
                                                     @RequestParam String Destiny,@RequestParam String Origin) {


        Client client = clientRepository.findByEmail(authentication.getName());
        Account destinyAccount=accountRepository.findByNumber(Destiny);
        Account originAccount=accountRepository.findByNumber(Origin);


        if ((amount == null) || (description.isEmpty()) || (Destiny.isEmpty()) || (Origin.isEmpty())){
            return new ResponseEntity<>("Por favor ingresa todos los campos", HttpStatus.FORBIDDEN);
        }

        if (Origin.equals(Destiny)){
            return new ResponseEntity<>("No puedes enviar dinero a la misma cuenta", HttpStatus.FORBIDDEN);
        }

        if (originAccount==null){
            return new ResponseEntity<>("La cuenta de origen no existe",HttpStatus.FORBIDDEN);
        }

        List<Account> listAccount = client.getAccounts().stream().filter(account ->
                (Objects.equals(account.getNumber(), Origin))).collect(Collectors.toList());

        if (listAccount.size() == 0) {
            return new ResponseEntity<>("cuenta no pertenece a cliente", HttpStatus.FORBIDDEN);
        }

        if (destinyAccount==null){
            return new ResponseEntity<>("La cuenta de destino no existe",HttpStatus.FORBIDDEN);
        }

        if (originAccount.getBalance()< amount || amount < 0){
            return new ResponseEntity<>("No tiene balance suficiente para hacer la transaccion",HttpStatus.FORBIDDEN);
        }

        Transaction transactionsOrigin=new Transaction(DEBITO,-amount,destinyAccount.getNumber()+" "+description, LocalDateTime.now(),originAccount,originAccount.getBalance()-amount,true);
        Transaction transactionsDestiny=new Transaction(CREDITO,amount,originAccount.getNumber()+" "+description,LocalDateTime.now(),destinyAccount,destinyAccount.getBalance()+amount,true);
        transactionRepository.save(transactionsOrigin);
        transactionRepository.save(transactionsDestiny);

        double balanceOrigin=originAccount.getBalance()-amount;
        double balanceDestiny=destinyAccount.getBalance()+amount;

        originAccount.setBalance(balanceOrigin);
        destinyAccount.setBalance(balanceDestiny);

        return new ResponseEntity<>("Tranferencia exitosa",HttpStatus.CREATED);
    }
    @Transactional
    @PostMapping("/payments")
    public ResponseEntity<Object> payment(Authentication authentication, @RequestBody PaymentDTO paymentDTO) {
        Card card = cardRepository.findByNumber(paymentDTO.getNumber());
        Account account = card.getAccount();

        if(paymentDTO.getNumber().isEmpty() || paymentDTO.getCvv() == 0 || paymentDTO.getAmount() == 0 || paymentDTO.getDescription().isEmpty()){
            return new ResponseEntity<>("Por favor ingresa todos los campos", HttpStatus.FORBIDDEN);
        }

        if(card.getTruDate().isBefore(LocalDateTime.now())){
            return new ResponseEntity<>("La tarjeta ya expiró", HttpStatus.FORBIDDEN);
        }

        if(account.getBalance() < paymentDTO.getAmount()){
            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
        }

        if(card.getCvv() != paymentDTO.getCvv()){
            return new ResponseEntity<>("Codigo de seguridad incorrecto", HttpStatus.FORBIDDEN);
        }

        if(card == null){
            return new ResponseEntity<>("Numero de tarjeta incorrecto", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(DEBITO,-paymentDTO.getAmount(), paymentDTO.getDescription(), LocalDateTime.now(),account,account.getBalance()- paymentDTO.getAmount(),true);
        account.setBalance(account.getBalance()-paymentDTO.getAmount());
        card.setQuota(card.getQuota()- paymentDTO.getAmount());
        cardRepository.save(card);
        transactionRepository.save(transaction);
        accountRepository.save(account);
        return new ResponseEntity<>("Tranferencia exitosa",HttpStatus.ACCEPTED);
    }
}


