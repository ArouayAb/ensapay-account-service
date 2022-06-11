package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.CreanceDAO;
import ensa.ebanking.accountservice.DAO.CreancierDAO;
import ensa.ebanking.accountservice.DAO.ServiceProviderDAO;
import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.Entities.Creance;
import ensa.ebanking.accountservice.Entities.Creancier;
import ensa.ebanking.accountservice.Entities.ServiceProvider;
import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Enums.CreanceStatus;
import ensa.ebanking.accountservice.Enums.CreancierCategory;
import ensa.ebanking.accountservice.Enums.ValidRecharge;
import ensa.ebanking.accountservice.Exceptions.CreanceAlreadyPaidException;
import ensa.ebanking.accountservice.Exceptions.InvalidRechargeAmountException;
import ensa.ebanking.accountservice.Exceptions.NotEnoughBalanceException;
import ensa.ebanking.accountservice.Exceptions.WrongCreancierCategoryException;
import ensa.ebanking.accountservice.Helpers.BankAccountHelper;
import ensa.ebanking.accountservice.Helpers.MappingHelper;
import ensa.ebanking.accountservice.soap.request.accountbalance.AccountBalanceRequest;
import ensa.ebanking.accountservice.soap.request.accountbalance.AccountBalanceResponse;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationRequest;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationResponse;
import ensa.ebanking.accountservice.soap.request.creanceslist.CreancesListRequest;
import ensa.ebanking.accountservice.soap.request.creanceslist.CreancesListResponse;
import ensa.ebanking.accountservice.soap.request.creancierslist.CreanciersListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CMIService {

    @Autowired
    BankAccountHelper bankAccountHelper;

    @Autowired
    ServiceProviderDAO serviceProviderDAO;

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

            if(creance.getCreancier().getCreancierCategory() != CreancierCategory.FACTURE) {
                throw new WrongCreancierCategoryException("Category expected is different than the provided");
            }else if(creance.getCreanceStatus() == CreanceStatus.COMPLETED) {
                throw new CreanceAlreadyPaidException("Creance: " + creance.getCode() + " has status COMPLETED");
            }else if(balance < creance.getAmount()) {
                throw new NotEnoughBalanceException("Client balance is: " + balance + " While required is: " + creance.getAmount());
            }

            bankAccountHelper.updateBankAccountBalance(phoneNumber, creance.getCreancier().getServiceProvider().getPhoneNumber(), creance.getAmount());
            creance.setCreanceStatus(CreanceStatus.COMPLETED);
            creanceDAO.save(creance);
        }
    }

    public void payDonation(String phoneNumber, String creancierCode, String amount) throws IOException {
        User user = userDAO.findByPhoneNumber(phoneNumber);
        Creancier creancier = creancierDAO.findByCode(Long.parseLong(creancierCode));
        Double balance = bankAccountHelper.findClientAccountBalance(phoneNumber);

        if(creancier.getCreancierCategory() != CreancierCategory.DONATION) {
            throw new WrongCreancierCategoryException("Category expected is different than the provided");
        }else if(balance < Double.parseDouble(amount)) {
            throw new NotEnoughBalanceException("Client balance is: " + balance + " While required is: " + Double.parseDouble(amount));
        }

        addCreance(phoneNumber, amount, user, creancier);
    }

    private void addCreance(String phoneNumber, String amount, User user, Creancier creancier) throws IOException {
        Creance creance = new Creance();
        creance.setAmount(Double.parseDouble(amount));
        creance.setClientProfile(user.getClientProfile());
        creance.setCreancier(creancier);
        creance.setCreanceStatus(CreanceStatus.COMPLETED);
        creance.setDueDate(Date.valueOf(LocalDate.now()));

        bankAccountHelper.updateBankAccountBalance(phoneNumber, creance.getCreancier().getServiceProvider().getPhoneNumber(), creance.getAmount());
        creanceDAO.save(creance);
    }

    public void payRecharge(String phoneNumber, String creancierCode, String amount) throws IOException {
        User user = userDAO.findByPhoneNumber(phoneNumber);
        Creancier creancier = creancierDAO.findByCode(Long.parseLong(creancierCode));
        Double balance = bankAccountHelper.findClientAccountBalance(phoneNumber);

        if(creancier.getCreancierCategory() != CreancierCategory.RECHARGE) {
            throw new WrongCreancierCategoryException("Category expected is different than the provided");
        }else if(balance < Double.parseDouble(amount)) {
            throw new NotEnoughBalanceException("Client balance is: " + balance + " While required is: " + Double.parseDouble(amount));
        }else if(!creancier.isValidAmount(Double.parseDouble(amount))) {
            throw new InvalidRechargeAmountException(
                    "Value entered: " + amount + " Valid amounts are: " +
                            Stream.of(ValidRecharge.values())
                                    .map(ValidRecharge::name)
                                    .collect(Collectors.toList())
            );
        }

        addCreance(phoneNumber, amount, user, creancier);
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

    public void saveStaticDataToDB() {

        // ServiceProviders
        serviceProviderDAO.save(new ServiceProvider(
                1L,
                "url",
                "Maroc Telecom",
                "IAM",
                "0698712345"));
        serviceProviderDAO.save(new ServiceProvider(
                2L,
                "url",
                "INWI",
                "INWI",
                "0623487634"));
        serviceProviderDAO.save(new ServiceProvider(
                3L,
                "url",
                "Association de Lutte Contre le SIDA",
                "ALCS",
                "0687678456"));

        //Creanciers
        creancierDAO.save(new Creancier(
                1,
                "Facture",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                2,
                "Recharge - *1",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                3,
                "Recharge - *2",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                4,
                "Recharge - *3",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                5,
                "Recharge - *6",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                6,
                "Facture",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                7,
                "Recharge - *1",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                8,
                "Recharge - *2",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                9,
                "Recharge - *3",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                10,
                "Recharge - *6",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                11,
                "ALCS - Donation",
                CreancierCategory.DONATION,
                serviceProviderDAO.findById(3L).get()
        ));
    }
}
