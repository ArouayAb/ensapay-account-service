package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AttachmentDAO extends JpaRepository<Attachment, Long> {
}
