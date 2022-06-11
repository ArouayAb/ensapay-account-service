package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.Creance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreanceDAO extends JpaRepository<Creance, Long> {
    List<Creance> findAllByClientProfile_Id(Long profileId);
    List<Creance> findByClientProfile_IdAndCreancier_Code(Long profileId, Long creancierCode);

}
