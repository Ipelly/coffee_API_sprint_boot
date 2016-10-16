package com.xiaoslab.coffee.api.services;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ipeli on 10/14/16.
 */
@Service
public interface IService<T> {
    List<T> getAll();

    T get(String id);

    Boolean Insert(T obj);

    Boolean Update(T pbj);

    Boolean Delete(T obj);
}
