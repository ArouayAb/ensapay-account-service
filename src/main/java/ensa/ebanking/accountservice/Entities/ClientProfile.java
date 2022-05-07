package ensa.ebanking.accountservice.Entities;

import ensa.ebanking.accountservice.Enums.ProductType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ClientProfile extends Profile{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    public ClientProfile(ProductType productType, String name, String surname, String email) {
        this.productType = productType;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public ClientProfile() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
