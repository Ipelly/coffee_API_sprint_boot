package com.xiaoslab.coffee.api.dao;

import com.xiaoslab.coffee.api.objects.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class IngredientDao {

    private static final BeanPropertyRowMapper<Ingredient> INGREDIENT_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Ingredient.class);
    private static final String INGREDIENT_SQL = "SELECT * FROM xipli.Ingredient";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Ingredient> Ingredients() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(INGREDIENT_SQL, parameters, INGREDIENT_ROW_MAPPER);
    }
}
