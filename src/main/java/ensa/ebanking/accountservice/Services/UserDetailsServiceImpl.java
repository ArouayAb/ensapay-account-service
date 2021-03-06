package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.Entities.CustomUserDetails;
import ensa.ebanking.accountservice.Entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDAO userDAO;

    public UserDetailsServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = this.userDAO.findByPhoneNumber(phoneNumber);
        if(user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(user.getRole().name));
//        return new org.springframework.security.core.userdetails.User(user.getPhoneNumber(), user.getPassword(), authorities);

        return new CustomUserDetails(user);
    }
}
