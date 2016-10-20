package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.AddonDao;
import com.xiaoslab.coffee.api.objects.Addon;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class AddonService implements IService<Addon, String> {

    @Autowired
    private AddonDao addonDao;

    @Override
    public List<Addon> getAll() {
        return addonDao.Addons();
    }

    @Override
    public Addon get(String id) {
        return null;
    }

    @Override
    public Addon Create(Addon obj) {
        return null;
    }

    @Override
    public Addon Update(Addon pbj) {
        return null;
    }

    @Override
    public Addon Delete(String obj) {
        return null;
    }
}
