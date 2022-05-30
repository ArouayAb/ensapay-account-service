package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDAO extends JpaRepository<AdminProfile,Long> {
}
