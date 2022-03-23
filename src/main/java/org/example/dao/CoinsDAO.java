package org.example.dao;

import org.example.models.Coin;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CoinsDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/memory_coins";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "sqlsql";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean findCoin(String partNumber) {
        ResultSet resultSet;
        Boolean bool = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM coins WHERE part_number=?");
            preparedStatement.setString(1, partNumber);
            resultSet = preparedStatement.executeQuery();
            bool = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bool;
    }

    public void save(List<Coin> coins) {
        try {
            for (Coin coin : coins) {
                if (showCoin(coin.getPartNumber()) != null) continue;
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO coins VALUES (?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, coin.getPartNumber());
                preparedStatement.setDate(2, new java.sql.Date(coin.getDt().getTime()));
                preparedStatement.setString(3, coin.getCname());
                preparedStatement.setString(4, coin.getSname());
                preparedStatement.setString(5, coin.getNominal());
                preparedStatement.setString(6, coin.getMetal());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public List<Coin> showAll() {
        List<Coin> coins = new ArrayList<>();
        String SQL;
        try {
            Statement statement = connection.createStatement();
            SQL = "SELECT * FROM coins";
            System.out.println(SQL);
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Coin coin = new Coin();
                coin.setPartNumber(resultSet.getString("part_number"));
                coin.setCname(resultSet.getString("cname"));
                coin.setSname(resultSet.getString("sname"));
                coin.setDt(resultSet.getDate("dt"));
                coin.setNominal(resultSet.getString("nominal"));
                coin.setMetal(resultSet.getString("metal"));

                coins.add(coin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coins;
    }


    public Coin showCoin(String partNumber) {
        Coin coin = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM coins WHERE part_number=?");
            preparedStatement.setString(1, partNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            coin = new Coin();
            coin.setPartNumber(resultSet.getString("part_number"));
            coin.setCname(resultSet.getString("cname"));
            coin.setSname(resultSet.getString("sname"));
            coin.setDt(resultSet.getDate("dt"));
            coin.setNominal(resultSet.getString("nominal"));
            coin.setMetal(resultSet.getString("metal"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coin;

    }


    public Object showQuery(String query, String value) {
        List<Coin> coins = new ArrayList<>();
        String SQL;
        try {
            Statement statement = connection.createStatement();
            if (!query.equals("dt")) {
                SQL = "SELECT * FROM coins WHERE " + query + " LIKE '" + value + "'";
            } else {
                SQL = "SELECT * FROM coins WHERE DATE_PART('year', " + query +") = DATE_PART('year', TIMESTAMP '" + value + "')";
            }
            System.out.println(SQL);
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Coin coin = new Coin();
                coin.setPartNumber(resultSet.getString("part_number"));
                coin.setCname(resultSet.getString("cname"));
                coin.setSname(resultSet.getString("sname"));
                coin.setDt(resultSet.getDate("dt"));
                coin.setNominal(resultSet.getString("nominal"));
                coin.setMetal(resultSet.getString("metal"));

                coins.add(coin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coins;
    }


}
