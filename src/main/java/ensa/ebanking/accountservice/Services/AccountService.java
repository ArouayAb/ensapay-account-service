package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.ClientDAO;
import ensa.ebanking.accountservice.DAO.ProfileDAO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Entities.Client;
import ensa.ebanking.accountservice.Entities.Profile;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private ProfileDAO profileDAO;
    private ClientDAO clientDAO;

    @Autowired
    public void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    @Autowired
    public void setClientDAO(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    public void registerClient(ClientProfileDTO cpdto) {
        Profile profile = new Profile(cpdto.getProductType(), cpdto.getName(), cpdto.getSurname(), cpdto.getEmail());
        Client client = new Client(cpdto.getPhone(), new RandomString().make(10),profile);

        profileDAO.save(profile);
        clientDAO.save(client);
    }
}
