package ensa.ebanking.accountservice.Web;


import ensa.ebanking.accountservice.Entities.Profile;
import ensa.ebanking.accountservice.Services.AgentService;
import ensa.ebanking.accountservice.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class AgentController {
    private AgentService agentService;


    public AgentController(AgentService agentService){
        this.agentService=agentService;
    }

    @PostMapping("/activate-account")
    @ResponseBody
    public Profile activation(@RequestBody String json){
        return agentService.validAccount(json);
    }
    @PostMapping("/reject-account")

    public void reject(@RequestBody String json){
        System.out.println(json);
         agentService.rejectAccount(json);
    }
    @GetMapping("/inactive-accounts")
    @ResponseBody
    public List<Profile> listOfInactiveAccounts(){
        return agentService.InvalideAccounts();
    }
}
