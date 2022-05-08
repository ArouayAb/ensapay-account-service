package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.AgentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface AgentProfileDAO extends JpaRepository<AgentProfile, Long> {

}
