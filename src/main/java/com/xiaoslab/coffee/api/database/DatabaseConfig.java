package com.xiaoslab.coffee.api.database;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "database.config")
public class DatabaseConfig extends DataSourceProperties {

}
