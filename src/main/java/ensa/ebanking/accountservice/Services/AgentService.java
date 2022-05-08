package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.ClientDAO;
import ensa.ebanking.accountservice.DAO.ProfileDAO;
import ensa.ebanking.accountservice.Entities.Client;
import ensa.ebanking.accountservice.Entities.Profile;
import ensa.ebanking.accountservice.Enums.ProfileStatus;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
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

    public Profile validAccount(String json) {
        try {
            int id=(Integer) new JSONObject(json).get("id");
            Profile profile = profileDAO.findProfileById(Long.valueOf(id));
            profile.setProfileStatus(ProfileStatus.ACTIVE);
            return profileDAO.save(profile);

        } catch (Exception e) {
            System.out.println("account not found");
        }
        return null;
    }

    public void rejectAccount(String json) {
            int id=(Integer) new JSONObject(json).get("id");

            clientDAO.deleteClientByProfileId(Long.valueOf(id));
            profileDAO.deleteProfileById(Long.valueOf(id));
        System.out.println("account with id "+id+" is deleted");


    }
}
