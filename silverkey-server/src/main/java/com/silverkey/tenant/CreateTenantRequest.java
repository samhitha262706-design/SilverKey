package com.silverkey.tenant;

import java.util.UUID;

public class CreateTenantRequest {

    private UUID organizationId;
    private String name;

    public CreateTenantRequest() {
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
}