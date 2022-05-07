package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.DAO.ProfileDAO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.User;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private ProfileDAO profileDAO;
    private UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public ClientService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    @Autowired
    public void setClientDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void registerClient(ClientProfileDTO cpdto) {
        String generatedPassword = RandomString.make(10);
        String encodedPassword = this.passwordEncoder.encode(generatedPassword);
        System.out.println(generatedPassword);

        ClientProfile clientProfile = new ClientProfile(cpdto.getProductType(), cpdto.getName(), cpdto.getSurname(), cpdto.getEmail());
        User client = new User(cpdto.getPhone(), encodedPassword, clientProfile);

        profileDAO.save(clientProfile);
        userDAO.save(client);
    }

    public User getClient(String phoneNumber) {
        return userDAO.findByPhoneNumber(phoneNumber);
    }

}
