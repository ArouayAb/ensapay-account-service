package ensa.ebanking.accountservice.DAO;


import ensa.ebanking.accountservice.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDAO extends JpaRepository<Client, Long> {
    Client findByPhoneNumber(String phoneNumber);
}
