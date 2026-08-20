package com.silverkey;

import com.silverkey.auth.AuthResource;
import com.silverkey.config.DatabaseFactory;
import com.silverkey.exception.GlobalExceptionMapper;
import com.silverkey.security.JwtAuthFilter;
import com.silverkey.security.JwtService;
import com.silverkey.user.UserRepository;
import com.silverkey.user.UserResource;
import com.silverkey.user.UserService;
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

        environment.jersey().register(new HealthResource());

        Jdbi jdbi = DatabaseFactory.build(configuration, environment);

        JwtService jwtService = new JwtService(configuration.getJwt());
        environment.jersey().register(
                new JwtAuthFilter(jwtService)
        );

        UserRepository userRepository =
                jdbi.onDemand(UserRepository.class);

        UserService userService =
                new UserService(userRepository, jwtService);

        UserResource userResource = new UserResource(userService);

        AuthResource authResource = new AuthResource(userService);

        environment.jersey().register(authResource);

        environment.jersey().register(userResource);
        environment.jersey().register(GlobalExceptionMapper.class);

        System.out.println("SilverKey IAM started successfully!");
    }
}
