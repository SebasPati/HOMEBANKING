package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientLoansRepository extends JpaRepository<ClientLoan,Long> {


}
