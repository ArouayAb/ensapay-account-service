package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.Profile;
import ensa.ebanking.accountservice.Enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileDAO extends JpaRepository<Profile, Long> {
    List<Profile> findProfileByProfileStatus(ProfileStatus profileStatus);
    Profile findProfileById(Long id);

}
