package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderDAO extends JpaRepository<ServiceProvider, Long> {

}
