package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.ProfileDAO;
import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.DTO.AgentProfileDTO;
import ensa.ebanking.accountservice.Entities.AgentProfile;
import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.User;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AgentService {
    private final ProfileDAO profileDAO;
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public AgentService(ProfileDAO profileDAO, UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.profileDAO = profileDAO;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerAgent(AgentProfileDTO apdto) {
        String generatedPassword = RandomString.make(10);
        String encodedPassword = this.passwordEncoder.encode(generatedPassword);
        System.out.println(generatedPassword);

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
        User agent = new User(apdto.getPhone(), encodedPassword, agentProfile);

        profileDAO.save(agentProfile);
        userDAO.save(agent);
    }
}
