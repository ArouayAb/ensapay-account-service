package ensa.ebanking.accountservice.Batches;

import ensa.ebanking.accountservice.DAO.ClientProfileDAO;
import ensa.ebanking.accountservice.DAO.CreanceDAO;
import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.Entities.ClientProfile;
import ensa.ebanking.accountservice.Entities.Creance;
import ensa.ebanking.accountservice.Entities.Creancier;
import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Enums.CreanceStatus;
import ensa.ebanking.accountservice.Helpers.BankAccountHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BatchService {

    @Autowired
    private  CreanceDAO creanceDAO;
    @Autowired
    private ClientProfileDAO clientProfileDAO;

    @Autowired
    private  UserDAO userDAO;

    @Autowired
    private  BankAccountHelper bankAccountHelper;

    @Scheduled(fixedDelay = 5000)
    public void cancel_duplicated_transaction() {
        // does who have status PENDING
        List<Creance> creances = creanceDAO.findPendingCreanceOrderedByDateAsc(CreanceStatus.PENDING,"date");
        for (int i = 0; i < creances.size(); i++) {
            Creance creance = creances.get(i);
            ClientProfile client = creance.getClientProfile();
            Long clientId = client.getId();

            List<User> users = userDAO.findByClientProfile(client);
            if (users.size() == 0)
                throw new RuntimeException();

            Creancier creancier = creance.getCreancier();
            Long codeCreancier = creancier.getCode();
            LocalDateTime dateTime = creance.getDate();
            int min = dateTime.getMinute();
            Long code = creance.getCode();

            for (int j = i + 1; j < creances.size(); j++) {
                Creance duplicatedCreance = creances.get(j);
                ClientProfile client1 = duplicatedCreance.getClientProfile();
                Long clientId1 = client1.getId();
                Creancier creancier1 = duplicatedCreance.getCreancier();
                Long codeCreancier1 = creancier1.getCode();
                LocalDateTime dateTime1 = duplicatedCreance.getDate();
                Long code1 = duplicatedCreance.getCode();
                int min1 = dateTime1.getMinute();
                if (min1 - min <= 1 && clientId.equals(clientId1) && codeCreancier.equals(codeCreancier1) && creance.getAmount().equals(duplicatedCreance.getAmount())) {

                    try {
                        bankAccountHelper.feedClientAccount(users.get(0).getPhoneNumber(), duplicatedCreance.getAmount());
                        duplicatedCreance.setCreanceStatus(CreanceStatus.CANCELED);
                        creanceDAO.save(duplicatedCreance);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            try {
                bankAccountHelper.feedServiceAccount(creance.getCreancier().getServiceProvider().getPhoneNumber(), creance.getAmount());
                creance.setCreanceStatus(CreanceStatus.COMPLETED);
                creanceDAO.save(creance);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        System.out.println("créances are up to date");

    }



    Double salary =10000D;
    @Scheduled(fixedDelay = 50000)
    public void UpdateSoldByEndOfMonth() {

        List<ClientProfile> clientList=clientProfileDAO.findAll();

        for (int i = 0; i < clientList.size(); i++) {
            ClientProfile client = clientList.get(i);
            List<User> users = userDAO.findByClientProfile(client);
            if (users.size() == 0)
                throw new RuntimeException();
            try {
                bankAccountHelper.feedClientAccount(users.get(0).getPhoneNumber(), salary);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        System.out.println("sold updated");
    }

}
