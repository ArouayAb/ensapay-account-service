package ensa.ebanking.accountservice.Services;

import ensa.ebanking.accountservice.DAO.*;
import ensa.ebanking.accountservice.Entities.*;
import ensa.ebanking.accountservice.Enums.CreanceStatus;
import ensa.ebanking.accountservice.Enums.CreancierCategory;
import ensa.ebanking.accountservice.Enums.ValidRecharge;
import ensa.ebanking.accountservice.Exceptions.*;
import ensa.ebanking.accountservice.Helpers.BankAccountHelper;
import ensa.ebanking.accountservice.Helpers.MappingHelper;
import ensa.ebanking.accountservice.soap.request.accountinfo.AccountInfoRequest;
import ensa.ebanking.accountservice.soap.request.accountinfo.AccountInfoResponse;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationRequest;
import ensa.ebanking.accountservice.soap.request.accountcreation.AccountCreationResponse;
import ensa.ebanking.accountservice.soap.request.creanceslist.CreancesListRequest;
import ensa.ebanking.accountservice.soap.request.creanceslist.CreancesListResponse;
import ensa.ebanking.accountservice.soap.request.creancierslist.CreanciersListResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    ClientProfileDAO clientProfileDAO;

    @Autowired
    Environment env;

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
        creance.setDate(LocalDateTime.now());

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

//    public void createBankAccount(String json) throws IOException{
//        JSONObject jsonObj = new JSONObject(json);
//        Long id = Long.parseLong(jsonObj.getString("id"));
//        Optional<ClientProfile> optClient = clientProfileDAO.findById(id);
//        if (optClient.isPresent()) {
//            ClientProfile client = optClient.get();
//            List<User> users = userDAO.findByClientProfile(client);
//            if (users.size() > 0 ) {
//                User user = users.get(0);
//                bankAccountHelper.addClientAccountToXml(user.getPhoneNumber(),
//                        client.getName(),
//                        Double.parseDouble(client.getProductType().plafond));
//            } else {
//                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//    }

    public void createBankAccount(String json) throws IOException {
        JSONObject jsonObj = new JSONObject(json);
        String accountNumber;
        try {
            bankAccountHelper.findClientAccount(jsonObj.getString("phoneNumber"));
        } catch (BankAccountNotFoundException be) {
            accountNumber = bankAccountHelper.addClientAccountToXml(jsonObj.getString("phoneNumber"),
                    jsonObj.getString("name"),
                    500.0D);
            if(accountNumber == null) {
                throw new BankAccountNotFoundException("Account number not found when creating");
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        throw new BankAccountAlreadyExistException("Bank account with this phone number already exist");
    }

    public Double consultBankAccount(String json) {
        JSONObject jsonObj = new JSONObject(json);
        Double balance;
        String accountNumber;
        try {
            balance = bankAccountHelper.findClientAccountBalance(jsonObj.getString("phoneNumber"));
            accountNumber = bankAccountHelper.findClientAccountAccountNumber(jsonObj.getString("phoneNumber"));
            if(accountNumber == null || balance == null) {
                throw new BankAccountNotFoundException("Account info not found when consulting");
            }
            return balance;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
                env.getProperty("iam-logo-url"),
                "Maroc Telecom",
                "IAM",
                "0698712345"));
        serviceProviderDAO.save(new ServiceProvider(
                2L,
                env.getProperty("inwi-logo-url"),
                "Inwi",
                "INWI",
                "0615354856"));

        serviceProviderDAO.save(new ServiceProvider(
                3L,
                env.getProperty("orange-logo-url"),
                "Orange",
                "ORANGE",
                "0623487634"));

        serviceProviderDAO.save(new ServiceProvider(
                4L,
                env.getProperty("radeema-logo-url"),
                "Régie Autonome de Distribution d'Eau et d'Electricité de MArrakech",
                "RADEEMA",
                "0698524763"));

        serviceProviderDAO.save(new ServiceProvider(
                5L,
                env.getProperty("alcs-logo-url"),
                "Association de Lutte Contre le SIDA",
                "ALCS",
                "0624156895"));

        serviceProviderDAO.save(new ServiceProvider(
                6L,
                env.getProperty("aamh-logo-url"),
                "Association Amal Marocaine des Handicapés",
                "AAMH",
                "0655426853"));

        //Creanciers
        creancierDAO.save(new Creancier(
                1,
                "Mobile",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                2,
                "Fixe",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                3,
                "Internet ADSL",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                4,
                "Fibre Optique",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                5,
                "*1",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                6,
                "*2",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                7,
                "*3",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                8,
                "*6",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                9,
                "Mobile",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                10,
                "Fixe",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                11,
                "Internet ADSL",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                12,
                "Fibre Optique",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                13,
                "*1",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                14,
                "*2",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                15,
                "*3",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                16,
                "*6",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                17,
                "Mobile",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(3L).get()
        ));

        creancierDAO.save(new Creancier(
                18,
                "Fixe",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(3L).get()
        ));

        creancierDAO.save(new Creancier(
                19,
                "Internet ADSL",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(3L).get()
        ));

        creancierDAO.save(new Creancier(
                20,
                "Fibre Optique",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(3L).get()
        ));

        creancierDAO.save(new Creancier(
                21,
                "*1",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(3L).get()
        ));

        creancierDAO.save(new Creancier(
                22,
                "*2",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(3L).get()
        ));

        creancierDAO.save(new Creancier(
                23,
                "*3",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(3L).get()
        ));

        creancierDAO.save(new Creancier(
                24,
                "*6",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(3L).get()
        ));

        creancierDAO.save(new Creancier(
                25,
                "Eau",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(4L).get()
        ));

        creancierDAO.save(new Creancier(
                26,
                "Electricité",
                CreancierCategory.FACTURE,
                serviceProviderDAO.findById(4L).get()
        ));

        creancierDAO.save(new Creancier(
                27,
                "Donation",
                CreancierCategory.DONATION,
                serviceProviderDAO.findById(5L).get()
        ));

        creancierDAO.save(new Creancier(
                28,
                "Donation",
                CreancierCategory.DONATION,
                serviceProviderDAO.findById(6L).get()
        ));

        creancierDAO.save(new Creancier(
                29,
                "NORMAL",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(1L).get()
        ));

        creancierDAO.save(new Creancier(
                30,
                "NORMAL",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(2L).get()
        ));

        creancierDAO.save(new Creancier(
                31,
                "NORMAL",
                CreancierCategory.RECHARGE,
                serviceProviderDAO.findById(3L).get()
        ));
    }
}
