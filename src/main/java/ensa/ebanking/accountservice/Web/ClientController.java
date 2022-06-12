package ensa.ebanking.accountservice.Web;



import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

import static org.springframework.http.HttpStatus.FORBIDDEN;

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
