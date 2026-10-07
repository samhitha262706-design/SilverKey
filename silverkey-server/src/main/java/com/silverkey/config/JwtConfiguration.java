package com.silverkey.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class JwtConfiguration {

    @NotBlank
    private String secret;

    private long expirationMinutes = 15;

    @JsonProperty
    public String getSecret() {
        return secret;
    }

    @JsonProperty
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @JsonProperty
    public long getExpirationMinutes() {
        return expirationMinutes;
    }

    @JsonProperty
    public void setExpirationMinutes(long expirationMinutes) {
        this.expirationMinutes = expirationMinutes;
    }
}
