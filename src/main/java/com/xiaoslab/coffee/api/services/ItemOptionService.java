package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ItemOptionDao;
import com.xiaoslab.coffee.api.objects.ItemOption;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemOptionService implements IService<ItemOption,String> {

    @Autowired
    private ItemOptionDao itemOptionDao;

    @Override
    public List<ItemOption> getAll() {
        return itemOptionDao.ItemOptions();
    }

    @Override
    public ItemOption get(String id) {
        return null;
    }

    @Override
    public ItemOption Create(ItemOption obj) {
        return null;
    }

    @Override
    public ItemOption Update(ItemOption pbj) {
        return null;
    }

    @Override
    public ItemOption Delete(String obj) {
        return null;
    }
}
