package ensa.ebanking.accountservice.Web;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Services.ClientService;
import ensa.ebanking.accountservice.Utilities.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@CrossOrigin
@RequestMapping("api/account/client")
@RestController
class ClientController {

    private ClientService clientService;

    @Autowired
    public void setAccountService(ClientService accountService) {
        this.clientService = accountService;
    }

    @GetMapping("/currentUser")
    ResponseEntity<User> test(Principal principal) {
        User client = clientService.getClient(principal.getName());
        if (client.isFirstLogin()) return ResponseEntity.status(FORBIDDEN).build();
        return ResponseEntity.ok(client);
    }

    @PostMapping("/register")
    public void registerClient(@RequestBody ClientProfileDTO cpdto) {
        clientService.registerClient(cpdto);
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody String json, Principal principal) {
        clientService.changePassword(json, principal.getName());
    }

}
