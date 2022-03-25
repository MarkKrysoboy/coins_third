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

    public Coin completionCoinFromDb(ResultSet resultSet) throws SQLException {
        Coin coin = new Coin();
        coin.setPartNumber(resultSet.getString("part_number"));
        coin.setCname(resultSet.getString("cname"));
        coin.setSname(resultSet.getString("sname"));
        coin.setDt(resultSet.getDate("dt"));
        coin.setNominal(resultSet.getString("nominal"));
        coin.setMetal(resultSet.getString("metal"));
        return coin;
    }
//
//
//    public boolean findCoin(String partNumber) {
//        ResultSet resultSet;
//        Boolean bool = null;
//        try {
//            PreparedStatement preparedStatement =
//                    connection.prepareStatement("SELECT * FROM coins WHERE part_number=?");
//            preparedStatement.setString(1, partNumber);
//            resultSet = preparedStatement.executeQuery();
//            bool = resultSet.next();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return bool;
//    }

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
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM coins ORDER BY part_number");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                coins.add(completionCoinFromDb(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coins;
    }


    public Coin showCoin(String partNumber) {
        Coin coin = new Coin();
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM coins WHERE part_number=?");
            preparedStatement.setString(1, partNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            coin = completionCoinFromDb(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coin;

    }

    public List<Coin> sortQuery(String query) {
        List<Coin> coins = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM coins ORDER BY " + query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                coins.add(completionCoinFromDb(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coins;
    }

    public Object showQuery(String query, String value) {
        List<Coin> coins = new ArrayList<>();
        try {
            PreparedStatement preparedStatement;
            if (!query.equals("dt")) {
                preparedStatement = connection.prepareStatement("SELECT * FROM coins WHERE " + query + " LIKE '"
                        + value + "' ORDER BY part_number");
            } else {
                preparedStatement = connection.prepareStatement("SELECT * FROM coins WHERE DATE_PART('year', "
                        + query + ") = DATE_PART('year', TIMESTAMP '" + value + "') ORDER BY part_number");
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                coins.add(completionCoinFromDb(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coins;
    }


}
