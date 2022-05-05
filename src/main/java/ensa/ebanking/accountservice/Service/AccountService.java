package ensa.ebanking.accountservice.Service;

import ensa.ebanking.accountservice.DAO.AttachmentDAO;
import ensa.ebanking.accountservice.DAO.ClientDAO;
import ensa.ebanking.accountservice.DAO.ProfileDAO;
import ensa.ebanking.accountservice.Entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private AttachmentDAO attachmentDAO;
    private ProfileDAO profileDAO;
    private ClientDAO clientDAO;

    @Autowired
    public void setAttachmentDAO(AttachmentDAO attachmentDAO) {
        this.attachmentDAO = attachmentDAO;
    }

    @Autowired
    public void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    @Autowired
    public void setClientDAO(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }
}
