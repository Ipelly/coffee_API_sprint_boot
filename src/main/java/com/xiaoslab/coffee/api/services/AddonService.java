package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.AddonDao;
import com.xiaoslab.coffee.api.objects.Addon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Created by ipeli on 10/16/16.
 */
public class AddonService implements IService<Addon> {

    @Autowired
    private AddonDao addonDao;

    @Override
    public List<Addon> list() {
        return addonDao.Addons();
    }

    @Override
    public Addon get(long id) {
        return null;
    }

    @Override
    public Addon create(Addon obj) {
        return null;
    }

    @Override
    public Addon update(Addon pbj) {
        return null;
    }

    @Override
    public Addon delete(long id) {
        return null;
    }

    @Override
    public List<Addon> list(Optional<Specification<Addon>> spec, Optional<Pageable> pageable) {
        return null;
    }
}
