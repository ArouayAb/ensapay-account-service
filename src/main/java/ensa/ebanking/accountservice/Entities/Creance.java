package ensa.ebanking.accountservice.Entities;

import ensa.ebanking.accountservice.Enums.CreanceStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Creance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private Double amount;

    @Enumerated(EnumType.STRING)
    private CreanceStatus creanceStatus;

    @ManyToOne
    private ClientProfile clientProfile;

    @ManyToOne
    private Creancier creancier;

    /*public Creance(Long code, Date date, CreanceStatus creanceStatus, ClientProfile clientProfile, Creancier creancier, Double amount) {
        this.code = code;
        this.date = date;
        this.creanceStatus = creanceStatus;
        this.clientProfile = clientProfile;
        this.creancier = creancier;
        this.amount = amount;
    }
*/
    public Creance(Long code, LocalDateTime date, CreanceStatus creanceStatus, ClientProfile clientProfile, Creancier creancier, Double amount) {
        this.code = code;
        this.date = date;
        this.creanceStatus = creanceStatus;
        this.clientProfile = clientProfile;
        this.creancier = creancier;
        this.amount = amount;
    }

    public Creance(LocalDateTime date, CreanceStatus creanceStatus, ClientProfile clientProfile, Creancier creancier, Double amount) {
        this.date = date;
        this.creanceStatus = creanceStatus;
        this.clientProfile = clientProfile;
        this.creancier = creancier;
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Creance() {
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public CreanceStatus getCreanceStatus() {
        return creanceStatus;
    }

    public void setCreanceStatus(CreanceStatus creanceStatus) {
        this.creanceStatus = creanceStatus;
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
