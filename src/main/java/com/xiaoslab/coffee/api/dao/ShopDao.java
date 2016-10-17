package com.xiaoslab.coffee.api.dao;

import com.xiaoslab.coffee.api.objects.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

/**
 * Created by ipeli on 10/1/16.
 */


public class ShopDao {

    private static final BeanPropertyRowMapper<Shop> SHOP_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Shop.class);
    private static final String SHOP_SQL = "SELECT * FROM xipli.Shop";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Shop> Shops() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(SHOP_SQL, parameters, SHOP_ROW_MAPPER);
    }
}