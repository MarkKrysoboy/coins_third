package org.example.dao;


import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64;
import org.example.models.Coin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CoinsDAO {

    private static final String dbURL = "jdbc:postgresql://localhost:5432/memory_coins";
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
            connection = DriverManager.getConnection(dbURL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String toImageBase64(byte[] bytes) throws UnsupportedEncodingException {
        byte[] encodeBase64 = Base64.encode(bytes);
        String base64Encoded = new String(encodeBase64, "UTF-8");
        return base64Encoded;
    }

    public String getCirculation(String url) {
        Document doc;
        String amount = "";
        try {
            doc = Jsoup.connect(url).get();
            String body = doc.text();
            String regexp = "шт\\W\\s[\\d+\\s{1}]+\\b";
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(body);
            int i = 0;
            while (matcher.find(i)) {
                amount = i == 0 ? matcher.group().substring(4) : amount + " / " + matcher.group().substring(4);
                i = matcher.end();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return amount;
    }

    private byte[] downloadFile(String Url) {
        try (InputStream inputStream = new URL(Url).openStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Coin completionCoinFromDb(ResultSet resultSet) throws SQLException, UnsupportedEncodingException {
        Coin coin = new Coin();
        coin.setPartNumber(resultSet.getString("part_number"));
        coin.setCname(resultSet.getString("cname"));
        coin.setSname(resultSet.getString("sname"));
        coin.setDt(resultSet.getDate("dt"));
        coin.setNominal(resultSet.getString("nominal"));
        coin.setMetal(resultSet.getString("metal"));
        // Настроить отображение с БД, если нет подгрузить с URL
        coin.setAvers(toImageBase64(resultSet.getBytes("avers")));
        coin.setRevers(toImageBase64(resultSet.getBytes("revers")));
        coin.setCirculation(resultSet.getString("circulation"));
        return coin;
    }


    public void save(List<Coin> coins) {
        try {
            byte[] bytes;
            String circulation;
            for (Coin coin : coins) {
                if (showCoin(coin.getPartNumber()) != null) continue;
                PreparedStatement preparedStatement = connection
                        .prepareStatement("INSERT INTO coins VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )");
                preparedStatement.setString(1, coin.getPartNumber());
                preparedStatement.setDate(2, new java.sql.Date(coin.getDt().getTime()));
                preparedStatement.setString(3, coin.getCname());
                preparedStatement.setString(4, coin.getSname());
                preparedStatement.setString(5, coin.getNominal());
                preparedStatement.setString(6, coin.getMetal());
                bytes = downloadFile("https://cbr.ru/legacy/PhotoStore/img/" + coin.getPartNumber() + ".jpg");
                preparedStatement.setBytes(7, bytes);
                bytes = downloadFile("https://cbr.ru/legacy/PhotoStore/img/" + coin.getPartNumber() + "r.jpg");
                preparedStatement.setBytes(8, bytes);
                circulation = getCirculation("https://cbr.ru/cash_circulation/memorable_coins/coins_base/ShowCoins/?cat_num="
                        + coin.getPartNumber());
                preparedStatement.setString(9, circulation);
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
        } catch (SQLException | UnsupportedEncodingException e) {
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
            if (resultSet.next()) {
                coin = completionCoinFromDb(resultSet);
            }

        } catch (SQLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return coin;
    }


    public List<Coin> sortQuery(String query) {
        List<Coin> coins = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM coins ORDER BY " + query);
            // Исправить сортировку по серии (сначала без серии)
            // Исправить сортировку по номиналу (сортировать как инты)
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                coins.add(completionCoinFromDb(resultSet));
            }
        } catch (SQLException | UnsupportedEncodingException e) {
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
        } catch (SQLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return coins;
    }


}
