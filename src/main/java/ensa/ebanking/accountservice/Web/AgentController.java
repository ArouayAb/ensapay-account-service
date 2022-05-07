package ensa.ebanking.accountservice.Web;


import ensa.ebanking.accountservice.Entities.Profile;
import ensa.ebanking.accountservice.Services.AgentService;
import ensa.ebanking.accountservice.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AgentController {
    private AgentService agentService;


    public AgentController(AgentService agentService){
        this.agentService=agentService;
    }

    @PostMapping("/active-Account")
    public Profile activation(@RequestBody Profile profile){
        return agentService.validAccount(profile.getId());
    }
    @GetMapping("/inactive-Accounts")
    @ResponseBody
    public List<Profile> listOfInactiveAccounts(){
        return agentService.InvalideAccounts();
    }
}
