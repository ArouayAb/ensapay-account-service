package ensa.ebanking.accountservice.Services;


import ensa.ebanking.accountservice.DAO.AdminDAO;
import ensa.ebanking.accountservice.DAO.ClientProfileDAO;
import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.DTO.AdminProfileDTO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Entities.AdminProfile;
import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.User;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private AdminDAO adminDTO;
    private UserDAO userDAO;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public void setAdminDTO(AdminDAO adminDTO) {
        this.adminDTO = adminDTO;
    }
    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AdminService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void registerAdmin(AdminProfileDTO admdto,String pass) {
        String encodedPassword = this.passwordEncoder.encode(pass);
        System.out.println("Client password: " + encodedPassword);

        AdminProfile adminProfile = new AdminProfile(admdto.getSurname(),admdto.getPhone());

        User client = new User(admdto.getPhone(), encodedPassword, adminProfile, true);

        adminDTO.save(adminProfile);
        userDAO.save(client);
    }
}
