package com.xiaoslab.coffee.api;

import com.xiaoslab.coffee.api.utilities.TestUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

@RunWith(SpringRunner.class)
@Sql("classpath:database/ClearDatabaseForTest.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class _BaseTestScript {

    @Autowired
    protected TestUtils testUtils;

    protected Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }


    protected <T> void assertContains(Collection<T> collection, T item) {
        if (!collection.contains(item)) {
            throw new AssertionError(String.format("Collection <%s> does not contain the expected item <%s>", collection, item));
        }
    }

    protected void assertContains(String full, String sub) {
        if (!full.contains(sub)) {
            throw new AssertionError(String.format("String <%s> does not contain the expected sub string <%s>", full, sub));
        }
    }

    @Test
    public void initialize() {
        // do not remove this method, otherwise junit throws exception "No runnable methods"
        getLogger().info("Running tests...");
    }

}
