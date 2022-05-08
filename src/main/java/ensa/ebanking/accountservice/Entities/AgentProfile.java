package ensa.ebanking.accountservice.Entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
public class AgentProfile extends Profile{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cin_url_verso;
    private String cin_url_recto;
    private String cin;
    private Date birthdate;
    private String address;
    private String patenteNumber;
    private String commerceRegisterImm;


    @OneToMany(mappedBy = "agentProfile", fetch = FetchType.EAGER)
    private List<Attachment> attachmentList;

    public AgentProfile(
            String name,
            String surname,
            String email,
            String cin_url_recto,
            String cin_url_verso,
            String cin,
            Date birthdate,
            String address,
            String patenteNumber,
            String commerceRegisterImm,
            List<Attachment> attachmentList)
    {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.cin_url_recto = cin_url_recto;
        this.cin = cin;
        this.birthdate = birthdate;
        this.address = address;
        this.patenteNumber = patenteNumber;
        this.commerceRegisterImm = commerceRegisterImm;
        this.attachmentList = attachmentList;
        this.cin_url_verso=cin_url_verso;
    }

    public AgentProfile() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCin_url_recto() {
        return cin_url_recto;
    }

    public void setCin_url(String cin_url) {
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

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
