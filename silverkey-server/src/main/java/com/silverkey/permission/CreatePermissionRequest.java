package com.silverkey.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePermissionRequest {

    @NotBlank(message = "Permission name is required")
    @Size(
            min = 3,
            max = 100,
            message = "Permission name must be between 3 and 100 characters"
    )
    private String name;

    @Size(
            max = 255,
            message = "Description cannot exceed 255 characters"
    )
    private String description;

    public CreatePermissionRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}