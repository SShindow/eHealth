package Controller;
import Connection.DBControl;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Controller class of admin_login.fxml, for admin to log in to the application
 * @author Hoang Dinh Minh
 */
public class AdminLoginController implements Initializable {

    @FXML
    private ImageView image_background;
    @FXML
    TextField tf_adminUsername;
    @FXML
    TextField tf_adminPassword;
    @FXML
    private Label label_loginmessage;
    @FXML
    Button tf_adminLogin;
    @FXML
    Button tf_adminCancel;


    /**
     * Method to initialize images and other elemtents
     * @param url of the images and other elements
     * @param resourceBundle used to store texts and components that are locale sensitive
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);
    }


    /**
     * Method to login to the admin platform in order to edit or delete user/doctor information
     * @param event when clicking log in button
     * @throws SQLException that provides information on a database access error or other errors
     * @throws NoSuchAlgorithmException detect other underlying exceptions
     * @throws InvalidKeySpecException when an invalid key specification is encountered
     * @throws IOException when encounter an I/O exception to some sort has occurred
     */
    @FXML
    public void loginButtonOnAction(ActionEvent event) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        if (tf_adminUsername.getText().isBlank() == false && tf_adminPassword.getText().isBlank() == false && validateLogin()) {

            label_loginmessage.setText("Welcome admin!");
            //Switch to Admin Edit scene
            Parent root = FXMLLoader.load(getClass().getResource("admin_edit.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            label_loginmessage.setText("Invalid credentials! Please try again.");
        }
    }

    /**
     * Method to return to login scene on click
     * @param event when clicking on cancel button
     * @throws IOException when encounter an I/O exception to some sort has occurred
     */
    @FXML
    public void cancelButtonOnAction(Event event) throws IOException {
        //Switch to log in scene
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to validate for login to the application
     * @return boolean value
     * @throws SQLException that provides information on a database access error or other errors
     * @throws NoSuchAlgorithmException detect other underlying exceptions
     * @throws InvalidKeySpecException when an invalid key specification is encountered
     * @throws IOException when encounter an I/O exception to some sort has occurred
     */
    public boolean validateLogin() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        // Get Input from user
        String username = tf_adminUsername.getText();
        String password = tf_adminPassword.getText();

        // Get salt and calculate hashedPassword to compare in Database. If match Login is successful.
        byte[] salt = getSaltFromDBUsernameAdmin(username);
        byte[] hashedPassword;
        if(salt.length == 0)
        {
            label_loginmessage.setText("Invalid credentials! Please try again.");
            return false;
        }
        else
            hashedPassword = generateHashedPassword(password, salt);


        if (isHashedPasswordCorrectAdmin(username, hashedPassword) == true) {
            label_loginmessage.setText("Login Successfully!");
            //loggedInUsername = username;
            return true;
        } else {
            System.out.println("Wrong password");
            label_loginmessage.setText("Invalid credentials! Please try again.");
            return false;
        }
    }

    byte[] getSaltFromDBUsernameAdmin(String DBUsername) throws SQLException {
        //Connection connection = getConnection();
        String query = "SELECT adminSalt FROM admin WHERE adminUsername= ?";
        PreparedStatement pst = DBControl.dbConnection.prepareStatement(query);
        pst.setString(1, DBUsername);
        ResultSet rs = pst.executeQuery();
        if (!rs.next()) {
            System.out.println("No salt data of username:" + DBUsername);
            byte[] emptyByte = {};
            return emptyByte;
        }
        byte[] importedSalt = rs.getBytes(1);
        return importedSalt;
    }

    byte[] generateHashedPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashedPassword = factory.generateSecret(spec).getEncoded();
        return hashedPassword;
    }

    boolean isHashedPasswordCorrectAdmin(String DBUsername, byte[] hashedPassword) throws SQLException {
        //Connection connection = getConnection();
        String query = "SELECT adminHashedPassword FROM admin WHERE adminUsername= ?";
        PreparedStatement pst = DBControl.dbConnection.prepareStatement(query);
        pst.setString(1, DBUsername);
        ResultSet rs = pst.executeQuery();
        rs.next();
        byte[] importedHashedPassword = rs.getBytes(1);
        return Arrays.equals(hashedPassword, importedHashedPassword);
    }
}
