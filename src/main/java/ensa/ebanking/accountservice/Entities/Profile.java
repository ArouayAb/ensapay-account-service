package ensa.ebanking.accountservice.Entities;

import ensa.ebanking.accountservice.Enums.ProductType;
import ensa.ebanking.accountservice.Enums.ProfileStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @Email
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus=ProfileStatus.INACTIVE;


    public Profile() {
    }

    public Profile(ProductType productType, String name, String surname, String email) {
        this.productType = productType;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public ProfileStatus getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
