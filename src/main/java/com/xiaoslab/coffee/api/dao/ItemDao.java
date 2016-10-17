package com.xiaoslab.coffee.api.dao;

import com.xiaoslab.coffee.api.objects.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemDao {

    private static final BeanPropertyRowMapper<Item> ITEM_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Item.class);
    private static final String ITEM_SQL = "SELECT * FROM xipli.Item";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Item> Items() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(ITEM_SQL, parameters, ITEM_ROW_MAPPER);
    }
}
