package com.silverkey.user;

import java.util.UUID;

public class UserResponse {

    private final UUID id;
    private final UUID tenantId;
    private final String username;
    private final String email;

    public UserResponse(
            UUID id,
            UUID tenantId,
            String username,
            String email
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.username = username;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}