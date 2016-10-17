package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ItemAddonDao;
import com.xiaoslab.coffee.api.objects.ItemAddon;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemAddonService implements IService<ItemAddon> {

    @Autowired
    private ItemAddonDao itemAddonDao;

    @Override
    public List<ItemAddon> getAll() {
        return itemAddonDao.ItemAddons();
    }

    @Override
    public ItemAddon get(String id) {
        return null;
    }

    @Override
    public Boolean Insert(ItemAddon obj) {
        return null;
    }

    @Override
    public Boolean Update(ItemAddon pbj) {
        return null;
    }

    @Override
    public Boolean Delete(ItemAddon obj) {
        return null;
    }
}
