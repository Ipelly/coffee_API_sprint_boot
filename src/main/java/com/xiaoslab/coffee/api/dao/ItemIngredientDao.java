package com.xiaoslab.coffee.api.dao;

import com.xiaoslab.coffee.api.objects.ItemIngredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemIngredientDao {

    private static final BeanPropertyRowMapper<ItemIngredient> ITEMINGREDIENT_ROW_MAPPER = BeanPropertyRowMapper.newInstance(ItemIngredient.class);
    private static final String ITEMINGREDIENT_SQL = "SELECT * FROM xipli.ItemIngredient";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<ItemIngredient> ItemIngredients() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(ITEMINGREDIENT_SQL, parameters, ITEMINGREDIENT_ROW_MAPPER);
    }
}
