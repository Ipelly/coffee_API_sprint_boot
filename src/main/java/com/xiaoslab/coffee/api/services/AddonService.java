package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.AddonDao;
import com.xiaoslab.coffee.api.objects.Addon;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class AddonService implements IService<Addon> {

    @Autowired
    private AddonDao addonDao;

    @Override
    public List<Addon> getAll() {
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
}
