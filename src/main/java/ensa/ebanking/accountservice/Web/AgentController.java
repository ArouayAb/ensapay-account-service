package ensa.ebanking.accountservice.Web;



import ensa.ebanking.accountservice.DTO.AgentProfileDTO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;

import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.Profile;

import ensa.ebanking.accountservice.Services.AgentService;
import ensa.ebanking.accountservice.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
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
    public void registerAgent(@ModelAttribute AgentProfileDTO apdto ,@RequestParam("cin_recto") MultipartFile cinRecto,@RequestParam("cin_verso") MultipartFile cinVerso) throws IOException {

        String fileNameRecto= StringUtils.cleanPath(cinRecto.getOriginalFilename());
        String fileNameVerso= StringUtils.cleanPath(cinVerso.getOriginalFilename());
        apdto.setCin_url_recto(apdto.getCin()+"/"+fileNameRecto);
        apdto.setCin_url_verso(apdto.getCin()+"/"+fileNameVerso);

        String uploadDir="./cin/"+apdto.getCin();


        Path uploadPath= Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        InputStream inputStreamRecto=cinRecto.getInputStream();
        InputStream inputStreamVerso=cinVerso.getInputStream();
        Path filePathRecto=uploadPath.resolve(fileNameRecto);
        Path filePathVerso=uploadPath.resolve(fileNameVerso);

        Files.copy(inputStreamRecto, filePathRecto, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(inputStreamVerso, filePathVerso, StandardCopyOption.REPLACE_EXISTING);

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
