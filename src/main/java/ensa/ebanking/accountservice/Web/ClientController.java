package ensa.ebanking.accountservice.Web;


import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("api/account/client")
@RestController
class ClientController {

    private ClientService clientService;

    @Autowired
    public void setAccountService(ClientService accountService) {
        this.clientService = accountService;
    }

    @GetMapping("/hello-world")
    String test() {
        return "Hello World!";
    }

    @PostMapping("/register")
    public void registerClient(@RequestBody ClientProfileDTO cpdto) {
        clientService.registerClient(cpdto);
    }

}
