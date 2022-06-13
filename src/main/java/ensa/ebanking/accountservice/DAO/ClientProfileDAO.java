package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Repository
public interface ClientProfileDAO extends JpaRepository<ClientProfile, Long> {

    List<ClientProfile> findClientProfileByAccountStatus(AccountStatus inactive);

    void deleteClientProfileById(long id);
}
