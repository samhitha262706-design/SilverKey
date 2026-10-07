package com.silverkey.role;

import com.silverkey.exception.BusinessException;
import com.silverkey.tenant.Tenant;
import com.silverkey.tenant.TenantRepository;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class RoleService {

    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;

    public RoleService(
            RoleRepository roleRepository,
            TenantRepository tenantRepository
    ) {
        this.roleRepository = roleRepository;
        this.tenantRepository = tenantRepository;
    }

    public Role createRole(UUID tenantId, String name) {

        // 1. Check whether tenant exists
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Tenant not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        // 2. Check whether role already exists in this tenant
        if (roleRepository
                .findByTenantAndName(tenant.getId(), name)
                .isPresent()) {

            throw new BusinessException(
                    "Role already exists in this tenant.",
                    Response.Status.CONFLICT
            );
        }

        // 3. Generate role ID
        UUID roleId = UUID.randomUUID();

        // 4. Create role
        roleRepository.create(
                roleId,
                tenant.getId(),
                name
        );

        // 5. Return created role
        return roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Failed to create role.",
                                Response.Status.INTERNAL_SERVER_ERROR
                        )
                );
    }
}