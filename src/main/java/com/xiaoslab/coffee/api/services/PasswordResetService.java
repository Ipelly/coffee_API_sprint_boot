package com.xiaoslab.coffee.api.services;


import com.xiaoslab.coffee.api.objects.PasswordResetCode;
import com.xiaoslab.coffee.api.objects.PasswordUpdateRequest;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.repository.PasswordResetCodeRepository;
import com.xiaoslab.coffee.api.repository.UserRepository;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PasswordResetService {

    @Autowired
    PasswordResetCodeRepository passwordResetCodeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final Logger LOGGER = Logger.getLogger(PasswordResetService.class);

    @Transactional
    public PasswordResetCode createAndEmailCodeForUser(Long userId) {
        //TODO: validate userId
        //TODO: implement 24hour expiry of a code

        User user = userRepository.getOne(userId);
        if (user == null || user.getStatus() == Constants.StatusCodes.DELETED || user.getStatus() == Constants.StatusCodes.INACTIVE) {
            throw new IllegalArgumentException("Invalid userId");
        }
        PasswordResetCode resetCode = new PasswordResetCode();
        resetCode.setUserId(userId);
        resetCode.setCodeId(nextCode(10));
        String code = nextCode(20) + "-" + nextCode(10);
        resetCode.setCode(passwordEncoder.encode(code));

        resetCode = passwordResetCodeRepository.save(resetCode);
        user.setStatus(Constants.StatusCodes.PENDING);
        userRepository.save(user);

        //TODO: implement email service for sending a link
        LOGGER.info("Email this code to the user <" + user.getEmailAddress() + ">: " + resetCode.getCodeId() + "-" +  code);

        return resetCode;
    }

    @Transactional
    public PasswordResetCode updatePasswordForUser(PasswordUpdateRequest passwordUpdateRequest) {

        final String errInvalidCode = "Invalid reset code";
        final String errInvalidPassword = "Invalid password format";

        String fullResetCode = passwordUpdateRequest.getCode();

        // the format of the full code is ASDF123456-ASDF123456ASDF123456-ASDF123456
        // where the first group is the codeId, and the other two group is the reset code
        if (StringUtils.isEmpty(fullResetCode) || !fullResetCode.contains("-")) {
            throw new IllegalArgumentException(errInvalidCode);
        }
        //if (not a valid password) {
        //    throw new IllegalArgumentException(errInvalidPassword);
        //}

        String[] splitted_code = fullResetCode.split("-", 2);

        String codeId = splitted_code[0];
        PasswordResetCode resetCode = passwordResetCodeRepository.getOne(codeId);
        if (resetCode == null || splitted_code.length < 2) {
            throw new IllegalArgumentException(errInvalidCode);
        }

        String rawCode = splitted_code[1];
        if (!passwordEncoder.matches(rawCode, resetCode.getCode())) {
            throw new IllegalArgumentException(errInvalidCode);
        }

        User user = userRepository.findOne(resetCode.getUserId());
        if (user == null || user.getStatus() == Constants.StatusCodes.DELETED || user.getStatus() == Constants.StatusCodes.INACTIVE) {
            // to make sure user was not deleted or deactivated after the code was generated
            throw new IllegalArgumentException(errInvalidCode);
        }
        if (user != null && !StringUtils.equals(user.getEmailAddress(), passwordUpdateRequest.getEmailAddress())) {
            // to sure the code belongs to the same user
            throw new IllegalArgumentException(errInvalidCode);
        }

        // update the users password
        user.setPassword(passwordEncoder.encode(passwordUpdateRequest.getPassword()));
        user.setStatus(Constants.StatusCodes.ACTIVE);
        userRepository.save(user);

        // finally delete the used code
        passwordResetCodeRepository.delete(resetCode.getCodeId());
        return resetCode;
    }

    public User getUserByCodeId(String codeId) {
        PasswordResetCode resetCode = passwordResetCodeRepository.getOne(codeId);
        return userRepository.findOne(resetCode.getUserId());
    }

    public String nextCode(int len) {
        if (len < 1) return "";
        if (len > 100) throw new IllegalArgumentException("Length too long");
        return new BigInteger(5 * len, SECURE_RANDOM).toString(32);
    }
}
