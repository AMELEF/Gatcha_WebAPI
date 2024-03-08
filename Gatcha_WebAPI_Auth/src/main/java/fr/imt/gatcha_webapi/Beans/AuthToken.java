package fr.imt.gatcha_webapi.Beans;

import java.time.Instant;
import java.util.Date;

public class AuthToken {
    private String token;
    private Instant expirationDate;

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isExpired() {
        return this.expirationDate.compareTo(Instant.now()) < 0;
    }
}
