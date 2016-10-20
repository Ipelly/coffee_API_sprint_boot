package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.dao.ItemDao;
import com.xiaoslab.coffee.api.objects.Item;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemService implements IService<Item, String> {

    @Autowired
    private ItemDao itemDao;

    @Override
    public List<Item> getAll() {
        return itemDao.Items();
    }

    @Override
    public Item get(String id) {
        return null;
    }

    @Override
    public Item Create(Item obj) {
        return null;
    }

    @Override
    public Item Update(Item pbj) {
        return null;
    }

    @Override
    public Item Delete(String obj) {
        return null;
    }
}
