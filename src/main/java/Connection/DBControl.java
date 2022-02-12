package Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A class to establish connection to database
 * @author Hai Cao Xuan, Hoang Dinh Minh
 */
public class DBControl {

    /**
     * Set parameter for connection
     */
    static public Connection dbConnection;

    /**
     * Method to set a connection to database
     * @author Hai Cao Xuan
     */
    static public void connectToDatabase() {
        String databaseName = "ehealth";
        String databaseUser = "admin";
        String databasePassword = "vgustudent";
        String url = "jdbc:mysql://ehealth-db.cqajckw84dii.us-east-1.rds.amazonaws.com:3306/" + databaseName;

        try {
            loadJDBCDriver();
            dbConnection = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("Database Connected!");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            System.out.println("Connection Failed!");
        }
        //return databaseConnection;
    }// end getConnection()

    /**
     * Method to load JDBC Driver
     * @author Hoang Dinh Minh
     */
    static public void loadJDBCDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }
    }

    /**
     * Method to connect Database with return connection
     * @return connection
     */
    public static Connection connectToDatabaseWithReturnConnection() throws SQLException {
        final String HOSTNAME = "ehealth-db.cqajckw84dii.us-east-1.rds.amazonaws.com";
        final String PORT = "3306";
        final String DBUSERNAME = "admin";
        final String DBPASSWORD = "vgustudent";
        final String DATABASENAME = "ehealth";
        final String URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/ " + DATABASENAME;
        return DriverManager.getConnection(URL, DBUSERNAME, DBPASSWORD);
    }

}

