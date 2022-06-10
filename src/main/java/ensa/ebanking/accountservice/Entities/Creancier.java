package ensa.ebanking.accountservice.Entities;

import ensa.ebanking.accountservice.Enums.CreancierCategory;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Creancier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long code;

    @NotEmpty
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CreancierCategory creancierCategory;

    @ManyToOne
    private ServiceProvider serviceProvider;

    public Creancier(long code, String name, CreancierCategory creancierCategory, ServiceProvider serviceProvider) {
        this.code = code;
        this.name = name;
        this.creancierCategory = creancierCategory;
        this.serviceProvider = serviceProvider;
    }

    public Creancier() {
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CreancierCategory getCreancierCategory() {
        return creancierCategory;
    }

    public void setCreancierCategory(CreancierCategory creancierCategory) {
        this.creancierCategory = creancierCategory;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
