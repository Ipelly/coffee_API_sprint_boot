package com.xiaoslab.coffee.api.apis;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.utilities.APIAdapter;
import com.xiaoslab.coffee.api.utilities.APIDataCreator;
import com.xiaoslab.coffee.api._BaseTestScript;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;

public abstract class _BaseAPITest extends _BaseTestScript {

    @Value("${local.server.port}")
    protected int port;

    @Value("http://localhost")
    protected String host;

    @Autowired
    protected TestRestTemplate template;

    @Autowired
    protected APIDataCreator apiDataCreator;

    @Autowired
    protected APIAdapter api;

    protected User CUSTOMER_USER;
    protected User XIPLI_ADMIN;

    @Before
    public void setupApiAdapter() {
        api.setTemplate(template);
        api.setHost(host);
        api.setPort(port);
        api.logout();
    }

    @Before
    public void setupApiUsers() {
        if (CUSTOMER_USER == null) CUSTOMER_USER = testUtils.createCustomerUser();
        if (XIPLI_ADMIN == null) XIPLI_ADMIN = testUtils.createXipliAdminUser();
    }

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {

        @Override
        protected void failed(Throwable throwable, Description description) {
            String errorMessage = throwable.getMessage();
            if (APIAdapter.getLastRequest() != null) {
                errorMessage = errorMessage +
                        "\nLast API Call: " +
                        "\nRequest URL: " + APIAdapter.getLastRequest().getMethod() + " " + APIAdapter.getLastRequest().getUrl() +
                        "\nRequest Body: " + APIAdapter.getLastRequest().getBody() +
                        "\nRequest Headers: " + APIAdapter.getLastRequest().getHeaders();
            }
            if (APIAdapter.getLastResponse() != null) {
                errorMessage = errorMessage +
                        "\nResponse Code: " + APIAdapter.getLastResponse().getStatusCode() +
                        "\nResponse Body: " + APIAdapter.getLastResponse().getBody();
            }

            try {
                FieldUtils.writeField(throwable, "detailMessage", errorMessage, true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    };

}