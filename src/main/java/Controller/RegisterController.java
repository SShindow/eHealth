package Controller;

import Connection.DBControl;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;

import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {

    @FXML
    private Button button_register;
    @FXML
    private Button button_login;

    @FXML
    private Label label_registermessage;

    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_email_address;

    @FXML
    private PasswordField field_password;
    @FXML
    private PasswordField field_password1;

    @FXML
    private ImageView image_background;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);
    }

    public void loginButtonOnAction(ActionEvent event) throws IOException {
        //Switch to log in scene
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void registerButtonOnAction(ActionEvent event){
//        If the password is match with the password confirmation
        if (field_password.getText().equals(field_password1.getText())){
            registerUser();
        } else{
            label_registermessage.setText("Password does not match!");
        }
    }

    public void registerUser(){
        DBControl connectNow = new DBControl();
        Connection connectDB = connectNow.getConnection();

        String username = tf_username.getText();
        String password = field_password.getText();
        String email_address = tf_email_address.getText();

        String insertField = "INSERT INTO customer(username, password, email_address) VALUES ('";
        String insertValue = username + "','" + password + "','" + email_address + "')";
        String insertToRegister = insertField + insertValue;

        try{
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);
            label_registermessage.setText("User has been registered successfully!");
        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


}
