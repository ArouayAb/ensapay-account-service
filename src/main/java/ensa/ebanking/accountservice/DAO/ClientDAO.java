package ensa.ebanking.accountservice.DAO;


import ensa.ebanking.accountservice.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDAO extends JpaRepository<Client, Long> {
}
