package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.Creance;
import ensa.ebanking.accountservice.Enums.CreanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CreanceDAO extends JpaRepository<Creance, Long> {
    List<Creance> findAllByClientProfile_Id(Long profileId);
    List<Creance> findByClientProfile_IdAndCode(Long profileId, Long creancierCode);
    @Transactional
    @Modifying
    @Query("SELECT u FROM Creance u WHERE u.creanceStatus = ?1 ORDER BY ?2 ASC")
    public List<Creance> findPendingCreanceOrderedByDateAsc(CreanceStatus creanceStatus, String indicator);

}
