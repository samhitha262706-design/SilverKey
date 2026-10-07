package com.silverkey.config;

import com.silverkey.SilverKeyConfiguration;
import io.dropwizard.core.setup.Environment;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public final class DatabaseFactory {

    private DatabaseFactory() {
    }

    public static Jdbi build(SilverKeyConfiguration configuration,
                             Environment environment) {

        var dataSource = configuration.getDataSourceFactory()
                .build(environment.metrics(), "postgresql");

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();

        flyway.migrate();

        Jdbi jdbi = Jdbi.create(dataSource);

        jdbi.installPlugin(new SqlObjectPlugin());

        return jdbi;
    }
}