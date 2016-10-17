package com.xiaoslab.coffee.api.services;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback(true)
public abstract class _BaseServiceTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }

    protected NamedParameterJdbcTemplate getJdbcTemplate() {
        return this.namedParameterJdbcTemplate;
    }

    @Test
    public void checkDatabaseConnection(){
        Object verion = namedParameterJdbcTemplate.queryForList("SELECT VERSION()", new MapSqlParameterSource());
        getLogger().info(verion);
        assertNotNull("Database connection failed", verion);
    }

}