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

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Controller class of register.fxml file to register for a new account
 * @author Hai Cao Xuan, Hoang Dinh Minh
 */
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


    /**
     * Method which makes the background image displayable
     * @author Hai Cao Xuan
     * @param url indicates url image
     * @param resourceBundle used to store texts and components that are locale sensitive
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);
    }

    /**
     * Method that switch to login scene on click
     * @author Hai Cao Xuan
     * @param event when clicking login text
     * @throws IOException when encounter an I/O exception to some sort has occurred
     */
    public void loginButtonOnAction(ActionEvent event) throws IOException {
        //Switch to log in scene
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method for check for match password before creating a new account
     * @param event when clicking register button
     * @throws SQLException that provides information on a database access error or other errors
     * @throws NoSuchAlgorithmException detect other underlying exceptions
     * @throws InvalidKeySpecException when an invalid key specification is encountered
     */
    public void registerButtonOnAction(ActionEvent event) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
//        If the password is match with the password confirmation
        if (field_password.getText().equals(field_password1.getText())) {
            registerUser();
        } else {
            label_registermessage.setText("Password does not match!");
        }
    }

    /**
     * Method to check for existing account in database before registering
     * @throws NoSuchAlgorithmException detect other underlying exception
     * @throws InvalidKeySpecException when an invalid key specification is encountered
     * @throws SQLException that provides information on a database access error or other errors
     */
    public void registerUser() throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {

        //Get User Input from Register Page
        String username = tf_username.getText();
        String password = field_password.getText();
        String email = tf_email_address.getText();

        //Generate accountUUID and Calculate salt and hashedPassword.
        UUID accountID = UUID.randomUUID();
        byte[] salt = generateSalt();
        byte[] hashedPassword = generateHashedPassword(password, salt);


        try {
            PreparedStatement stmt = DBControl.dbConnection.prepareStatement
                    ("INSERT INTO user"
                            + " (accountID, username, hashedPassword, salt, email)"
                            + " VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, accountID.toString());
            stmt.setString(2, username);
            stmt.setBytes(3, hashedPassword);
            stmt.setBytes(4, salt);
            stmt.setString(5, email);

            stmt.executeUpdate();
            System.out.println("Account created Successfully!");
            label_registermessage.setText("Account Created!");
        } catch (Exception e) {
            //Add pop up Message in Front End here!
            System.out.println("Insert Failed!");
            label_registermessage.setText("Account Creation Failed!\n Username and/or Email is already taken");
            System.out.println(e);
        }

    }

    byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    byte[] generateHashedPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashedPassword = factory.generateSecret(spec).getEncoded();
        return hashedPassword;
    }


}
