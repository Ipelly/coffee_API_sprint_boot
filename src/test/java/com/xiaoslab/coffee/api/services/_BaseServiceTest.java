package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.utilities.TestUtils;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback(true)
@Sql("classpath:database/ClearDatabaseForTest.sql")
public abstract class _BaseServiceTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    protected TestUtils testUtils;


    protected Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }

    protected NamedParameterJdbcTemplate getJdbcTemplate() {
        return this.namedParameterJdbcTemplate;
    }

}
