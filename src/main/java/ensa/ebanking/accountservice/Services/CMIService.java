package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.CreanceDAO;
import ensa.ebanking.accountservice.DAO.CreancierDAO;
import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.Entities.Creance;
import ensa.ebanking.accountservice.Entities.Creancier;
import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Enums.CreanceStatus;
import ensa.ebanking.accountservice.Exceptions.CreanceAlreadyPaidException;
import ensa.ebanking.accountservice.Exceptions.NotEnoughBalanceException;
import ensa.ebanking.accountservice.Helpers.BankAccountHelper;
import ensa.ebanking.accountservice.Helpers.MappingHelper;
import ensa.ebanking.accountservice.soap.request.accountbalance.AccountBalanceRequest;
import ensa.ebanking.accountservice.soap.request.accountbalance.AccountBalanceResponse;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationRequest;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationResponse;
import ensa.ebanking.accountservice.soap.request.creanceslist.CreancesListRequest;
import ensa.ebanking.accountservice.soap.request.creanceslist.CreancesListResponse;
import ensa.ebanking.accountservice.soap.request.creancierslist.CreanciersListResponse;
import org.hibernate.engine.spi.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CMIService {

    @Autowired
    BankAccountHelper bankAccountHelper;

    @Autowired
    CreancierDAO creancierDAO;

    @Autowired
    CreanceDAO creanceDAO;

    @Autowired
    MappingHelper mappingHelper;

    @Autowired
    UserDAO userDAO;

    public void payFacture(String phoneNumber, List<Object> creanceCodes) throws IOException {
        for (Object creanceCode: creanceCodes) {
            User user = userDAO.findByPhoneNumber(phoneNumber);
            Creance creance = creanceDAO.findByClientProfile_IdAndCode(user.getClientProfile().getId(), Long.parseLong((String) creanceCode)).get(0);
            Double balance = bankAccountHelper.findClientAccountBalance(phoneNumber);

            if(balance < creance.getAmount()) {
                throw new NotEnoughBalanceException("Client balance is: " + balance + " While required is: " + creance.getAmount());
            }else if(creance.getCreanceStatus() == CreanceStatus.COMPLETED) {
                throw new CreanceAlreadyPaidException("Creance: " + creance.getCode() + " has status COMPLETED");
            }

            bankAccountHelper.updateBankAccountBalance(phoneNumber, creance.getCreancier().getServiceProvider().getPhoneNumber(), creance.getAmount());
            creance.setCreanceStatus(CreanceStatus.COMPLETED);
            creanceDAO.save(creance);
        }
    }

    public AccountCreationResponse createBankAccount(AccountCreationRequest bankAccountReq) {

        try {
            if(bankAccountHelper.findClientAccount(bankAccountReq.getPhoneNumber()) == null) {
                bankAccountHelper.addClientAccountToXml(bankAccountReq.getPhoneNumber(), bankAccountReq.getName(), bankAccountReq.getBalance());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AccountCreationResponse bankAccountRes = new AccountCreationResponse();
        bankAccountRes.setPhoneNumber(bankAccountReq.getPhoneNumber());
        bankAccountRes.setName(bankAccountReq.getName());
        bankAccountRes.setBalance(bankAccountReq.getBalance());
        bankAccountRes.setAccountNumber(String.valueOf(ThreadLocalRandom.current().nextLong(0L, 9999999999L)));
        return bankAccountRes;
    }

    public AccountBalanceResponse consultBankAccount(AccountBalanceRequest consultAccountReq) {

        double balance = 0;
        try {
            if(bankAccountHelper.findClientAccount(consultAccountReq.getPhoneNumber()) != null) {
                balance = bankAccountHelper.findClientAccountBalance(consultAccountReq.getPhoneNumber());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AccountBalanceResponse consultAccountRes = new AccountBalanceResponse();
        consultAccountRes.setBalance(balance);
        return consultAccountRes;
    }

    public CreanciersListResponse getCreanciersList() {
        CreanciersListResponse creanciersListResponse = new CreanciersListResponse();
        List<CreanciersListResponse.Creancier> creanciersResponse = creanciersListResponse.getCreancier();


        List<Creancier> creanciers = creancierDAO.findAll();

        for (Creancier creancier: creanciers) {
            CreanciersListResponse.Creancier creancierResponse = new CreanciersListResponse.Creancier();
            MappingHelper.mapCreancier(creancier, creancierResponse);
            creanciersResponse.add(creancierResponse);
        }

        return creanciersListResponse;
    }

    public CreancesListResponse getCreancesList(CreancesListRequest creancesListReq) {
        CreancesListResponse creancesListRes = new CreancesListResponse();
        List<CreancesListResponse.Creance> creancesRes = creancesListRes.getCreance();
        List<Creance> creances = creanceDAO.findAllByClientProfile_Id(creancesListReq.getProfileId());

        for (Creance creance: creances) {
            CreancesListResponse.Creance creanceRes = new CreancesListResponse.Creance();
            MappingHelper.mapCreance(creance, creanceRes);
            creancesRes.add(creanceRes);
        }

        return creancesListRes;
    }
}
