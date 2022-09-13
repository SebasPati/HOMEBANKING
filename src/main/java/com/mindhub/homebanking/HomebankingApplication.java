package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mindhub.homebanking.models.TransactionType.CREDITO;
import static com.mindhub.homebanking.models.TransactionType.DEBITO;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository repository,
									  AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoansRepository clientLoansRepository,CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers2
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("1234"));
			repository.save(client1);

			Client client2 = new Client("Sebastian", "Patiño", "admin", passwordEncoder.encode("5678"));
			repository.save(client2);


			LocalDateTime creationDate = LocalDateTime.now();
			LocalDateTime tomorrow = creationDate.plusDays(1);
			LocalDateTime fromDate = creationDate.minusDays(2);
			LocalDateTime truDate= creationDate.plusYears(5);



			Account account1 = new Account(AccountType.AHORROS,"VIN0001", creationDate, 5000, client1,true);
			Account account2 = new Account(AccountType.CORRIENTE,"VIN0002", tomorrow, 7000, client1,true);

			accountRepository.save(account1);
			accountRepository.save(account2);

			Card card1= new Card(client1.getFirstName()+" "+ client1.getLastName(), account1.getBalance(), CardType.DEBITO,CardColor.TITANIUM,"5356-5573-5578-5678",344,fromDate,truDate,client1,account1,true);
			Card card2= new Card(client1.getFirstName()+" "+client1.getLastName(),0,CardType.CREDITO,CardColor.SILVER,"4563-3489-8790-4690",675,fromDate,truDate.minusYears(6),client1,true);
			cardRepository.save(card1);
			cardRepository.save(card2);

			Account account3 = new Account(AccountType.AHORROS,"VIN0003", creationDate, 8000, client2,true);
			Account account4 = new Account(AccountType.AHORROS,"VIN0004", creationDate, 9000, client2,true);
			Account account5 = new Account(AccountType.CORRIENTE,"VIN0005", tomorrow, 12000, client2,true);

			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);

			//Transaction transaction1 = new Transaction(DEBITO,-200.0,"compra", LocalDateTime.now(),account1);
			//transactionRepository.save(transaction1);
			//transactionRepository.save(transaction2);
			//Transaction transaction3 = new Transaction(DEBITO,-300.2,"compra",LocalDateTime.now(),account2);
			//Transaction transaction4 = new Transaction(CREDITO,500.0,"venta",LocalDateTime.now(),account2);
			//transactionRepository.save(transaction3);
			//transactionRepository.save(transaction4);

			//Transaction transaction5 = new Transaction(DEBITO,-400.0,"compra", LocalDateTime.now(),account1);
			//Transaction transaction6 = new Transaction(CREDITO,420.0,"venta",tomorrow,account1);
			//transactionRepository.save(transaction5);
			//transactionRepository.save(transaction6);
			//Transaction transaction7 = new Transaction(DEBITO,-500.2,"compra",LocalDateTime.now(),account2);
			//Transaction transaction8 = new Transaction(CREDITO,700.0,"venta",LocalDateTime.now(),account2);
			//transactionRepository.save(transaction7);
			//transactionRepository.save(transaction8);

			List<Integer> payments = List.of(60,40,24,12);
			List<Integer> payments2 = List.of(48,24,12);
			List<Integer> payments3 = List.of(80,60,48,24,12);
			Loan loan1= new Loan("hipotecario",500000.0,payments,1.2);
			Loan loan2= new Loan("personal", 200000.0,payments2,1.3);
			Loan loan3= new Loan("automotriz", 100000.0,payments3,1.4);
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000.0,60,client1,loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000.0,12,client1,loan2);
			clientLoansRepository.save(clientLoan1);
			clientLoansRepository.save(clientLoan2);


			Card card3= new Card(client1.getFirstName()+" "+client1.getLastName(),0,CardType.CREDITO,CardColor.GOLD,"4563-3489-8790-1234",873,fromDate,truDate,client2,true);
			cardRepository.save(card3);

		};
	};
}
