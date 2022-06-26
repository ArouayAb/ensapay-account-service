package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.AgentProfileDAO;
import ensa.ebanking.accountservice.DAO.ClientProfileDAO;
import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.DTO.AgentProfileDTO;
import ensa.ebanking.accountservice.Entities.AgentProfile;
import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Enums.AccountStatus;
import ensa.ebanking.accountservice.Helpers.BankAccountHelper;
import ensa.ebanking.accountservice.Helpers.EmailHelper;
import net.bytebuddy.utility.RandomString;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {
    private final AgentProfileDAO agentProfileDAO;
    private final ClientProfileDAO clientProfileDAO;
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final EmailHelper emailHelper;
    private final BankAccountHelper bankAccountHelper;


    public AgentService(AgentProfileDAO agentProfileDAO, UserDAO userDAO, PasswordEncoder passwordEncoder, ClientProfileDAO clientProfileDAO, EmailHelper emailHelper, BankAccountHelper bankAccountHelper) {
        this.agentProfileDAO = agentProfileDAO;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.clientProfileDAO = clientProfileDAO;
        this.emailHelper = emailHelper;
        this.bankAccountHelper = bankAccountHelper;
    }

    public void registerAgent(AgentProfileDTO apdto) {
        String generatedPassword = RandomString.make(10);
        String encodedPassword = this.passwordEncoder.encode(generatedPassword);
        System.out.println("Agent password: " + generatedPassword);
        JSONObject email = this.emailHelper.parseJsonFile("EmailDictionary.json");
        this.emailHelper.sendEmail(apdto.getEmail(),
                email.getJSONObject("subject").getString("creation"),
                email.getJSONObject("body").getString("creation") + generatedPassword);
        AgentProfile agentProfile = new AgentProfile(
                apdto.getName(),
                apdto.getSurname(),
                apdto.getEmail(),
                apdto.getCin_url_recto(),
                apdto.getCin_url_verso(),
                apdto.getCin(),
                apdto.getBirthdate(),
                apdto.getAddress(),
                apdto.getPatenteNumber(),
                apdto.getCommerceRegisterImm()
        );
        User agent = new User(apdto.getPhone(), encodedPassword, agentProfile, true);

        agentProfileDAO.save(agentProfile);
        userDAO.save(agent);
    }

    public List<ClientProfile> invalideAccounts () {
        return clientProfileDAO.findClientProfileByAccountStatus(AccountStatus.INACTIVE);
    }

    public User validAccount (String json) throws RuntimeException{
        Long id = new JSONObject(json).getLong("id");
        try {
            JSONObject email = this.emailHelper.parseJsonFile("EmailDictionary.json");
            Optional<ClientProfile> optionalClientProfile = clientProfileDAO.findById(id);
            if (optionalClientProfile.isPresent()) {
                ClientProfile client = optionalClientProfile.get();
                client.setAccountStatus(AccountStatus.ACTIVE);
                this.emailHelper.sendEmail(client.getEmail(),
                        email.getJSONObject("subject").getString("validation"),
                        email.getJSONObject("body").getString("validation"));
                clientProfileDAO.save(client);
                return userDAO.findByClientProfile(client).get(0);
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public User rejectAccount (String json){
        Long id = new JSONObject(json).getLong("id");
        try {
            JSONObject email = this.emailHelper.parseJsonFile("EmailDictionary.json");
            Optional<ClientProfile> optionalClientProfile = clientProfileDAO.findById(id);
            if (optionalClientProfile.isPresent()) {
                ClientProfile client = optionalClientProfile.get();
                client.setAccountStatus(AccountStatus.REJECTED);
                this.emailHelper.sendEmail(client.getEmail(),
                        email.getJSONObject("subject").getString("rejection"),
                        email.getJSONObject("body").getString("rejection"));
                clientProfileDAO.save(client);
                return userDAO.findByClientProfile(client).get(0);
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void changePassword(String json, String phoneNumber) {
        User user = userDAO.findByPhoneNumber(phoneNumber);
        String password = (String) new JSONObject(json).get("password");
        user.setPassword(this.passwordEncoder.encode(password));
        if (user.isFirstLogin()) user.setFirstLogin(false);
        userDAO.save(user);
    }

}
