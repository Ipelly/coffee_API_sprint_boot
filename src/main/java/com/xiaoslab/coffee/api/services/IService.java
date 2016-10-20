package com.xiaoslab.coffee.api.services;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ipeli on 10/14/16.
 */
@Service
public interface IService<T,E>  {

    List<T> getAll();

    T get(E obj);

    T Create(T obj);


    T Update(T pbj);

    T Delete(E obj);
}
