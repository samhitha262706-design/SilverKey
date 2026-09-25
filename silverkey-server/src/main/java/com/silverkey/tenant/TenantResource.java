package com.silverkey.tenant;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tenants")
@Consumes(MediaType.APPLICATION_JSON)
public class TenantResource {

    private final TenantService tenantService;

    public TenantResource(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @POST
    public Response createTenant(CreateTenantRequest request) {

        Tenant tenant = tenantService.createTenant(
                request.getOrganizationId(),
                request.getName()
        );

        TenantResponse response = new TenantResponse(
                tenant.getId(),
                tenant.getOrganizationId(),
                tenant.getName(),
                tenant.getCreatedAt()
        );

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }
}