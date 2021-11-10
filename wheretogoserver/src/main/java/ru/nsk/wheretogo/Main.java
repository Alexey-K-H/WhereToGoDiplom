package ru.nsk.wheretogo;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        FileInputStream fis;
        Properties property = new Properties();

        String host;
        String login;
        String password;

        try {
            fis = new FileInputStream("src/main/resources/local_properties/local_config.properties");
            property.load(fis);

            host = property.getProperty("db.host");
            login = property.getProperty("db.login");
            password = property.getProperty("db.password");

            Connection connection;
            String connectionUrl = "jdbc:mysql://" + host;

            connection = DriverManager.getConnection(connectionUrl, login, password);
            if(connection != null){
                System.out.println("We are connected!");
            }

        } catch (SQLException exception) {
            System.err.println("Не удалось подключиться");
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }

    }
}
