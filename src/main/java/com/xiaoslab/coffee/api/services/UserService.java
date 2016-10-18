package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.repository.UserRepository;
import com.xiaoslab.coffee.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {

    @Autowired
    UserRepository userRepository;

    // TODO: authorization
    public User get(long userId) {
        // TODO: validations
        return userRepository.getOne(userId);
    }

    // TODO: authorization
    public User create(User user) {
        // TODO: validations
        user.setUserId(0l);
        user.setStatus(Constants.StatusCodes.ACTIVE);
        return userRepository.save(user);
    }

    // TODO: authorization
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

    // TODO: authorization
    public User update(User user) {
        // TODO: validations
        return userRepository.save(user);
    }

    // TODO: authorization
    public User delete(long userId) {
        // TODO: validations
        User user = userRepository.findOne(userId);
        user.setStatus(Constants.StatusCodes.DELETED);
        return userRepository.save(user);
    }

}
