package com.silverkey.permission;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/permissions")
@Consumes(MediaType.APPLICATION_JSON)
public class PermissionResource {

    private final PermissionService permissionService;

    public PermissionResource(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @POST
    public Response createPermission(
            @Valid CreatePermissionRequest request
    ) {

        Permission permission = permissionService.createPermission(
                request.getName(),
                request.getDescription()
        );

        PermissionResponse response = new PermissionResponse(
                permission.getId(),
                permission.getName(),
                permission.getDescription()
        );

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }
}