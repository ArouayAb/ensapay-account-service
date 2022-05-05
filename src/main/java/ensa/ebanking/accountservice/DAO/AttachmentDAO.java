package ensa.ebanking.accountservice.DAO;

import ensa.ebanking.accountservice.Entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentDAO extends JpaRepository<Attachment, Long> {
}
