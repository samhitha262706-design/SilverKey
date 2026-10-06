package com.silverkey.security;

import com.silverkey.permission.PermissionRepository;
import com.silverkey.role.RolePermissionRepository;
import com.silverkey.user.UserRoleRepository;

import java.util.List;
import java.util.UUID;

public class PermissionChecker {

    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;

    public PermissionChecker(
            UserRoleRepository userRoleRepository,
            RolePermissionRepository rolePermissionRepository,
            PermissionRepository permissionRepository
    ) {
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean hasPermission(
            UUID userId,
            String permissionName
    ) {

        List<UUID> roleIds =
                userRoleRepository.findRoleIdsByUser(userId);

        for (UUID roleId : roleIds) {

            List<UUID> permissionIds =
                    rolePermissionRepository.findPermissionIdsByRole(roleId);

            for (UUID permissionId : permissionIds) {

                var permission =
                        permissionRepository.findById(permissionId);

                if (permission.isPresent()
                        && permission.get()
                        .getName()
                        .equals(permissionName)) {

                    return true;
                }
            }
        }

        return false;
    }
}