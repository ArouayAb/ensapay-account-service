package ensa.ebanking.accountservice.Web;

import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Services.CMIService;
import ensa.ebanking.accountservice.soap.request.accountbalance.AccountBalanceRequest;
import ensa.ebanking.accountservice.soap.request.accountbalance.AccountBalanceResponse;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationRequest;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationResponse;
import ensa.ebanking.accountservice.soap.request.creanceslist.CreancesListRequest;
import ensa.ebanking.accountservice.soap.request.creanceslist.CreancesListResponse;
import ensa.ebanking.accountservice.soap.request.creancierslist.CreanciersListResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Endpoint
@RestController
@RequestMapping("cmi-rest")
public class CMIController {
    private static final String NAMESPACE_CREATION="http://www.ebanking.ensa/accountservice/Soap/Request/AccountCreation/";
    private static final String NAMESPACE_CONSULTATION="http://www.ebanking.ensa/accountservice/Soap/Request/AccountBalance/";
    private static final String NAMESPACE_CREANCIERS="http://www.ebanking.ensa/accountservice/Soap/Request/CreanciersList/";
    private static final String NAMESPACE_CREANCES="http://www.ebanking.ensa/accountservice/Soap/Request/CreancesList/";

    @Autowired
    private CMIService cmiService;

    @GetMapping("/hello-world")
    String test() {
        return "Hello World!";
    }

    @PostMapping("/pay-facture")
    ResponseEntity<String> payFacture(@RequestBody String jsonBody) {
        try {
            JSONArray jsonArray = (JSONArray) new JSONObject(jsonBody).get("creanceCodes");
            cmiService.payFacture((String) new JSONObject(jsonBody).get("phoneNumber"), jsonArray.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.status(200).build();
    }

    @PayloadRoot(namespace = NAMESPACE_CREATION, localPart = "AccountCreationRequest")
    @ResponsePayload
    public AccountCreationResponse createBankAccount(@RequestPayload AccountCreationRequest bankAccountReq) {
        AccountCreationResponse accountCreationResponse = cmiService.createBankAccount(bankAccountReq);
        return accountCreationResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_CONSULTATION, localPart = "AccountBalanceRequest")
    @ResponsePayload
    public AccountBalanceResponse consultBankAccount(@RequestPayload AccountBalanceRequest consultAccountReq) {
        return this.cmiService.consultBankAccount(consultAccountReq);
    }

    @PayloadRoot(namespace = NAMESPACE_CREANCIERS, localPart = "CreanciersListRequest")
    @ResponsePayload
    public CreanciersListResponse CreanciersList() {
        return this.cmiService.getCreanciersList();
    }

    @PayloadRoot(namespace = NAMESPACE_CREANCES, localPart = "CreancesListRequest")
    @ResponsePayload
    public CreancesListResponse CreancesList(@RequestPayload CreancesListRequest creancesListReq) {
        return this.cmiService.getCreancesList(creancesListReq);
    }
}
