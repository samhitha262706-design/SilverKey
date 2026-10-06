package com.silverkey.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AssignRoleRequest {

    @NotNull(message = "Role ID is required")
    private UUID roleId;

    public AssignRoleRequest() {
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }
}