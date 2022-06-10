package ensa.ebanking.accountservice.Web;

import ensa.ebanking.accountservice.Services.CMIService;
import ensa.ebanking.accountservice.soap.request.accountbalance.AccountBalanceRequest;
import ensa.ebanking.accountservice.soap.request.accountbalance.AccountBalanceResponse;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationRequest;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationResponse;
import ensa.ebanking.accountservice.soap.request.creancierslist.CreanciersListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CMIEndpoint {
    private static final String NAMESPACE_CREATION="http://www.ebanking.ensa/accountservice/Soap/Request/AccountCreation/";
    private static final String NAMESPACE_CONSULTATION="http://www.ebanking.ensa/accountservice/Soap/Request/AccountBalance/";
    private static final String NAMESPACE_CREANCIERS="http://www.ebanking.ensa/accountservice/Soap/Request/CreanciersList/";

    @Autowired
    private CMIService cmiService;

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
}
