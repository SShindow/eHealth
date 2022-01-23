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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;



public class LoginController implements Initializable {

    @FXML
    private Button button_login;
    @FXML
    private Button button_register;
    @FXML
    private Button button_cancel;

    @FXML
    private Label label_loginmessage;

    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField field_password;

    @FXML
    private ImageView image_background;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);
    }

    public void loginButtonOnAction() throws IOException {
//       if the username is not blank, go to the log in function to confirm account exist
        if (tf_username.getText().isBlank() == false && field_password.getText().isBlank() == false) {
            validateLogin();
        } else {
            label_loginmessage.setText("Please enter username and password");
        }
    }

    public void cancelButtonOnAction(ActionEvent event) {
//        Close the application
        Stage stage = (Stage) button_cancel.getScene().getWindow();
        stage.close();
    }

    public void registerButtonOnAction(ActionEvent event) throws Exception {
//        Switch to register stage
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void validateLogin() {
//        Connect to Database
        DBControl connectNow = new DBControl();
        Connection connectDB = connectNow.getConnection();

//        String verifyLogin = "SELECT * FROM customer WHERE username = '" + tf_username.getText() + "' AND password = '" + field_password.getText() + "' ";
        String sql = "SELECT password FROM customer WHERE username = '" + tf_username.getText() + "'";

        try {
            Statement st = connectDB.createStatement();
            ResultSet rs = st.executeQuery(sql);

            rs.next();
            String DBUserPassword = rs.getString(1);
            String inputPassword = field_password.getText();
            if (DBUserPassword.equals(inputPassword)) {
                label_loginmessage.setText("Login Successfully!");
                ActionEvent event = new ActionEvent();
                Parent root = FXMLLoader.load(getClass().getResource("after_login.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }


//            while(queryResult.next()){
//                if (queryResult.getInt(1) == 1 && field_password == ){
//                    label_loginmessage.setText("Login Successfully!");
////                    ActionEvent event = new ActionEvent();
////                    Parent root = FXMLLoader.load(getClass().getResource("after_login.fxml"));
////                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
////                    Scene scene = new Scene(root);
////                    stage.setScene(scene);
////                    stage.show();
//
//                }
            else {
                label_loginmessage.setText("Invalid credentials! Please try again.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}