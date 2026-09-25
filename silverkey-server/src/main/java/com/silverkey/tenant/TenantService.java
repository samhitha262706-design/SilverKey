package com.silverkey.tenant;

import com.silverkey.exception.BusinessException;
import com.silverkey.organization.Organization;
import com.silverkey.organization.OrganizationRepository;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class TenantService {

    private final TenantRepository tenantRepository;
    private final OrganizationRepository organizationRepository;

    public TenantService(
            TenantRepository tenantRepository,
            OrganizationRepository organizationRepository
    ) {
        this.tenantRepository = tenantRepository;
        this.organizationRepository = organizationRepository;
    }

    public Tenant createTenant(UUID organizationId, String name) {

        // 1. Check whether organization exists
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Organization not found.",
                                Response.Status.NOT_FOUND
                        )
                );

        // 2. Check whether tenant already exists in this organization
        if (tenantRepository
                .findByOrganizationAndName(organization.getId(), name)
                .isPresent()) {

            throw new BusinessException(
                    "Tenant already exists in this organization.",
                    Response.Status.CONFLICT
            );
        }

        // 3. Generate tenant ID
        UUID tenantId = UUID.randomUUID();

        // 4. Create tenant
        tenantRepository.create(
                tenantId,
                organization.getId(),
                name
        );

        // 5. Return created tenant
        return tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Failed to create tenant.",
                                Response.Status.INTERNAL_SERVER_ERROR
                        )
                );
    }
}