package ensa.ebanking.accountservice.Web;

import ensa.ebanking.accountservice.Exceptions.PaymentException;
import ensa.ebanking.accountservice.Services.CMIService;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationRequest;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationResponse;
import ensa.ebanking.accountservice.soap.request.accountinfo.AccountInfoRequest;
import ensa.ebanking.accountservice.soap.request.accountinfo.AccountInfoResponse;
import ensa.ebanking.accountservice.soap.request.creanceslist.CreancesListRequest;
import ensa.ebanking.accountservice.soap.request.creanceslist.CreancesListResponse;
import ensa.ebanking.accountservice.soap.request.creancierslist.CreanciersListResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.IOException;

// This controller can produce both a rest and soap api
@Endpoint
@RestController
@RequestMapping("cmi-rest")
public class CMIController {
    private static final String NAMESPACE_CREATION="http://www.ebanking.ensa/accountservice/Soap/Request/AccountCreation/";
    private static final String NAMESPACE_CONSULTATION="http://www.ebanking.ensa/accountservice/Soap/Request/AccountInfo/";
    private static final String NAMESPACE_CREANCIERS="http://www.ebanking.ensa/accountservice/Soap/Request/CreanciersList/";
    private static final String NAMESPACE_CREANCES="http://www.ebanking.ensa/accountservice/Soap/Request/CreancesList/";

    @Autowired
    private CMIService cmiService;

    @PostMapping("/pay-facture")
    ResponseEntity<String> payFacture(@RequestBody String jsonBody) {
        try {
            JSONArray jsonArray = (JSONArray) new JSONObject(jsonBody).get("creanceCodes");
            cmiService.payFacture((String) new JSONObject(jsonBody).get("phoneNumber"), jsonArray.toList());
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof PaymentException)
                return ResponseEntity.status(((PaymentException) e).errorCode()).build();
            else {
                return ResponseEntity.status(500).build();
            }
        }
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/pay-donation")
    ResponseEntity<String> payDonation(@RequestBody String jsonBody) {
        try {
            cmiService.payDonation(
                    (String) new JSONObject(jsonBody).get("phoneNumber"),
                    (String) new JSONObject(jsonBody).get("creancierCode"),
                    (String) new JSONObject(jsonBody).get("amount"));
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof PaymentException)
                return ResponseEntity.status(((PaymentException) e).errorCode()).build();
            else {
                return ResponseEntity.status(500).build();
            }
        }
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/pay-recharge")
    ResponseEntity<String> payRecharge(@RequestBody String jsonBody) {
        try {
            cmiService.payRecharge(
                    (String) new JSONObject(jsonBody).get("phoneNumber"),
                    (String) new JSONObject(jsonBody).get("creancierCode"),
                    (String) new JSONObject(jsonBody).get("amount"));
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof PaymentException)
                return ResponseEntity.status(((PaymentException) e).errorCode()).build();
            else {
                return ResponseEntity.status(500).build();
            }
        }
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/create-bank-account")
    ResponseEntity<?> createBankAccount(@RequestBody String json) {
        try {
            cmiService.createBankAccount(json);
            return ResponseEntity.status(200).build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/consult-balance")
    ResponseEntity<Double> consultBalance(@RequestBody String json) {
        try {
            return ResponseEntity.ok(cmiService.consultBankAccount(json));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

//    @PayloadRoot(namespace = NAMESPACE_CREATION, localPart = "AccountCreationRequest")
//    @ResponsePayload
//    public AccountCreationResponse createBankAccount(@RequestPayload AccountCreationRequest bankAccountReq) {
//        try {
//            return cmiService.createBankAccount(bankAccountReq);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @PayloadRoot(namespace = NAMESPACE_CONSULTATION, localPart = "AccountInfoRequest")
//    @ResponsePayload
//    public AccountInfoResponse consultBankAccount(@RequestPayload AccountInfoRequest consultAccountReq) {
//        return this.cmiService.consultBankAccount(consultAccountReq);
//    }

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
