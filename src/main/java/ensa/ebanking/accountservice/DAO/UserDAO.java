package ensa.ebanking.accountservice.DAO;


import ensa.ebanking.accountservice.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    User findByPhoneNumber(String phoneNumber);
}
