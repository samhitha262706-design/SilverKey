package com.silverkey;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import com.silverkey.resources.HealthResource;

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

        System.out.println("SilverKey IAM started successfully!");
    }
}
