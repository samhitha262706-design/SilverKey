package com.silverkey.role;

import com.silverkey.exception.BusinessException;
import com.silverkey.permission.Permission;
import com.silverkey.permission.PermissionRepository;
import jakarta.ws.rs.core.Response;

import java.util.UUID;
import java.util.List;

public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RolePermissionService(
            RolePermissionRepository rolePermissionRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public void assignPermission(
            UUID roleId,
            UUID permissionId
    ) {

        // 1. Check role exists
        roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Role not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        // 2. Check permission exists
        permissionRepository.findById(permissionId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Permission not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        // 3. Prevent duplicate assignment
        if (rolePermissionRepository.exists(roleId, permissionId)) {
            throw new BusinessException(
                    "Permission is already assigned to this role.",
                    Response.Status.CONFLICT
            );
        }

        // 4. Create relationship
        rolePermissionRepository.assignPermission(
                roleId,
                permissionId
        );
    }
    public List<Permission> getPermissionsForRole(UUID roleId) {

        roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Role not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        List<UUID> permissionIds =
                rolePermissionRepository.findPermissionIdsByRole(roleId);

        return permissionIds.stream()
                .map(permissionId ->
                        permissionRepository.findById(permissionId)
                                .orElseThrow(() ->
                                        new BusinessException(
                                                "Permission not found.",
                                                Response.Status.INTERNAL_SERVER_ERROR
                                        )
                                )
                )
                .toList();
    }
    public void removePermission(
            UUID roleId,
            UUID permissionId
    ) {

        roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Role not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        permissionRepository.findById(permissionId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Permission not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        if (!rolePermissionRepository.exists(roleId, permissionId)) {
            throw new BusinessException(
                    "Permission is not assigned to this role.",
                    Response.Status.NOT_FOUND
            );
        }

        rolePermissionRepository.removePermission(
                roleId,
                permissionId
        );
    }
}