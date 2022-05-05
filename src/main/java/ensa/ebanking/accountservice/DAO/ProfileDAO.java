package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileDAO extends JpaRepository<Profile, Long> {
}
