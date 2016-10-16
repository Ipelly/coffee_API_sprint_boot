package com.xiaoslab.coffee.api.dao;

import com.xiaoslab.coffee.api.objects.ItemAddon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class ItemAddonDao {

    private static final BeanPropertyRowMapper<ItemAddon> ITEMADDON_ROW_MAPPER = BeanPropertyRowMapper.newInstance(ItemAddon.class);
    private static final String ITEMADDON_SQL = "SELECT * FROM xipli.ItemAddon";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<ItemAddon> ItemAddons() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(ITEMADDON_SQL, parameters, ITEMADDON_ROW_MAPPER);
    }
}
