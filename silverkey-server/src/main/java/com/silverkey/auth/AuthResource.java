package com.silverkey.auth;

import com.silverkey.user.LoginRequest;
import com.silverkey.user.LoginResponse;
import com.silverkey.user.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final UserService userService;

    public AuthResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {

        String accessToken = userService.login(request);

        return Response.ok()
                .entity(new LoginResponse(accessToken))
                .build();
    }
}
