package Controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.controlsfx.control.action.Action;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Controller class of after_login.fxml, display after logging in to the application successfully, providing various options
 * @author Hai Cao Xuan
 */
public class AfterLoginController implements Initializable{
    @FXML
    private Label label_welcomemessage;

    @FXML
    private Button button_editprofile;
    @FXML
    private Button button_bookappointment;
    @FXML
    private Button button_viewappointment;
    @FXML
    private Button button_signout;

    @FXML
    private ImageView image_background;

    /**
     * Method to initialize image
     * @param url of the images
     * @param resourceBundle used to store texts and components that are locale sensitive
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Make image displayable on run
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);
    }

    /**
     * Method to switch to the edit profile scene on click
     * @param event when clicking edit profile button
     * @throws Exception to catch unexpected exception
     */
    public void editProfileButtonOnAction(ActionEvent event) throws Exception{
//        Switch to edit profile stage
        Parent root = FXMLLoader.load(getClass().getResource("edit_profile.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to switch to book appointment scene on click
     * @param event when clicking book appointment button
     * @throws Exception to catch unexpected exception
     */
    public void bookAppointmentButtonOnActon(ActionEvent event) throws Exception{
//        Switch to edit profile stage
        Parent root = FXMLLoader.load(getClass().getResource("book_appointment.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to switch to view appointment scene on click
     * @param event when clicking edit profile button
     * @throws Exception to catch unexpected exception
     */
    public void viewAppointmentButtonOnAction(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view_appointment.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to return to login scene on click
     * @param event when clicking sign out button
     * @throws Exception to catch unexpected exception
     */
    public void signoutButtonOnAction(ActionEvent event) throws Exception{
//        After pressing sign out, it will return to the main screen
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
