package ensa.ebanking.accountservice.Enums;

public enum ProductType {
    HSSAB1("200"), // hssab1 - Plafond 200 DH
    HSSAB2("5000"), // hssab2 - Plafond 5000 DH
    HSSAB3("20000"); // hssab3 - Plafond 20000 DH

    public final String plafond;
    ProductType(String plafond) {
        this.plafond = plafond;
    }
}
