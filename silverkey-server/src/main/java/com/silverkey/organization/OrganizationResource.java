package com.silverkey.organization;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/organizations")
public class OrganizationResource {

    private final OrganizationService organizationService;

    public OrganizationResource(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @POST
    public Response createOrganization(CreateOrganizationRequest request) {

        Organization organization =
                organizationService.createOrganization(request.getName());

        OrganizationResponse response = new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getCreatedAt()
        );

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }
}