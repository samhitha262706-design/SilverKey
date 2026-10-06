package com.silverkey;

import com.silverkey.auth.AuthResource;
import com.silverkey.config.DatabaseFactory;
import com.silverkey.exception.GlobalExceptionMapper;
import com.silverkey.organization.OrganizationRepository;
import com.silverkey.organization.OrganizationResource;
import com.silverkey.organization.OrganizationService;
import com.silverkey.permission.PermissionRepository;
import com.silverkey.permission.PermissionResource;
import com.silverkey.permission.PermissionService;
import com.silverkey.role.*;
import com.silverkey.security.JwtAuthFilter;
import com.silverkey.security.JwtService;
import com.silverkey.security.PermissionAuthorizationFilter;
import com.silverkey.security.PermissionChecker;
import com.silverkey.tenant.TenantRepository;
import com.silverkey.tenant.TenantResource;
import com.silverkey.tenant.TenantService;
import com.silverkey.user.*;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import com.silverkey.system.HealthResource;
import org.jdbi.v3.core.Jdbi;

public class SilverKeyApplication extends Application<SilverKeyConfiguration> {

    public static void main(String[] args) throws Exception {
        new SilverKeyApplication().run(args);
    }



    @Override
    public void initialize(Bootstrap<SilverKeyConfiguration> bootstrap) {
        // Future bundles go here
    }

    @Override
    public void run(SilverKeyConfiguration configuration,
                    Environment environment) {

        environment.getObjectMapper()
                .findAndRegisterModules();

        environment.jersey().register(new HealthResource());

        Jdbi jdbi = DatabaseFactory.build(configuration, environment);

        OrganizationRepository organizationRepository =
                jdbi.onDemand(OrganizationRepository.class);

        OrganizationService organizationService =
                new OrganizationService(organizationRepository);

        environment.jersey().register(
                new OrganizationResource(organizationService)
        );

        TenantRepository tenantRepository =
                jdbi.onDemand(TenantRepository.class);

        TenantService tenantService =
                new TenantService(
                        tenantRepository,
                        organizationRepository
                );

        environment.jersey().register(
                new TenantResource(tenantService)
        );

        RoleRepository roleRepository =
                jdbi.onDemand(RoleRepository.class);

        RoleService roleService =
                new RoleService(
                        roleRepository,
                        tenantRepository
                );

        environment.jersey().register(
                new RoleResource(roleService)
        );

        PermissionRepository permissionRepository =
                jdbi.onDemand(PermissionRepository.class);

        PermissionService permissionService =
                new PermissionService(permissionRepository);

        environment.jersey().register(
                new PermissionResource(permissionService)
        );

        RolePermissionRepository rolePermissionRepository =
                jdbi.onDemand(RolePermissionRepository.class);

        RolePermissionService rolePermissionService =
                new RolePermissionService(
                        rolePermissionRepository,
                        roleRepository,
                        permissionRepository
                );

        environment.jersey().register(
                new RolePermissionResource(rolePermissionService)
        );

        JwtService jwtService = new JwtService(configuration.getJwt());
        environment.jersey().register(
                new JwtAuthFilter(jwtService)
        );

        UserRepository userRepository =
                jdbi.onDemand(UserRepository.class);

        UserService userService =
                new UserService(
                        userRepository,
                        jwtService,
                        tenantRepository
                );
        UserResource userResource = new UserResource(userService);

        AuthResource authResource = new AuthResource(userService);
        UserRoleRepository userRoleRepository =
                jdbi.onDemand(UserRoleRepository.class);

        UserRoleService userRoleService =
                new UserRoleService(
                        userRoleRepository,
                        userRepository,
                        roleRepository
                );

        environment.jersey().register(
                new UserRoleResource(userRoleService)
        );

        PermissionChecker permissionChecker =
                new PermissionChecker(
                        userRoleRepository,
                        rolePermissionRepository,
                        permissionRepository
                );

        environment.jersey().register(
                new PermissionAuthorizationFilter(permissionChecker)
        );



        environment.jersey().register(authResource);

        environment.jersey().register(userResource);
        environment.jersey().register(GlobalExceptionMapper.class);

        System.out.println("SilverKey IAM started successfully!");
    }
}
