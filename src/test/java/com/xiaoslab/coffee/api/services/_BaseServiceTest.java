package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.utilities.TestUtils;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@Sql(scripts = "classpath:database/ClearDatabaseForTest.sql")
@SpringBootTest
public abstract class _BaseServiceTest {

    @Autowired
    protected TestUtils testUtils;

    protected Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }

}
