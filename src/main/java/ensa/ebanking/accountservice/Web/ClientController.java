package ensa.ebanking.accountservice.Web;


import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Entities.Client;
import ensa.ebanking.accountservice.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/account/")
@RestController
class ClientController {

    private ClientService accountService;

    @Autowired
    public void setAccountService(ClientService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/hello-world")
    String test() {
        return "Hello World!";
    }

    @PostMapping("/register")
    public void registerClient(@RequestBody ClientProfileDTO cpdto) {
        accountService.registerClient(cpdto);
    }

}
