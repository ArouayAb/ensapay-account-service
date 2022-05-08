package ensa.ebanking.accountservice.DTO;

import ensa.ebanking.accountservice.Enums.ProductType;

public class ClientProfileDTO {
    private ProductType productType;
    private String name;
    private String surname;
    private String phone;
    private String email;

    public ClientProfileDTO(ProductType productType, String name, String surname, String phone, String email) {
        this.productType = productType;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
    }

    public ClientProfileDTO() {
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


}
