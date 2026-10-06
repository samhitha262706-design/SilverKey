package com.silverkey.user;

import com.silverkey.role.Role;
import com.silverkey.role.RoleResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
public class UserRoleResource {

    private final UserRoleService userRoleService;

    public UserRoleResource(
            UserRoleService userRoleService
    ) {
        this.userRoleService = userRoleService;
    }

    @POST
    @Path("/{userId}/roles")
    public Response assignRole(
            @PathParam("userId") UUID userId,
            @Valid AssignRoleRequest request
    ) {

        userRoleService.assignRole(
                userId,
                request.getRoleId()
        );

        return Response.status(Response.Status.CREATED)
                .entity("Role assigned to user successfully.")
                .build();
    }

    @GET
    @Path("/{userId}/roles")
    public Response getRoles(
            @PathParam("userId") UUID userId
    ) {

        List<Role> roles =
                userRoleService.getRolesForUser(userId);

        List<RoleResponse> response = roles.stream()
                .map(role -> new RoleResponse(
                        role.getId(),
                        role.getTenantId(),
                        role.getName(),
                        role.getCreatedAt()
                ))
                .toList();

        return Response.ok(response).build();
    }
    @DELETE
    @Path("/{userId}/roles/{roleId}")
    public Response removeRole(
            @PathParam("userId") UUID userId,
            @PathParam("roleId") UUID roleId
    ) {

        userRoleService.removeRole(
                userId,
                roleId
        );

        return Response.noContent().build();
    }
}