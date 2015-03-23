package control;


import util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private DatabaseConnectionCredentials credentials = null;
    private Connection connection = null;

    private DatabaseConnection() {
    }

    public void setCredentials(DatabaseConnectionCredentials c) {
        AppUtils.checkNull("Credentials object must not be null", c);
        credentials = c;
    }

    private void connectHelper() {
        AppUtils.checkNull("Credentials object must not be null", credentials);

        try {
            Class.forName(credentials.getDriverName());
        } catch (ClassNotFoundException e) {
            AppGlobals.out.display("Not a valid Oracle JDBC Driver " + e.getMessage());
            return;
        }

        AppGlobals.out.display("Oracle JDBC Driver Registered!");

        try {
            connection = DriverManager.getConnection(credentials.getUrl(), credentials.getUser(), credentials.getPassword());
        } catch (SQLException e) {
            AppGlobals.out.display("Connection Failed!  " + e.getMessage());
            return;
        }

        AppGlobals.out.display("Connected!");
    }

    public void disconnect() {

        if (connection == null) {
            AppGlobals.out.display("There is not a valid connection!");
            return;
        }

        try {
            connection.close();
            connection = null; // if I put the next 2 statements in finally block I reset my object without really closing my connection (ex physically network connection which is temporally broken)
            credentials = null;
        } catch (SQLException e) {
            AppGlobals.out.display("Failed to close connection!" + e.getMessage());
            return;
        }finally{
            //...
        }

        AppGlobals.out.display("Connection closed!");
    }

    public Connection connect() {
        if (connection == null) {
            connectHelper();
        }
        return connection;
    }

    public static DatabaseConnection getInstance() {
        return DatabaseConnectionHolder.INSTANCE;
    }

    private static class DatabaseConnectionHolder {

        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }
}
