package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProfileDAO extends JpaRepository<Profile, Long> {
}
