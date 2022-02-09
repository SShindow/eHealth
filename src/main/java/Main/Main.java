package Main;

import Connection.DBControl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;

/**
 * Main class to run the whole application
 * @author Hai Cao Xuan
 */
public class Main extends Application {
    /**
     * Start method to run application, starting from file login.fxml
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Connect To Database:
        DBControl.connectToDatabase();
        Parent root = FXMLLoader.load(getClass().getResource("/Controller/admin_edit.fxml"));
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Welcome!");
        primaryStage.show();


    }

    public static void main(String[] args) {

        launch();
    }
}