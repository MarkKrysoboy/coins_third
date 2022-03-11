package org.example.dao;


import java.sql.*;
import java.time.LocalDate;

public class CoinsDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/memory_coins";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "sqlsql";

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void save(String[] str, Date date) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO coins VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, str[0]);
            preparedStatement.setDate(2, date);
            preparedStatement.setString(3, str[2]);
            preparedStatement.setString(4, str[3]);
            preparedStatement.setString(5, str[4]);
            preparedStatement.setString(6, str[5]);

            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }





}
