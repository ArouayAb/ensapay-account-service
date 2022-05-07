package ensa.ebanking.accountservice.DAO;


import ensa.ebanking.accountservice.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ClientDAO extends JpaRepository<Client, Long> {
    Client findByPhoneNumber(String phoneNumber);

    void deleteClientByProfileId(Long id);
}
