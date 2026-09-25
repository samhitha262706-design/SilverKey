package com.silverkey.organization;

import com.silverkey.exception.BusinessException;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization createOrganization(String name) {

        // 1. Check if organization already exists
        if (organizationRepository.findByName(name).isPresent()) {
            throw new BusinessException(
                    "Organization already exists.",
                    Response.Status.CONFLICT
            );
        }

        // 2. Generate a unique ID
        UUID id = UUID.randomUUID();

        // 3. Save organization
        organizationRepository.create(id, name);

        // 4. Return the created organization
        return organizationRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "Failed to create organization.",
                                Response.Status.INTERNAL_SERVER_ERROR
                        )
                );
    }
}
