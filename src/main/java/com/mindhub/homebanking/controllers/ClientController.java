package com.mindhub.homebanking.controllers;



import com.mindhub.homebanking.DTOS.ClientDTO;

import com.mindhub.homebanking.Utils.ClientUtils;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})

@RequestMapping("/api")
public class ClientController {
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClientDTO() {

        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClientDTO(@PathVariable Long id) {
        ClientDTO clientId = new ClientDTO(clientRepository.findById(id).orElse(null));
        return clientId;
    }

    @PutMapping("/clients/current")
    public ResponseEntity<Object> update(Authentication authentication, @RequestParam String email) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (email.isEmpty()) {
            return new ResponseEntity<>("Ingrese por favor todos los campos", HttpStatus.FORBIDDEN);
        }

        if (!email.contains("@")) {
            return new ResponseEntity<>("Ingrese un email valido", HttpStatus.FORBIDDEN);
        }

        List<String> emails = clientRepository.findAll().stream().map(cliente -> client.getEmail()).collect(Collectors.toList());

        if (emails.contains(email)) {
            return new ResponseEntity<>("Este email ya se encuentra registrado", HttpStatus.FORBIDDEN);
        }

        client.setEmail(email);
        clientRepository.save(client);

        return new ResponseEntity<>("Datos actualizados correctamente", HttpStatus.ACCEPTED);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName,
                                           @RequestParam String email, @RequestParam String password) {


        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Ingrese por favor todos los campos", HttpStatus.FORBIDDEN);

        }

        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("El email ya se encuentra registrado", HttpStatus.FORBIDDEN);
        }

        List<String> accountNumbers = accountRepository.findAll().stream().map(Account::getNumber).collect(Collectors.toList());

        String numeroCuenta = ClientUtils.getNumber(accountNumbers);

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(client);


        Account account = new Account(AccountType.AHORROS,numeroCuenta, LocalDateTime.now(), 0, client,true);
        accountRepository.save(account);

        return new ResponseEntity<>("Se ha registrado con exito", HttpStatus.CREATED);

    }



    @GetMapping("/clients/current")
    public ClientDTO getAll(Authentication authentication) {
        ClientDTO clientDTO1 = new ClientDTO(clientRepository.findByEmail(authentication.getName()));
        return clientDTO1;
    }

    public int getRandomNumber(int min, int max) {
        return ClientUtils.GetNumber(min, max);
    }

    public String getStringRandomClient() {
        return ClientUtils.getString();
    }
}