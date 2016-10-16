package com.xiaoslab.coffee.api.dao;

import com.xiaoslab.coffee.api.objects.Addon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

/**
 * Created by ipeli on 10/16/16.
 */
public class AddonDao {

    private static final BeanPropertyRowMapper<Addon> ADDON_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Addon.class);
    private static final String ADDON_SQL = "SELECT * FROM xipli.Addon";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Addon> Addons() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(ADDON_SQL, parameters, ADDON_ROW_MAPPER);
    }
}
