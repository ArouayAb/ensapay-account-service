package ensa.ebanking.accountservice.Entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class AgentProfile extends Profile{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cin_url;
    private String cin;
    private Date birthdate;
    private String address;
    private String patenteNumber;
    private String commerceRegisterImm;

    public AgentProfile() {
    }

    @OneToMany(mappedBy = "agentProfile")
    private List<Attachment> attachmentList;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCin_url() {
        return cin_url;
    }

    public void setCin_url(String cin_url) {
        this.cin_url = cin_url;
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
