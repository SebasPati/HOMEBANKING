package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTOS.CardDTO;
import com.mindhub.homebanking.Utils.CardUtils;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/cards")
    public List<CardDTO> cardDTo() {
        return cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/cards/{id}")
    public CardDTO getcardDTO(@PathVariable Long id) {
        CardDTO cardId = new CardDTO(cardRepository.findById(id).orElse(null));
        return cardId;
    }

    @GetMapping("/clients/current/cards")
    public List<CardDTO> cardsDTo(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards().stream().map(CardDTO::new).collect(Collectors.toList());
    }



    public int getRandomCardNumber(int min2, int max2) {
        return CardUtils.GetRandomNumber(min2, max2);
    }

    public String getRandomCardNumber() {
        return CardUtils.getString();
    }
    

    public String getRandomStringCard() {
        return CardUtils.getStrings();
    }
    

    public int getRandomNumber(int min1, int max1) {
        return CardUtils.getCvv();
    }



    @PostMapping("clients/current/debitCards")
    public ResponseEntity<Object> createDebitCard(Authentication authentication, @RequestParam CardColor cardColor, @RequestParam String accountNumber) {

        Client clientCurrent = clientRepository.findByEmail(authentication.getName());

        Account accountCurrent = accountRepository.findByNumber(accountNumber);

        Set<Card> cards = clientCurrent.getCards().stream().filter(card -> card.getCardType() == CardType.DEBITO).filter(card -> card.getCardColor() == cardColor && card.isActive()).collect(Collectors.toSet());

        Set<Card> accounts = accountCurrent.getCards();

        if(accountNumber.isEmpty()){
            return new ResponseEntity<>("Por favor ingresa numero de cuenta", HttpStatus.FORBIDDEN);
        }

        if (accounts.size() >= 1){
            return new ResponseEntity<>("No puedes tener mas de una tarjeta para una misma cuenta", HttpStatus.FORBIDDEN);
        }

        if (!clientCurrent.getAccounts().contains(accountCurrent)){
            return new ResponseEntity<>("Cuenta inexistente", HttpStatus.FORBIDDEN);
        }

        if (cards.size() > 0){
            return new ResponseEntity<>("Ya cuentas con una tarjeta debito de este color", HttpStatus.FORBIDDEN);
        }

        String cardNumber = getRandomStringCard();
        int cvv = getRandomNumber(100, 999);

        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime thruDate = fromDate.plusYears(5);

        Card cards1 = new Card(clientCurrent.getFirstName() + " " + clientCurrent.getLastName(),accountCurrent.getBalance(),CardType.DEBITO, cardColor, cardNumber, cvv, fromDate, thruDate, clientCurrent,accountCurrent,true);
        cardRepository.save(cards1);
        return new ResponseEntity<>("Has creado una tarjeta con exito", HttpStatus.CREATED);
    }

    @PostMapping("clients/current/creditCards")
    public ResponseEntity<Object> createCreditCard(Authentication authentication, @RequestParam CardColor cardColor) {

        Client clientCurrent = clientRepository.findByEmail(authentication.getName());

        List<Card> listCard = clientCurrent.getCreditCards().stream().filter(card ->
                (card.getCardColor() == cardColor && card.isActive())).collect(Collectors.toList());


        if (listCard.size() > 0) {
            return new ResponseEntity<>("Ya tienes una tarjeta credito "+cardColor, HttpStatus.FORBIDDEN);
        }

        String cardNumber = getRandomStringCard();
        int cvv = getRandomNumber(100, 999);

        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime thruDate = fromDate.plusYears(5);

        Card cards2 = new Card(clientCurrent.getFirstName() + " " + clientCurrent.getLastName(),0,CardType.CREDITO, cardColor, cardNumber, cvv, fromDate, thruDate, clientCurrent,true);
        cardRepository.save(cards2);
        return new ResponseEntity<>("Has creado una tarjeta con exito", HttpStatus.CREATED);
    }

    @PatchMapping("card")
    public ResponseEntity<Object> eliminateCard(@RequestParam String cardNumber){

        Card card = cardRepository.findByNumber(cardNumber);
        card.setActive(false);
        cardRepository.save(card);

        return  new ResponseEntity<>("Se ha eliminado la tarjeta exitosamente", HttpStatus.ACCEPTED);
    }

}





