package com.xiaoslab.coffee.api.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoslab.coffee.api.objects.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.HashMap;
import java.util.List;

public class MenuItemDao {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final BeanPropertyRowMapper<MenuItem> MENU_ITEM_ROW_MAPPER = BeanPropertyRowMapper.newInstance(MenuItem.class);
    private static final String LIST_MENU_SQL = "SELECT * FROM demaze.TestMenu";
    private static final String GET_MENU_SQL = "SELECT * FROM demaze.TestMenu WHERE id = :id";
    private static final String CREATE_MENU_SQL = "INSERT INTO demaze.TestMenu (`Name`, `Price`) " +
            "VALUES (:name, :price) ";
    

    public List<MenuItem> listMenuItems() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(LIST_MENU_SQL, parameters, MENU_ITEM_ROW_MAPPER);
    }

    public MenuItem get(int id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(GET_MENU_SQL, parameters, MENU_ITEM_ROW_MAPPER);
    }

    public int create(MenuItem menuItem) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.convertValue(menuItem, HashMap.class);
        MapSqlParameterSource parameters = new MapSqlParameterSource(map);

        KeyHolder heyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(CREATE_MENU_SQL, parameters, heyHolder);
        return heyHolder.getKey().intValue();
    }


}
