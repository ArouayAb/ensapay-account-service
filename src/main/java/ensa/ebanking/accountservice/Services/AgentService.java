package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.AgentProfileDAO;
import ensa.ebanking.accountservice.DAO.ClientProfileDAO;
import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.DTO.AgentProfileDTO;
import ensa.ebanking.accountservice.Entities.AgentProfile;
import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.Profile;
import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Enums.AccountStatus;
import net.bytebuddy.utility.RandomString;
import org.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {
    private final AgentProfileDAO agentProfileDAO;
    private final ClientProfileDAO clientProfileDAO;
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public AgentService(AgentProfileDAO agentProfileDAO, UserDAO userDAO, PasswordEncoder passwordEncoder, ClientProfileDAO clientProfileDAO) {
        this.agentProfileDAO = agentProfileDAO;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.clientProfileDAO = clientProfileDAO;
    }

    public void registerAgent(AgentProfileDTO apdto) {
        String generatedPassword = RandomString.make(10);
        String encodedPassword = this.passwordEncoder.encode(generatedPassword);
        System.out.println("Agent password: " + generatedPassword);

        AgentProfile agentProfile = new AgentProfile(
                apdto.getName(),
                apdto.getSurname(),
                apdto.getEmail(),
                apdto.getCinUrl(),
                apdto.getCin(),
                apdto.getBirthdate(),
                apdto.getAddress(),
                apdto.getPatenteNumber(),
                apdto.getCommerceRegisterImm(),
                apdto.getAttachmentList()
        );
        User agent = new User(apdto.getPhone(), encodedPassword, agentProfile, true);

        agentProfileDAO.save(agentProfile);
        userDAO.save(agent);
    }

    public List<ClientProfile> invalideAccounts () {
        return clientProfileDAO.findClientProfileByAccountStatus(AccountStatus.INACTIVE);
    }

    public ClientProfile validAccount (String json){
        try {
            int id = (Integer) new JSONObject(json).get("id");
            ClientProfile profile = clientProfileDAO.findClientProfileById(id);
            profile.setAccountStatus(AccountStatus.ACTIVE);
            return clientProfileDAO.save(profile);

        } catch (Exception e) {
            System.out.println("account not found");
        }
        return null;
    }

    public void rejectAccount (String json){
        int id = (Integer) new JSONObject(json).get("id");

        userDAO.deleteClientById((long) id);
        clientProfileDAO.deleteClientProfileById(id);
        System.out.println("account with id " + id + " is deleted");
    }
}
