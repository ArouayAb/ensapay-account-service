package ensa.ebanking.accountservice.Enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CLIENT("ROLE_CLIENT"),
    AGENT("ROLE_AGENT"),

    ADMIN("ROLE_ADMIN");

    public final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
