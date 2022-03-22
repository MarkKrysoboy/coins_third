package org.example.dao;

import org.example.models.Coin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CoinsMapper implements RowMapper<Coin> {
    @Override
    public Coin mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Coin coin = new Coin();
        coin.setPartNumber(resultSet.getString("part_number"));
        coin.setCname(resultSet.getString("cname"));
        coin.setSname(resultSet.getString("sname"));
        coin.setDt(resultSet.getDate("dt"));
        coin.setNominal(resultSet.getString("nominal"));
        coin.setMetal(resultSet.getString("metal"));

        return coin;
    }
}
