package Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.DriverManager;

public class DBControl {


    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName = "new_schema";
        String databaseUser = "root";
        String databasePassword = "Caoxuanhai1811";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }

}
