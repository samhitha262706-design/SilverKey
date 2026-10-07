package com.silverkey.user;

import com.silverkey.security.RequiresPermission;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("/register")
    public Response register(@Valid RegisterUserRequest request) {

        userService.register(request);

        return Response.status(Response.Status.CREATED)
                .entity("User registered successfully.")
                .build();
    }

    @GET
    @Path("/me")
    @RequiresPermission("USER_READ")
    public Response me(@Context ContainerRequestContext requestContext) {
        UUID userId = (UUID) requestContext.getProperty("userId");

        User user = userService.getUserById(userId);

        UserResponse response = new UserResponse(
                user.getId(),
                user.getTenantId(),
                user.getUsername(),
                user.getEmail()
        );

        return Response.ok(response).build();
    }
}