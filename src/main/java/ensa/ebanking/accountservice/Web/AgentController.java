package ensa.ebanking.accountservice.Web;



import ensa.ebanking.accountservice.DTO.AgentProfileDTO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;

import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.Profile;

import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Services.AgentService;
import ensa.ebanking.accountservice.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("api/account/agent")
@RestController
class AgentController {

    private AgentService agentService;

    @Autowired
    public void setAccountService(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/hello-world")
    String test() {
        return "Hello World!";
    }

    @PostMapping("/register")
    public void registerAgent(@RequestBody AgentProfileDTO apdto) {
        agentService.registerAgent(apdto);
    }

    @PutMapping("/activate-account")
    @ResponseBody
    public ClientProfile activate(@RequestBody ClientProfile clientProfile){return agentService.validAccount(clientProfile);}

    @PutMapping("/reject-account")
    public ClientProfile reject(@RequestBody ClientProfile clientProfile){return agentService.rejectAccount(clientProfile);}

    @GetMapping("/inactive-accounts")
    @ResponseBody
    public List<ClientProfile> listOfInactiveAccounts(){
        return agentService.invalideAccounts();
    }

}
