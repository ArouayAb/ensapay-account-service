package ensa.ebanking.accountservice.Web;


import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/account/")
@RestController
class AccountController {

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
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
