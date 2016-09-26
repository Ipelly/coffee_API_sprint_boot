package com.xiaoslab.coffee.api.dao;

import com.xiaoslab.coffee.api.objects.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class MenuItemDao {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final BeanPropertyRowMapper<MenuItem> MENU_ITEM_ROW_MAPPER = BeanPropertyRowMapper.newInstance(MenuItem.class);
    private static final String LIST_MENU_SQL = "SELECT * FROM demaze.TestMenu";

    public List<MenuItem> listMenuItems() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(LIST_MENU_SQL, parameters, MENU_ITEM_ROW_MAPPER);
    }
}
