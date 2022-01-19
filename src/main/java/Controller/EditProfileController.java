package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {

    @FXML
    private Button button_save;
    @FXML
    private Button button_cancel;

    @FXML
    private RadioButton rb_male;
    @FXML
    private RadioButton rb_female;
    @FXML
    private RadioButton rb_other;
    @FXML
    private RadioButton rb_private;
    @FXML
    private RadioButton rb_public;

    @FXML
    private ImageView image_background;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Display background image
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);
//        Make Radio button use-able (Gender section)
        ToggleGroup toggleGender = new ToggleGroup();
        rb_male.setToggleGroup(toggleGender);
        rb_female.setToggleGroup(toggleGender);
        rb_other.setToggleGroup(toggleGender);
//        Make Radio button use-able (Insurance type section)
        ToggleGroup toggleType = new ToggleGroup();
        rb_private.setToggleGroup(toggleType);
        rb_public.setToggleGroup(toggleType);
//        Default setting for the radio button
        rb_male.setSelected(true);
        rb_private.setSelected(true);
    }

    public void cancelButtonOnAction(ActionEvent event) throws Exception{
//          After click, return to the after login stage
        Parent root = FXMLLoader.load(getClass().getResource("after_login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    };

    public void saveChangesButtonOnAction() throws Exception{
            String gender = ((RadioButton) toggleGender.getSelectedToggle().getText());
        String insuranceType = ((RadioButton) toggleType.getSelectedToggle().getText());
    }
}
