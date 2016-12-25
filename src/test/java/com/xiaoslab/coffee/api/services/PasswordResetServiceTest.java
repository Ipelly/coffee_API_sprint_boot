package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.utilities.ServiceLoginUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PasswordResetServiceTest extends _BaseServiceTest {

    @Autowired
    PasswordResetService passwordResetService;

    @Autowired
    ServiceLoginUtils serviceLoginUtils;

    @Test
    public void createResetCode() {
        //getLogger().info(passwordResetService.createAndEmailCodeForUser(1L));
        //TODO: complete the test
    }

}
