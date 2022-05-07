package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.ClientDAO;
import ensa.ebanking.accountservice.DAO.ProfileDAO;
import ensa.ebanking.accountservice.Entities.Client;
import ensa.ebanking.accountservice.Entities.Profile;
import ensa.ebanking.accountservice.Enums.ProfileStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {
    private ProfileDAO profileDAO;
    private ClientDAO clientDAO;


    public AgentService(ProfileDAO profileDAO, ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
        this.profileDAO = profileDAO;
    }

    public List<Profile> InvalideAccounts() {
        return profileDAO.findProfileByProfileStatus(ProfileStatus.INACTIVE);
    }

    public Profile validAccount(Long id) {
        try {
            Profile profile = profileDAO.findProfileById(id);
            profile.setProfileStatus(ProfileStatus.ACTIVE);
            System.out.println("test");
            return profileDAO.save(profile);

        } catch (Exception e) {
            System.out.println("account not found");
        }
        return null;
    }

    public void rejectAccount(Long id) {
            clientDAO.deleteClientByProfileId(id);
            profileDAO.deleteProfileById(id);
        System.out.println("account with id"+id+"is deleted");


    }
}
