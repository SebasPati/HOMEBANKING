package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOS.CreateLoanDTO;
import com.mindhub.homebanking.DTOS.LoanApplicationDTO;
import com.mindhub.homebanking.DTOS.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
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

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    LoanRepository loanRepository;

    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ClientLoansRepository clientLoansRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoanApplicationDTO() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/admin/loan")
    public ResponseEntity<String> CrearLoan (@RequestBody CreateLoanDTO createLoanDTO) {

        if (createLoanDTO.getName().isEmpty() || createLoanDTO.getPayments().isEmpty() || createLoanDTO.getMaxAmmount() == 0 || createLoanDTO.getInterest() == 0){
            return new ResponseEntity<>("Por favor ingrese todos los datos", HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(createLoanDTO.getName(), createLoanDTO.getMaxAmmount(),createLoanDTO.getPayments(),createLoanDTO.getInterest());
        loanRepository.save(loan);

        return new ResponseEntity<>("Prestamo desembolsado Pa",HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<String>newLoans(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
        Account accountDestiny = accountRepository.findByNumber(loanApplicationDTO.getNumberAccount());
        Card card = cardRepository.findByAccount(accountDestiny);

        if (loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getNumberAccount().isEmpty() || loanApplicationDTO.getPayments()==0) {
            return new ResponseEntity<>("Por favor ingrese todos los datos", HttpStatus.FORBIDDEN);
        }

        if (loan==null){
            return new ResponseEntity<>("Aun no manejamos ese tipo de prestamo",HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("El monto solicitado excede el limite del prestamo ",HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Aun no manejamos la cantidad de cuotas solicitada para este prestamo",HttpStatus.FORBIDDEN);
        }

        if(accountDestiny==null){
            return new ResponseEntity<>("Esta cuenta no existe",HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(accountDestiny)){
            return new ResponseEntity<>("No se ha podido autenticar la cuenta",HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoans = new ClientLoan(loanApplicationDTO.getAmount()*loan.getInterest(),loanApplicationDTO.getPayments(),client,loan);

        Transaction loanCredit=new Transaction(TransactionType.CREDITO, loanApplicationDTO.getAmount(),"prestamo "+loan.getName(),LocalDateTime.now(),accountDestiny,accountDestiny.getBalance()+loanApplicationDTO.getAmount(),true);

        transactionRepository.save(loanCredit);
        clientLoansRepository.save(clientLoans);

        accountDestiny.setBalance(accountDestiny.getBalance() + loanApplicationDTO.getAmount());
        if (card!=null){
            card.setQuota(accountDestiny.getBalance());
        }


        return new ResponseEntity<>("Prestamo desembolsado Pa",HttpStatus.CREATED);
    }


}

