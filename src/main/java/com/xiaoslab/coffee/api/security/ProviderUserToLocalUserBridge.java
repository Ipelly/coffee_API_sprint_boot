package com.xiaoslab.coffee.api.security;

import com.xiaoslab.coffee.api.objects.AppAuthority;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.repository.UserRepository;
import com.xiaoslab.coffee.api.specifications.UserSpecifications;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProviderUserToLocalUserBridge {

    @Autowired
    UserRepository userRepository;

    private static Logger logger = Logger.getLogger(ProviderUserToLocalUserBridge.class);

    public User findOrCreateLocalUserFromProviderUser(User providerUser) {

        List<User> localUsers = new ArrayList<>();
        User foundLocalUser;

        try {
            localUsers = userRepository.findAll(Specifications
                    .where(UserSpecifications.isNotDeleted())
                    .and(UserSpecifications.hasProviderUserId(
                            providerUser.getProviderUserId(), providerUser.getProviderType()
                    ))
            );
        } catch (Exception ex) {
            logger.error(ex);
        }

        if (localUsers.isEmpty()) {
            providerUser.setStatus(Constants.StatusCodes.ACTIVE);
            providerUser.setRoles(Arrays.asList(new AppAuthority(Roles.ROLE_USER)));
            foundLocalUser = userRepository.save(providerUser);
            logger.info(String.format("Local user [userId:%s] created for provider user [%s:%s].",
                    foundLocalUser.getUserId(), providerUser.getProviderType(), providerUser.getProviderUserId()));
        } else if (localUsers.size() == 1) {
            foundLocalUser = localUsers.get(0);
            logger.info(String.format("Local user [userId:%s] found for provider user [%s:%s].",
                    foundLocalUser.getUserId(), providerUser.getProviderType(), providerUser.getProviderUserId()));
        } else {
            logger.warn("Multiple non-deleted local users were found for same provider user. Keeping only latest and deleting others.");
            for(int i = 0; i <= localUsers.size()-2; i++) {
                localUsers.get(i).setStatus(Constants.StatusCodes.DELETED);
                userRepository.save(localUsers.get(i));
            }
            foundLocalUser = localUsers.get(localUsers.size()-1);
            logger.info(String.format("Local user [userId:%s] found for provider user [%s:%s].",
                    foundLocalUser.getUserId(), providerUser.getProviderType(), providerUser.getProviderUserId()));
        }
        return foundLocalUser;
    }
}
