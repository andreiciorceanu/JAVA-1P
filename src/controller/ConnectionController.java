package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionController {
    private Connection connection;

    private ConnectionController(){
        String url = "jdbc:mysql://localhost:3306/examenjavap";

        try {
            connection = DriverManager.getConnection(url, "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final class SingletonHolder{
        private static final ConnectionController instance = new ConnectionController();
    }

    public static final ConnectionController getInstance(){
        return SingletonHolder.instance;
    }

    public Connection getConnection(){
        return connection;
    }
}
