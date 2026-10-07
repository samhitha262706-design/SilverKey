package com.silverkey.user;

import com.silverkey.exception.BusinessException;
import com.silverkey.role.Role;
import com.silverkey.role.RoleRepository;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleService(
            UserRoleRepository userRoleRepository,
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void assignRole(
            UUID userId,
            UUID roleId
    ) {

        // 1. Check user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        // 2. Check role exists
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Role not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        // 3. Enforce tenant isolation
        if (!user.getTenantId().equals(role.getTenantId())) {
            throw new BusinessException(
                    "User and role must belong to the same tenant.",
                    Response.Status.FORBIDDEN
            );
        }

        // 4. Prevent duplicate assignment
        if (userRoleRepository.exists(userId, roleId)) {
            throw new BusinessException(
                    "Role is already assigned to this user.",
                    Response.Status.CONFLICT
            );
        }

        // 5. Create relationship
        userRoleRepository.assignRole(
                userId,
                roleId
        );
    }
    public List<Role> getRolesForUser(UUID userId) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        List<UUID> roleIds =
                userRoleRepository.findRoleIdsByUser(userId);

        return roleIds.stream()
                .map(roleId ->
                        roleRepository.findById(roleId)
                                .orElseThrow(() ->
                                        new BusinessException(
                                                "Role not found.",
                                                Response.Status.INTERNAL_SERVER_ERROR
                                        )
                                )
                )
                .toList();
    }
    public void removeRole(
            UUID userId,
            UUID roleId
    ) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new BusinessException(
                                "User not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Role not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        if (!userRoleRepository.exists(userId, roleId)) {
            throw new BusinessException(
                    "Role is not assigned to this user.",
                    Response.Status.NOT_FOUND
            );
        }

        userRoleRepository.removeRole(
                userId,
                roleId
        );
    }
}