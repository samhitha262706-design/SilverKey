package com.silverkey;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.silverkey.config.JwtConfiguration;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class SilverKeyConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty("jwt")
    private JwtConfiguration jwt = new JwtConfiguration();

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public JwtConfiguration getJwt() {
        return jwt;
    }
}