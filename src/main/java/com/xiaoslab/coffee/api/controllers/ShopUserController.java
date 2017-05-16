package com.xiaoslab.coffee.api.controllers;

import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.objects.User;
import com.xiaoslab.coffee.api.objects.User.NewUserRequest;
import com.xiaoslab.coffee.api.services.IService;
import com.xiaoslab.coffee.api.services.UserService;
import com.xiaoslab.coffee.api.specifications.UserSpecifications;
import com.xiaoslab.coffee.api.utility.AppUtility;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = Constants.V1 + Constants.SHOP_ENDPOINT + "/{shopId}" + Constants.USER_ENDPOINT)
public class ShopUserController {

    @Autowired
    private IService<Shop> shopService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity list(
            @PathVariable long shopId,
            @RequestParam(name = "size", required = true) int size,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "search", required = false) String search) {

        Pageable pageable = AppUtility.createPageRequest(page, size);
        AppUtility.notFoundOnNull(shopService.get(shopId));

        Specification<User> specification = UserSpecifications.isNotDeleted();
        specification = Specifications.where(specification).and(UserSpecifications.belongsToShopId(shopId));

        if (StringUtils.isNotBlank(search)) {
            //specification = Specifications.where(specification).and(UserSpecifications.search(search));
        }
        return ResponseEntity.ok(userService.list(Optional.of(specification), Optional.of(pageable)));
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable long shopId, @PathVariable long userId) {
        throw new NotImplementedException("not implemented");
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity create(@PathVariable long shopId, @RequestBody NewUserRequest newUserRequest) {
        AppUtility.notFoundOnNull(shopService.get(shopId));
        User newUser = User.copyFromNewUserRequest(newUserRequest);
        newUser.setShopId(shopId);
        User createdUser = userService.create(newUser);
        URI location = AppUtility.buildCreatedLocation(createdUser.getUserId());
        return ResponseEntity.created(location).body(createdUser);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable long shopId, @PathVariable long userId, @RequestBody User user) {
        throw new NotImplementedException("not implemented");
    }

    @RequestMapping(path = "/{userId}", method=RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable long shopId, @PathVariable long userId) {
        throw new NotImplementedException("not implemented");
    }
}

