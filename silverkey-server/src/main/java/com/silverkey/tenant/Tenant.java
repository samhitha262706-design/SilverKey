package com.silverkey.tenant;

import java.time.LocalDateTime;
import java.util.UUID;

public class Tenant {

    private UUID id;
    private UUID organizationId;
    private String name;
    private LocalDateTime createdAt;

    public Tenant() {
    }

    public Tenant(
            UUID id,
            UUID organizationId,
            String name,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}