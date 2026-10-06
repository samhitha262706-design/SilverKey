package com.silverkey.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class PermissionAuthorizationFilter
        implements ContainerRequestFilter {

    private final PermissionChecker permissionChecker;

    @Context
    private ResourceInfo resourceInfo;

    public PermissionAuthorizationFilter(
            PermissionChecker permissionChecker
    ) {
        this.permissionChecker = permissionChecker;
    }

    @Override
    public void filter(
            ContainerRequestContext requestContext
    ) throws IOException {

        Method method = resourceInfo.getResourceMethod();

        if (method == null) {
            return;
        }

        RequiresPermission requiresPermission =
                method.getAnnotation(RequiresPermission.class);

        if (requiresPermission == null) {
            return;
        }

        Object userIdObject =
                requestContext.getProperty("userId");

        if (userIdObject == null) {
            throw new ForbiddenException(
                    "User authentication required."
            );
        }

        String[] requiredPermissions =
                requiresPermission.value();

        UUID userId = (UUID) userIdObject;

        boolean hasPermission = Arrays.stream(requiredPermissions)
                .anyMatch(permission ->
                        permissionChecker.hasPermission(
                                userId,
                                permission
                        )
                );

        if (!hasPermission) {
            throw new ForbiddenException(
                    "You do not have permission to access this resource."
            );
        }
    }
}