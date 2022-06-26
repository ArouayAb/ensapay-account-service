package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.ClientProfileDAO;
import ensa.ebanking.accountservice.DAO.CreanceDAO;
import ensa.ebanking.accountservice.DAO.CreancierDAO;
import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.Creance;
import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Enums.CreanceStatus;
import ensa.ebanking.accountservice.Helpers.EmailHelper;
import net.bytebuddy.utility.RandomString;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClientService {
    private ClientProfileDAO profileDAO;
    private UserDAO userDAO;
    private final EmailHelper emailHelper;
    private final PasswordEncoder passwordEncoder;

    public ClientService(PasswordEncoder passwordEncoder, EmailHelper emailHelper) {
        this.passwordEncoder = passwordEncoder;
        this.emailHelper = emailHelper;
    }

    @Autowired
    public void setProfileDAO(ClientProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    @Autowired
    public void setClientDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    private CreanceDAO creanceDAO;

    @Autowired
    private CreancierDAO creancierDAO;

    private void createCreances(Long id) {
        creanceDAO.save(new Creance(
                LocalDateTime.of(2022, 10, 1,12,20,16),
                CreanceStatus.UNPAID,
                profileDAO.findById(id).get(),
                creancierDAO.findById(1L).get(),
                12.53D
        ));
        creanceDAO.save(new Creance(
                LocalDateTime.of(2022, 9, 1,2,15,3),
                CreanceStatus.UNPAID,
                profileDAO.findById(id).get(),
                creancierDAO.findById(10L).get(),
                200D
        ));
    }

    public void registerClient(ClientProfileDTO cpdto) {
        String generatedPassword = RandomString.make(10);
        String encodedPassword = this.passwordEncoder.encode(generatedPassword);
        System.out.println("Client password: " + generatedPassword);

        JSONObject email = this.emailHelper.parseJsonFile("EmailDictionary.json");
        this.emailHelper.sendEmail(cpdto.getEmail(),
                email.getJSONObject("subject").getString("creation"),
                email.getJSONObject("body").getString("creation") + generatedPassword);

        ClientProfile clientProfile = new ClientProfile(cpdto.getProductType(), cpdto.getName(), cpdto.getSurname(), cpdto.getEmail());
        User client = new User(cpdto.getPhone(), encodedPassword, clientProfile, true);

        ClientProfile clientProfile1 = profileDAO.save(clientProfile);
        userDAO.save(client);

        // For testing purposes
        createCreances(clientProfile1.getId());
    }

    public User getClient(String phoneNumber) {
        return userDAO.findByPhoneNumber(phoneNumber);
    }

    public void changePassword(String json, String phoneNumber) {
            User user = userDAO.findByPhoneNumber(phoneNumber);
            String password = (String) new JSONObject(json).get("password");
            user.setPassword(this.passwordEncoder.encode(password));
            if (user.isFirstLogin()) user.setFirstLogin(false);
            userDAO.save(user);
    }

}
