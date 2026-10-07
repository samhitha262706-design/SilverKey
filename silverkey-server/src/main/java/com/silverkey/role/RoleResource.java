package com.silverkey.role;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/roles")
@Consumes(MediaType.APPLICATION_JSON)
public class RoleResource {

    private final RoleService roleService;

    public RoleResource(RoleService roleService) {
        this.roleService = roleService;
    }

    @POST
    public Response createRole(@Valid CreateRoleRequest request) {

        Role role = roleService.createRole(
                request.getTenantId(),
                request.getName()
        );

        RoleResponse response = new RoleResponse(
                role.getId(),
                role.getTenantId(),
                role.getName(),
                role.getCreatedAt()
        );

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }
}