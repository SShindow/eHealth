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
        String databaseName = "ehealth";
        String databaseUser = "admin";
        String databasePassword = "vgustudent";
        String url = "jdbc:mysql://ehealth-db.cqajckw84dii.us-east-1.rds.amazonaws.com:3306/" + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
            System.out.println("DB Connected");
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
            System.out.println("Sai cmnr");
        }
        return databaseLink;
    }

}
