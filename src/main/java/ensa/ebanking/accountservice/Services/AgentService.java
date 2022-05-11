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
    private final EmailSenderService emailSenderService;

    public AgentService(AgentProfileDAO agentProfileDAO, UserDAO userDAO, PasswordEncoder passwordEncoder, ClientProfileDAO clientProfileDAO, EmailSenderService emailSenderService) {
        this.agentProfileDAO = agentProfileDAO;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.clientProfileDAO = clientProfileDAO;
        this.emailSenderService = emailSenderService;
    }

    public void registerAgent(AgentProfileDTO apdto) {
        String generatedPassword = RandomString.make(10);
        String encodedPassword = this.passwordEncoder.encode(generatedPassword);
        System.out.println("Agent password: " + generatedPassword);

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

    public ClientProfile validAccount (ClientProfile clientProfile){
        try {
            Optional<ClientProfile> optionalClientProfile = clientProfileDAO.findById(clientProfile.getId());
            if (optionalClientProfile.isPresent()) {
                ClientProfile client = optionalClientProfile.get();
                String email = client.getEmail();
                client.setAccountStatus(AccountStatus.ACTIVE);
                String subject = "Validation de votre compte";
                String body = "Nous sommes ravis de vous informer que votre demande a été validée. Vous pouvez désormais " +
                        "bénéficier des services de ENSAPAY.";
                this.emailSenderService.sendEmail(email, subject, body);
                return clientProfileDAO.save(client);
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public ClientProfile rejectAccount (ClientProfile clientProfile){
        try {
            Optional<ClientProfile> optionalClientProfile = clientProfileDAO.findById(clientProfile.getId());
            if (optionalClientProfile.isPresent()) {
                ClientProfile client = optionalClientProfile.get();
                String email = client.getEmail();
                client.setAccountStatus(AccountStatus.REJECTED);
                String subject = "Validation de votre compte";
                String body = "Nous sommes désolés de vous informer que votre demande a été rejetée. Il semble que certaines " +
                        "de vos informations sont invalides.";
                this.emailSenderService.sendEmail(email, subject, body);
                emailSenderService.sendEmail(email, subject, body);
                System.out.println();
                return clientProfileDAO.save(client);
            } else {
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
