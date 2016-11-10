package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.utilities.APIAdapter;
import com.xiaoslab.coffee.api.utilities.APITestUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

//Rollback does not work for integration tests, because spring runs the 
//web application on a different thread than the test. As an alternative,
//we are removing test data by using @Sql annotation with a cleanup script.
//@Transactional
//@Rollback(true)
@Sql("classpath:database/ClearDatabaseForTest.sql")
public abstract class _BaseAPITest {

    @Value("${local.server.port}")
    protected int port;

    @Value("http://localhost")
    protected String host;

    @Autowired
    protected TestRestTemplate template;

    @Autowired
    protected APITestUtils apiTestUtils;

    protected APIAdapter api;

    protected User CUSTOMER_USER;
    protected User XIPLI_ADMIN;

    @Before
    public void setupApiAdapter() {
        if (api == null) {
            api = new APIAdapter(template, host, port);
        }
    }

    @Before
    public void setupUsers() {
        if (CUSTOMER_USER == null) CUSTOMER_USER = apiTestUtils.createCustomerUser();
        if (XIPLI_ADMIN == null) XIPLI_ADMIN = apiTestUtils.createXipliAdminUser();
    }

    public Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {

        @Override
        protected void failed(Throwable throwable, Description description) {
            String originalMessage = throwable.getMessage();

            String newMessage = originalMessage + "\nLast API Call: " +
                    "\nRequest URL: " + APIAdapter.getLastRequest().getMethod() + " " + APIAdapter.getLastRequest().getUrl() +
                    "\nRequest Body: " + APIAdapter.getLastRequest().getBody() +
                    "\nRequest Headers: " + APIAdapter.getLastRequest().getHeaders() +
                    "\nResponse Code: " + APIAdapter.getLastResponse().getStatusCode() +
                    "\nResponse Body: " + APIAdapter.getLastResponse().getBody();

            try {
                FieldUtils.writeField(throwable, "detailMessage", newMessage, true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    };

}