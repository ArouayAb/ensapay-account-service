package ensa.ebanking.accountservice.Web;

import ensa.ebanking.accountservice.DTO.AgentProfileDTO;

import ensa.ebanking.accountservice.Entities.ClientProfile;

import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

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

        String uploadDir="./src/main/resources/cin/"+apdto.getCin();

        Path uploadPath= Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        try {
            InputStream inputStreamRecto=cinRecto.getInputStream();
            InputStream inputStreamVerso=cinVerso.getInputStream();
            Path filePathRecto=uploadPath.resolve(fileNameRecto);
            Path filePathVerso=uploadPath.resolve(fileNameVerso);
            Files.copy(inputStreamRecto, filePathRecto, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(inputStreamVerso, filePathVerso, StandardCopyOption.REPLACE_EXISTING);
        } catch(Exception e) {
            e.printStackTrace();
        }

        agentService.registerAgent(apdto);
    }

    @RequestMapping(value = "/activate-account", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<User> activate(@RequestBody String json){
        try{
            return ResponseEntity.ok(agentService.validAccount(json));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = "/reject-account", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<User> reject(@RequestBody String json){
        try{
            return ResponseEntity.ok(agentService.rejectAccount(json));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/inactive-accounts")
    @ResponseBody
    public List<ClientProfile> listOfInactiveAccounts(){
        return agentService.invalideAccounts();
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody String json, Principal principal) {
        agentService.changePassword(json, principal.getName());
    }

}
