package com.xiaoslab.coffee.api.services;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ipeli on 10/14/16.
 */
@Service
public interface IService<T>  {

    List<T> getAll();

    T get(long id);

    T create(T obj);


    T update(T obj);

    T delete(long id);
}
