package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ItemAddonDao;
import com.xiaoslab.coffee.api.objects.ItemAddon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemAddonService implements IService<ItemAddon> {

    @Autowired
    private ItemAddonDao itemAddonDao;

    @Override
    public List<ItemAddon> list() {
        return itemAddonDao.ItemAddons();
    }

    @Override
    public ItemAddon get(long id) {
        return null;
    }

    @Override
    public ItemAddon create(ItemAddon obj) {
        return null;
    }

    @Override
    public ItemAddon update(ItemAddon pbj) {
        return null;
    }

    @Override
    public ItemAddon delete(long obj) {
        return null;
    }

    @Override
    public List<ItemAddon> list(Optional<Specification<ItemAddon>> spec, Optional<Pageable> pageable) {
        return null;
    }
}
