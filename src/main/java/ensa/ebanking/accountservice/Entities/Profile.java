package ensa.ebanking.accountservice.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
public class Profile implements Serializable {

    @Id
    private Long id;
    private String name;
    private String surname;
    private Date birthdate;
    private String address;
    private String cin;
    private String cinUrl;
    private String email;
    private String patentNum;
    private String commerceRegisterNum;

    @OneToMany(mappedBy = "profile")
    private List<Attachment> attachmentList;

    @OneToOne(mappedBy = "profile")
    private Client client;

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public Profile() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public String getEmail() {
        return email;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getCinUrl() {
        return cinUrl;
    }

    public void setCinUrl(String cinUrl) {
        this.cinUrl = cinUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPatentNum() {
        return patentNum;
    }

    public void setPatentNum(String patentNum) {
        this.patentNum = patentNum;
    }

    public String getCommerceRegisterNum() {
        return commerceRegisterNum;
    }

    public void setCommerceRegisterNum(String commerceRegisterNum) {
        this.commerceRegisterNum = commerceRegisterNum;
    }
}
