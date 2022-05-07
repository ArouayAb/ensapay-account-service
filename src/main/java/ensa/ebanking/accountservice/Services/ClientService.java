package ensa.ebanking.accountservice.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.DAO.ProfileDAO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Utilities.JWTUtil;
import net.bytebuddy.utility.RandomString;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

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

    public void changePassword(HttpServletRequest request, HttpServletResponse response, String json) throws IOException {
        String authorizationHeader = request.getHeader(JWTUtil.AUTH_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(JWTUtil.PREFIX)) {
            String token = authorizationHeader.substring(JWTUtil.PREFIX.length());
            Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            try {
                DecodedJWT decodedJWT = verifier.verify(token);
                String phoneNumber = decodedJWT.getSubject();
                User user = userDAO.findByPhoneNumber(phoneNumber);
                String password = (String) new JSONObject(json).get("password");
                System.out.println(password);
                user.setPassword(this.passwordEncoder.encode(password));
                userDAO.save(user);
            } catch (Exception e) {
                response.sendError(FORBIDDEN.value());
            }
        } else {
            response.sendError(FORBIDDEN.value());
        }
    }
}
