package com.silverkey.role;

import com.silverkey.permission.PermissionResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.silverkey.permission.Permission;
import java.util.List;

import java.util.UUID;

@Path("/roles")
@Consumes(MediaType.APPLICATION_JSON)
public class RolePermissionResource {

    private final RolePermissionService rolePermissionService;

    public RolePermissionResource(
            RolePermissionService rolePermissionService
    ) {
        this.rolePermissionService = rolePermissionService;
    }

    @POST
    @Path("/{roleId}/permissions")
    public Response assignPermission(
            @PathParam("roleId") UUID roleId,
            @Valid AssignPermissionRequest request
    ) {

        rolePermissionService.assignPermission(
                roleId,
                request.getPermissionId()
        );

        return Response.status(Response.Status.CREATED)
                .entity("Permission assigned to role successfully.")
                .build();
    }
    @GET
    @Path("/{roleId}/permissions")
    public Response getPermissions(
            @PathParam("roleId") UUID roleId
    ) {

        List<Permission> permissions =
                rolePermissionService.getPermissionsForRole(roleId);

        List<PermissionResponse> response = permissions.stream()
                .map(permission -> new PermissionResponse(
                        permission.getId(),
                        permission.getName(),
                        permission.getDescription()
                ))
                .toList();

        return Response.ok(response).build();
    }
    @DELETE
    @Path("/{roleId}/permissions/{permissionId}")
    public Response removePermission(
            @PathParam("roleId") UUID roleId,
            @PathParam("permissionId") UUID permissionId
    ) {

        rolePermissionService.removePermission(
                roleId,
                permissionId
        );

        return Response.noContent().build();
    }
}