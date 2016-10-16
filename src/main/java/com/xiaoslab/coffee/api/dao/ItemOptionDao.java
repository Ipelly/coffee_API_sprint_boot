package com.xiaoslab.coffee.api.dao;

import com.xiaoslab.coffee.api.objects.ItemOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemOptionDao {

    private static final BeanPropertyRowMapper<ItemOption> ITEMOPTION_ROW_MAPPER = BeanPropertyRowMapper.newInstance(ItemOption.class);
    private static final String ITEMOPTION_SQL = "SELECT * FROM xipli.ItemOption";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<ItemOption> ItemOptions() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(ITEMOPTION_SQL, parameters, ITEMOPTION_ROW_MAPPER);
    }
}
