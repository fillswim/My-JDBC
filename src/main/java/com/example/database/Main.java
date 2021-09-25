package com.example.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        FileInputStream fileInputStream;
        Properties properties = new Properties();

        String URL = null;
        String LOGIN = null;
        String PASSWORD = null;

        try {
            fileInputStream = new FileInputStream("src/main/resources/config.properties");
            properties.load(fileInputStream);

            URL = properties.getProperty("db.host");
            LOGIN = properties.getProperty("db.login");
            PASSWORD = properties.getProperty("db.password");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {

            // Создание тестового пользователя
            User testUser = new User("Test Name", 25, "testUser@gmail.com");

            create(connection, testUser.getName(), testUser.getAge(), testUser.getEmail());

            readAll(connection);

            read(connection, 7);

            updateName(connection, 7, "Pete");

            deleteUser(connection, 7);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static void create(Connection connection, String name, int age, String email) {
        final String CREATE = "INSERT INTO users(name, age, email) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(CREATE);

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, email);

            preparedStatement.execute();

            User user = new User(name, age, email);
            System.out.println("Создан пользователь: " + user);
            System.out.println("-------------------------------");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static void read(Connection connection, int id) {

        final String READ = "SELECT * FROM users WHERE id=?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(READ);
            preparedStatement.setInt(1, id);

            ResultSet resultSetRead = preparedStatement.executeQuery();

            System.out.println("Пользователь с id = " + id);
            while (resultSetRead.next()) {
                String name = resultSetRead.getString("name");
                int age = resultSetRead.getInt("age");
                String email = resultSetRead.getString("email");

                User user = new User(name, age, email);
                System.out.println(user);
            }

            System.out.println("-------------------------------");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    static void readAll(Connection connection) {

        final String READ_ALL = "SELECT * FROM users";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(READ_ALL);
            ResultSet resultSetRead = preparedStatement.executeQuery();

            System.out.println("Пользователи в базе данных");
            while (resultSetRead.next()) {
                String name = resultSetRead.getString("name");
                int age = resultSetRead.getInt("age");
                String email = resultSetRead.getString("email");

                User user = new User(name, age, email);
                System.out.println(user);
            }

            System.out.println("-------------------------------");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static void updateName(Connection connection, int id, String name) {
        final String UPDATE_NAME = "UPDATE users SET name=? WHERE id=?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_NAME);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

            System.out.println("Пользователь с id = " + id + " обновлён");
            System.out.println("-------------------------------");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static void deleteUser(Connection connection, int id) {
        final String DELETE_USER = "DELETE FROM users WHERE id=?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_USER);
            preparedStatement.setInt(1, id);

            preparedStatement.execute();

            System.out.println("Пользователь с id = " + id + " удалён");
            System.out.println("-------------------------------");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
