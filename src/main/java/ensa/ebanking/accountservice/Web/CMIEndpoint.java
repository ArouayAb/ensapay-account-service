package ensa.ebanking.accountservice.Web;

import ensa.ebanking.accountservice.Services.CMIService;
import ensa.ebanking.accountservice.soap.request.BankAccountRequest;
import ensa.ebanking.accountservice.soap.request.BankAccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CMIEndpoint {
    private static final String NAMESPACE="http://ebanking.ensa/accountservice/Soap/Request";

    @Autowired
    private CMIService cmiService;

    @PayloadRoot(namespace = NAMESPACE, localPart = "BankAccountRequest")
    @ResponsePayload
    public BankAccountResponse createBankAccount(@RequestPayload BankAccountRequest bankAccountReq) {
        return this.cmiService.createBankAccount(bankAccountReq);
    }
}
