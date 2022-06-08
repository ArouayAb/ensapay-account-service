package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.soap.request.BankAccountRequest;
import ensa.ebanking.accountservice.soap.request.BankAccountResponse;
import org.springframework.stereotype.Service;

@Service
public class CMIService {

    public BankAccountResponse createBankAccount(BankAccountRequest bankAccountReq) {
        BankAccountResponse bankAccountRes = new BankAccountResponse();
        bankAccountRes.setId(bankAccountReq.getId());
        bankAccountRes.setName(bankAccountReq.getName());
        bankAccountRes.setBalance(bankAccountRes.getBalance());
        bankAccountRes.setAccountNumber("0123456789");
        return bankAccountRes;
    }
}
