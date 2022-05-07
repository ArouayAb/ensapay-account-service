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

    @GetMapping("/activate-account")
    @ResponseBody
    public Profile activation(@RequestParam(name="id") Long id){
        return agentService.validAccount(id);
    }
    @GetMapping("/reject-account")

    public void reject(@RequestParam(name="id") Long id){
         agentService.rejectAccount(id);
    }
    @GetMapping("/inactive-accounts")
    @ResponseBody
    public List<Profile> listOfInactiveAccounts(){
        return agentService.InvalideAccounts();
    }
}
