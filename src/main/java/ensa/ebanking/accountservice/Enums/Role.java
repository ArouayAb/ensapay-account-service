package ensa.ebanking.accountservice.Enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_CLIENT("CLIENT"),
    ROLE_AGENT("AGENT");

    public final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
