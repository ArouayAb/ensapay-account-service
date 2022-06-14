package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.Sms;
import ensa.ebanking.accountservice.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsDao extends JpaRepository<Sms,Long> {
    Sms findSmsByMessageTextAndUser(String code, User user);

}
