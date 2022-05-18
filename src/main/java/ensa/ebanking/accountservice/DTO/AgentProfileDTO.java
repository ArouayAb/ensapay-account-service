package ensa.ebanking.accountservice.DTO;

import ensa.ebanking.accountservice.Enums.ProductType;

import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.List;

public class AgentProfileDTO {

    private String name;
    private String surname;
    private String phone;
    private String email;
    private String cin_url_recto;
    private String cin_url_verso;
    private String cin;
    private Date birthdate;
    private String address;
    private String patenteNumber;
    private String commerceRegisterImm;

    public AgentProfileDTO(
            String name,
            String surname,
            String phone,
            String email,
            String cin_url_recto,
            String cin_url_verso,
            String cin,
            Date birthdate,
            String address,
            String patenteNumber,
            String commerceRegisterImm)
    {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.cin_url_recto=cin_url_recto;
        this.cin_url_verso=cin_url_verso;
        this.cin = cin;
        this.birthdate = birthdate;
        this.address = address;
        this.patenteNumber = patenteNumber;
        this.commerceRegisterImm = commerceRegisterImm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCin_url_recto() {
        return cin_url_recto;
    }

    public void setCin_url_recto(String cin_url_recto) {
        this.cin_url_recto = cin_url_recto;
    }

    public String getCin_url_verso() {
        return cin_url_verso;
    }

    public void setCin_url_verso(String cin_url_verso) {
        this.cin_url_verso = cin_url_verso;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPatenteNumber() {
        return patenteNumber;
    }

    public void setPatenteNumber(String patenteNumber) {
        this.patenteNumber = patenteNumber;
    }

    public String getCommerceRegisterImm() {
        return commerceRegisterImm;
    }

    public void setCommerceRegisterImm(String commerceRegisterImm) {
        this.commerceRegisterImm = commerceRegisterImm;
    }
}
