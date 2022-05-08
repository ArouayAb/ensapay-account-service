package ensa.ebanking.accountservice.Web;



import ensa.ebanking.accountservice.DTO.AgentProfileDTO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;

import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.Profile;

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
    public List<ClientProfile> listOfInactiveAccounts(){
        return agentService.invalideAccounts();
    }

}
