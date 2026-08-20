package com.silverkey.user;

import java.util.UUID;

public class UserResponse {

    private final UUID id;
    private final String username;
    private final String email;

    public UserResponse(
            UUID id,
            String username,
            String email
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}