package ensa.ebanking.accountservice.Entities;

import ensa.ebanking.accountservice.Enums.Role;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 10 ,max = 10)
    private String phoneNumber;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    private AgentProfile agentProfile;

    @OneToOne
    private ClientProfile clientProfile;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User() {
    }

    public AgentProfile getAgentProfile() {
        return agentProfile;
    }

    public void setAgentProfile(AgentProfile agentProfile) {
        this.agentProfile = agentProfile;
    }

    public ClientProfile getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }

    public User(String phoneNumber, String password, Profile profile) {
        this.phoneNumber = phoneNumber;
        this.password = password;

        if(profile instanceof ClientProfile) {
            this.clientProfile = (ClientProfile) profile;
            this.role = Role.CLIENT;
        }
        else if(profile instanceof AgentProfile){
            this.agentProfile = (AgentProfile) profile;
            this.role = Role.AGENT;
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

}
