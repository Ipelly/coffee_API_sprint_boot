package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.AppAuthority;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.repository.UserRepository;
import com.xiaoslab.coffee.api.security.Roles;
import com.xiaoslab.coffee.api.specifications.UserSpecifications;
import com.xiaoslab.coffee.api.utility.Constants;
import com.xiaoslab.coffee.api.utility.GroupValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;

import javax.annotation.security.RolesAllowed;
import java.util.*;

public class UserService implements IService<User>, UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger logger = Logger.getLogger(UserService.class);

    @RolesAllowed({Roles.ROLE_X_ADMIN, Roles.ROLE_SHOP_ADMIN})
    public User get(long userId) {
        // TODO: validations
        return userRepository.getOne(userId);
    }

    // this method should not be called. use either registerNewUser() or createShopUser() or createXAdmin()
    public User create(User user) {
        throw new UnsupportedOperationException("cannot create user through this service method");
    }

    @RolesAllowed({Roles.ROLE_X_ADMIN, Roles.ROLE_SHOP_ADMIN})
    public User createShopUser(User user) {
        return userRepository.save(user);
    }

    @RolesAllowed(Roles.ROLE_X_ADMIN)
    public User createXAdmin(User user) {
        user.setRoles(Arrays.asList(new AppAuthority(Roles.ROLE_X_ADMIN)));
        return userRepository.save(user);
    }

    @RolesAllowed({Roles.ROLE_X_ADMIN, Roles.ROLE_SHOP_ADMIN})
    public List<User> list() {
        return list(Optional.empty(), Optional.empty());
    }

    @RolesAllowed({Roles.ROLE_X_ADMIN, Roles.ROLE_SHOP_ADMIN})
    public List<User> list(Optional<Specification<User>> spec, Optional<Pageable> pageable) {
        // TODO: filter based on user access

        List<User> list = new ArrayList<>();

        if (spec.isPresent() &&  pageable.isPresent()) {
            userRepository.findAll(spec.get(), pageable.get()).forEach(list::add);
        } else if (spec.isPresent()) {
            userRepository.findAll(spec.get()).forEach(list::add);
        } else if (pageable.isPresent()) {
            userRepository.findAll(pageable.get()).forEach(list::add);
        } else {
            userRepository.findAll().forEach(list::add);
        }

        return list;
    }

    @RolesAllowed({Roles.ROLE_X_ADMIN, Roles.ROLE_SHOP_ADMIN})
    public User update(User user) {
        // TODO: validations
        return userRepository.save(user);
    }

    @RolesAllowed({Roles.ROLE_X_ADMIN, Roles.ROLE_SHOP_ADMIN})
    public User delete(long userId) {
        // TODO: validations
        User user = userRepository.findOne(userId);
        user.setStatus(Constants.StatusCodes.DELETED);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findOne(Specifications
                .where(UserSpecifications.hasEmailAddress(username))
                .and(UserSpecifications.isNotDeleted())
                .and(UserSpecifications.notEmptyPassword())
        );
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Invalid username or password.");
        } else {
            return user;
        }
    }

    public User registerNewUser(User newUser) {
        GroupValidator.validate(newUser, User.XipliUser.class);
        newUser.setUserId(null);
        newUser.setStatus(Constants.StatusCodes.PENDING);
        newUser.setProviderType(Constants.LoginProviderType.XIPLI);
        newUser.setProviderUserId(null);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoles(new ArrayList<>(Arrays.asList(new AppAuthority(Roles.ROLE_USER))));
        parseFirstAndLastName(newUser);
        checkIfEmailAddressIsUnique(newUser.getEmailAddress());

        newUser = userRepository.save(newUser);
        logger.info(String.format("A new user [userId:%s] was registered.", newUser.getUserId()));
        return newUser;
    }

    private void parseFirstAndLastName(User user) {
        if (StringUtils.isBlank(user.getName())) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        String[] names =  user.getName().trim().split("\\s");
        user.setFirstName(names[0]);
        if (names.length == 1) {
            user.setLastName(null);
        } else {
            StringBuilder lastName = new StringBuilder();
            for (int i = 1; i <= names.length-2; i++) {
                lastName.append(names[i]).append(" ");
            }
            lastName.append(names[names.length-1]);
            user.setLastName(lastName.toString());
        }
    }

    private void checkIfEmailAddressIsUnique(String emailAddress) {
        if (StringUtils.isBlank(emailAddress)) {
            throw new IllegalArgumentException("emailAddress cannot be empty");
        }
        List<User> usersWithSameEmail = userRepository.findAll(Specifications
                .where(UserSpecifications.hasEmailAddress(emailAddress))
                .and(UserSpecifications.notEmptyPassword())
                .and(UserSpecifications.isNotDeleted())
        );

        if (!CollectionUtils.isEmpty(usersWithSameEmail)) {
            throw new IllegalArgumentException("another user exists with same emailAddress: " + emailAddress);
        }
    }
}
