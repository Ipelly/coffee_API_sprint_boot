package com.xiaoslab.coffee.api.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by ipeli on 10/14/16.
 */
@Service
public interface IService<T>  {

    List<T> list();

    List<T> list(Optional<Specification<T>> specOptional, Optional<Pageable> pageableOptional);

    T get(long id);

    T create(T obj);

    T update(T obj);

    T delete(long id);
}
