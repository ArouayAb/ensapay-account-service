package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    User findByPhoneNumber(String phoneNumber);
    List<User> findByClientProfile(ClientProfile clientProfile);
    void deleteClientById(Long id);


}
