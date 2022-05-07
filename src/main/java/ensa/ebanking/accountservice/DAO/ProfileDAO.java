package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.Profile;
import ensa.ebanking.accountservice.Enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
@Transactional
public interface ProfileDAO extends JpaRepository<Profile, Long> {
    List<Profile> findProfileByProfileStatus(ProfileStatus profileStatus);
    Profile findProfileById(Long id);
    void deleteProfileById(Long id);

}
