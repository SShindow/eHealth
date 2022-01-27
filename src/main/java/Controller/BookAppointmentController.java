package Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookAppointmentController implements Initializable {

    @FXML
    private ChoiceBox<String> choice_symptoms;
    private String [] symptoms = {"Cardiology", "Allergy", "Oncology", "Neurology", "Orthopaedics", "Internal medicine"};
/*  Cardiology: Department for Heart problem
*   Allergy: Department for allergy problem
*   Oncology: Department for cancer diseases
*   Neurology: Department for neutral problem
*   Orthopaedics: General Department for external trauma
*   Internal medicine: General Department for internal trauma*/

    @FXML
    private ImageView image_background;

    @FXML
    private TextField tf_additionalinfo;

    @FXML
    private Spinner<Integer> spinner_distance;

    @FXML
    private Button button_search;
    @FXML
    private Button button_cancel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Make image displayable
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);
//        Implement the choice box -> add all option to the choice box
        choice_symptoms.getItems().addAll(symptoms);
//        Implement distance box
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        valueFactory.setValue(1);
        spinner_distance.setValueFactory(valueFactory);
    }

    public void searchButtonOnAction(ActionEvent event){
        String mySymptoms = choice_symptoms.getValue();
        String healthInfo = tf_additionalinfo.getText();
        Integer distance = spinner_distance.getValue();
    }

    public void cancelButtonOnAction(ActionEvent event) throws IOException {
//        After click, return to the after login stage
        Parent root = FXMLLoader.load(getClass().getResource("after_login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
