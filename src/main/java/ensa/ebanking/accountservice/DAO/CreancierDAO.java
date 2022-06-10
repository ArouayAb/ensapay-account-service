package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.Creancier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreancierDAO extends JpaRepository<Creancier, Long> {

}
