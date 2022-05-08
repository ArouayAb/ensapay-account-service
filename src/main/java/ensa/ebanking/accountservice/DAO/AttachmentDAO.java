package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface AttachmentDAO extends JpaRepository<Attachment, Long> {
}
