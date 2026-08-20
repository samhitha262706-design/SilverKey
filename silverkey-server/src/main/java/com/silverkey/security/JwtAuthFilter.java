package com.silverkey.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.UUID;

@Provider
public class JwtAuthFilter implements ContainerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        String path = requestContext.getUriInfo()
                .getPath();

        // Authentication endpoints are public
        if (path.startsWith("auth/")) {
            return;
        }
        if (path.startsWith("users/register")) {
            return;
        }

        String authorization =
                requestContext.getHeaderString("Authorization");

        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Missing or invalid Authorization header.")
                            .build()
            );

            return;
        }

        String token = authorization.substring(7);

        try {

            UUID userId = jwtService.extractUserId(token);

            requestContext.setProperty("userId", userId);

        } catch (Exception e) {

            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Invalid or expired token.")
                            .build()
            );
        }
    }
}