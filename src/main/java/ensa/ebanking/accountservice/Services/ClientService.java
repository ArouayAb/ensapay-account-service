package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.ClientDAO;
import ensa.ebanking.accountservice.DAO.ProfileDAO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Entities.Client;
import ensa.ebanking.accountservice.Entities.Profile;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ClientService implements UserDetailsService {
    private ProfileDAO profileDAO;
    private ClientDAO clientDAO;
    private final PasswordEncoder passwordEncoder;

    public ClientService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    @Autowired
    public void setClientDAO(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Client client = this.clientDAO.findByPhoneNumber(phoneNumber);
        if(client == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(client.getPhoneNumber(), client.getPassword(), authorities);
    }

    public void registerClient(ClientProfileDTO cpdto) {
        String generatedPassword = RandomString.make(10);
        String encodedPassword = this.passwordEncoder.encode(generatedPassword);
        System.out.println(generatedPassword);

        Profile profile = new Profile(cpdto.getProductType(), cpdto.getName(), cpdto.getSurname(), cpdto.getEmail());
        Client client = new Client(cpdto.getPhone(), encodedPassword, profile);

        profileDAO.save(profile);
        clientDAO.save(client);
    }

}
