package com.silverkey.permission;

import com.silverkey.exception.BusinessException;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission createPermission(
            String name,
            String description
    ) {

        if (permissionRepository.findByName(name).isPresent()) {
            throw new BusinessException(
                    "Permission already exists.",
                    Response.Status.CONFLICT
            );
        }

        UUID permissionId = UUID.randomUUID();

        permissionRepository.create(
                permissionId,
                name,
                description
        );

        return permissionRepository.findById(permissionId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Failed to create permission.",
                                Response.Status.INTERNAL_SERVER_ERROR
                        )
                );
    }
}