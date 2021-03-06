package ensa.ebanking.accountservice.Helpers;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import ensa.ebanking.accountservice.Exceptions.BankAccountNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class BankAccounts {
    @JacksonXmlElementWrapper(useWrapping = false)
    List<BankAccount> bankAccount;

    public List<BankAccount> getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(List<BankAccount> bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BankAccounts() {
    }

    public BankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccount = bankAccounts;
    }
}

@JacksonXmlRootElement(localName = "BankAccount")
class BankAccount {
    @JacksonXmlProperty(isAttribute = true)
    private String phoneNumber;
    private String accountNumber;
    private String name;
    private Double balance;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String id) {
        this.phoneNumber = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public BankAccount() {
    }

    public BankAccount(String phoneNumber, String accountNumber, String name, Double balance) {
        this.phoneNumber = phoneNumber;
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }
}

@Component
public class BankAccountHelper {

    @Value("${bank-accounts.directory}")
    private String accountsDirectory;

    @Value("${bank-accounts.client-file}")
    private String clientAccountsFile;

    @Value("${bank-accounts.service-file}")
    private String serviceAccountsFile;


    public String getClientAccountsFile() {
        return clientAccountsFile;
    }

    public String getServiceAccountsFile() {
        return serviceAccountsFile;
    }

    public BankAccountHelper() {
    }

    public BankAccounts loadBankAccountsFromXml(String accountFile) throws IOException {
        String clientAccountPath = String.valueOf(Paths.get(accountsDirectory, accountFile));
        String absoluteClientAccountPath = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "external", clientAccountPath));

        InputStream is = Files.newInputStream(Paths.get(absoluteClientAccountPath.toString()));

        BankAccounts bankAccounts = null;
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            XmlMapper xmlMapper = new XmlMapper();
            bankAccounts = xmlMapper.readValue(sb.toString(), BankAccounts.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bankAccounts;
    }

    public String addClientAccountToXml(String phoneNumber, String name, Double balance) throws IOException {
        String clientAccountPath = String.valueOf(Paths.get(accountsDirectory, clientAccountsFile));
        String absoluteClientAccountPath = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "external", clientAccountPath));

        BankAccounts bankAccounts = loadBankAccountsFromXml(clientAccountsFile);

        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(String.valueOf(absoluteClientAccountPath))
        );

        String accountNumber = String.valueOf(ThreadLocalRandom.current().nextLong(0L, 9999999999L));
        BankAccount bankAccount = new BankAccount(
                phoneNumber,
                accountNumber,
                name,
                balance
        );
        List<BankAccount> bankAccountList = bankAccounts.getBankAccount();
        bankAccountList.add(bankAccount);

        bankAccounts.setBankAccount(bankAccountList);

        XmlMapper xmlMapper = new XmlMapper();
        String xml = xmlMapper.writeValueAsString(bankAccounts);
        bufferedWriter.write(xml);
        bufferedWriter.close();

        return accountNumber;
    }

    public BankAccount findClientAccount(String phoneNumber) throws IOException {
        BankAccounts bankAccounts = loadBankAccountsFromXml(clientAccountsFile);
        List<BankAccount> bankAccountList = bankAccounts.getBankAccount();
        for (BankAccount bankAccount: bankAccountList) {
            if(bankAccount.getPhoneNumber().equals(phoneNumber)) {
                return bankAccount;
            }
        }
        throw new BankAccountNotFoundException("Client bank account not found");
    }

    public BankAccount findServiceAccount(String phoneNumber) throws IOException {
        BankAccounts bankAccounts = loadBankAccountsFromXml(serviceAccountsFile);
        List<BankAccount> bankAccountList = bankAccounts.getBankAccount();
        for (BankAccount bankAccount: bankAccountList) {
            if(bankAccount.getPhoneNumber().equals(phoneNumber)) {
                return bankAccount;
            }
        }
        throw new BankAccountNotFoundException("Service bank account not found");
    }

    public Double findClientAccountBalance(String phoneNumber) throws IOException {
        BankAccount bankAccount = findClientAccount(phoneNumber);
        return bankAccount.getBalance();
    }

    public String findClientAccountAccountNumber(String phoneNumber) throws IOException {
        BankAccount bankAccount = findClientAccount(phoneNumber);
        return bankAccount.getAccountNumber();
    }

    public  void feedClientAccount(String phoneNumber, Double amount) throws IOException {

        BankAccount clientBankAccount = findClientAccount(phoneNumber);

        if(clientBankAccount == null) {
            throw new BankAccountNotFoundException("Client bank account not found");
        }

        clientBankAccount.setBalance(clientBankAccount.getBalance() + amount);


        saveClientBankAccount(clientBankAccount);

    }

    public  void feedServiceAccount(String phoneNumber, Double amount) throws IOException {

        BankAccount serviceBankAccount = findServiceAccount(phoneNumber);

        if(serviceBankAccount == null) {
            throw new BankAccountNotFoundException("Service bank account not found");
        }

        serviceBankAccount.setBalance(serviceBankAccount.getBalance() + amount);


        saveServiceBankAccount(serviceBankAccount);

    }

    public void updateBankAccountBalance(String phoneNumber, String targetPhoneNumber, Double amount) throws IOException {
        BankAccount clientBankAccount = findClientAccount(phoneNumber);
        BankAccount serviceBankAccount = findServiceAccount(targetPhoneNumber);

        if(serviceBankAccount == null){
            throw new BankAccountNotFoundException("Service bank account not found");
        }else if(clientBankAccount == null) {
            throw new BankAccountNotFoundException("Client bank account not found");
        }

        clientBankAccount.setBalance(clientBankAccount.getBalance() - amount);
        serviceBankAccount.setBalance(serviceBankAccount.getBalance() + amount);

        saveClientBankAccount(clientBankAccount);
        saveServiceBankAccount(serviceBankAccount);
    }

    public void saveClientBankAccount(BankAccount bankAccountToSave) throws IOException {
        saveBankAccount(bankAccountToSave, clientAccountsFile);
    }

    public void saveServiceBankAccount(BankAccount bankAccountToSave) throws IOException {
        saveBankAccount(bankAccountToSave, serviceAccountsFile);
    }

    private void saveBankAccount(BankAccount bankAccountToSave, String serviceAccountsFile) throws IOException {
        String serviceAccountPath = String.valueOf(Paths.get(accountsDirectory, serviceAccountsFile));
        String absoluteServiceAccountPath = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "external", serviceAccountPath));

        BankAccounts bankAccounts = loadBankAccountsFromXml(serviceAccountsFile);
        List<BankAccount> bankAccountList = bankAccounts.getBankAccount();
        for (BankAccount bankAccount: bankAccountList) {
            if(bankAccount.getPhoneNumber().equals(bankAccountToSave.getPhoneNumber())) {
                bankAccount.setBalance(bankAccountToSave.getBalance());
                bankAccount.setName(bankAccountToSave.getName());
            }
        }

        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(String.valueOf(absoluteServiceAccountPath))
        );
        XmlMapper xmlMapper = new XmlMapper();
        String xml = xmlMapper.writeValueAsString(bankAccounts);
        bufferedWriter.write(xml);
        bufferedWriter.close();
    }
}
