package ensa.ebanking.accountservice.Entities;

import ensa.ebanking.accountservice.Enums.CreanceStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Creance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @NotNull
    private Date dueDate;

    @Enumerated(EnumType.STRING)
    private CreanceStatus creanceStatus;

    @ManyToOne
    private ClientProfile clientProfile;

    @ManyToOne
    private Creancier creancier;

    public Creance(Long code, ClientProfile clientProfile, Creancier creancier) {
        this.code = code;
        this.clientProfile = clientProfile;
        this.creancier = creancier;
    }

    public Creance() {
    }

    public Long getCode() {
        return code;
    }

    public void setId(Long code) {
        this.code = code;
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }

    public Creancier getCreancier() {
        return creancier;
    }

    public void setCreancier(Creancier creancier) {
        this.creancier = creancier;
    }
}
