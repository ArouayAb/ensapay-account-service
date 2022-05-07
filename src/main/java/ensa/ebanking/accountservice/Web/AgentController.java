package ensa.ebanking.accountservice.Web;


import ensa.ebanking.accountservice.DTO.AgentProfileDTO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Services.AgentService;
import ensa.ebanking.accountservice.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
