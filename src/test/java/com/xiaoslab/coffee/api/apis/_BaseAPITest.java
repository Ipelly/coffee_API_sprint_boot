package com.xiaoslab.coffee.api.apis;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback(true)
public abstract class _BaseAPITest {

    @Value("${local.server.port}")
    private int port;

    @Value("http://localhost")
    private String host;

    @Value("v1")
    private String version;

    @Autowired
    private TestRestTemplate template;

    protected Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }

    protected String getBaseApiUrl() {
        return host + ":" + port + "/" + version;
    }

    protected ResponseEntity get(String path, Class returnType) {
        getLogger().info("Request: GET " + path);
        ResponseEntity entity = template.getForEntity(getBaseApiUrl() + path, returnType);
        getLogger().info("Resposne: " + entity);
        return entity;
    }
}