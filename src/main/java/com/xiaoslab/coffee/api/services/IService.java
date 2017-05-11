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

    default List<T> list() {
        throw new UnsupportedOperationException();
    }

    default List<T> list(long owningEntityId) {
        throw new UnsupportedOperationException();
    }

    default List<T> list(Optional<Specification<T>> specOptional, Optional<Pageable> pageableOptional) {
        throw new UnsupportedOperationException();
    }

    default T get(long id) {
        throw new UnsupportedOperationException();
    }

    default T create(T obj) {
        throw new UnsupportedOperationException();
    }

    default T update(T obj) {
        throw new UnsupportedOperationException();
    }

    default List<T> updateAll(long owningEntityId, List<T> list) {
        throw new UnsupportedOperationException();
    }

    default T delete(long id) {
        throw new UnsupportedOperationException();
    }

}
