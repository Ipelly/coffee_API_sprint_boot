package com.xiaoslab.coffee.api.beans;

import com.xiaoslab.coffee.api.database.DatabaseConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(DatabaseConfig.class)
public class DatabaseBeans {

    private Log logger = LogFactory.getLog(getClass());

    @Bean
    @Primary
    DataSourceProperties dataSourceProperties(DatabaseConfig databaseConfig) {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();

        String url = databaseConfig.getUrl();
        String username = databaseConfig.getUsername();
        String password = databaseConfig.getPassword();
        if (System.getProperty("db.host") != null) {
            url = url.replace("//127.0.0.1", "//" + System.getProperty("db.host"));
        }
        if (System.getProperty("db.port") != null) {
            url = url.replace(":3306", ":" + System.getProperty("db.port"));
        }
        if (System.getProperty("db.username") != null) {
            username = System.getProperty("db.username");
        }
        if (System.getProperty("db.password") != null) {
            password = System.getProperty("db.password");
        }

        logger.warn("Database URL: " + url);
        logger.warn("Database User: " + username);
        databaseConfig.setUrl(url);

        dataSourceProperties.setUrl(url);
        dataSourceProperties.setUsername(username);
        dataSourceProperties.setPassword(password);
        dataSourceProperties.setName(databaseConfig.getName());

        return dataSourceProperties;
    }

}
