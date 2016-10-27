package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ItemOptionDao;
import com.xiaoslab.coffee.api.objects.ItemOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemOptionService implements IService<ItemOption> {

    @Autowired
    private ItemOptionDao itemOptionDao;

    @Override
    public List<ItemOption> list() {
        return itemOptionDao.ItemOptions();
    }

    @Override
    public ItemOption get(long id) {
        return null;
    }

    @Override
    public ItemOption create(ItemOption obj) {
        return null;
    }

    @Override
    public ItemOption update(ItemOption pbj) {
        return null;
    }

    @Override
    public ItemOption delete(long obj) {
        return null;
    }

    @Override
    public List<ItemOption> list(Optional<Specification<ItemOption>> spec, Optional<Pageable> pageable) {
        return null;
    }
}
