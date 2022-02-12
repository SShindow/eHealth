package Controller;

import Connection.DBControl;
import Location.LocationService;
import javafx.event.ActionEvent;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

/**
 * Controller of book_appointment.fxml, user select department, write short description, then select desired distance to search
 * @author Hai Cao Xuan, Hoang Dinh Minh
 */
public class BookAppointmentController implements Initializable {

    @FXML
    private ChoiceBox<String> choice_symptoms;
    //private String [] symptoms = {"Cardiology", "Allergy", "Oncology", "Neurology", "Orthopaedics", "Internal medicine"};
    ArrayList<String> symptoms = new ArrayList<String>();
    ArrayList<String> symptomsDescription = new ArrayList<String>();
    /*  Cardiology: Department for Heart problem
     *   Allergy: Department for allergy problem
     *   Oncology: Department for cancer diseases
     *   Neurology: Department for neutral problem
     *   Orthopaedics: General Department for external trauma
     *   Internal medicine: General Department for internal trauma*/

    @FXML
    private ImageView image_background;

    @FXML
    private TextArea tf_additionalinfo;

    @FXML
    private Spinner<Integer> spinner_distance;

    @FXML
    private Button button_search;
    @FXML
    private Button button_cancel;
    @FXML
    private Button button_help;
    @FXML
    private Tooltip tooltip_help;

    /**
     * Static variable for user health description
     */
    public static String moreHealthInfo;
    /**
     * Static variable for health department
     */
    public static String chosenHealthDept;
    /**
     * Static array of getting doctor ID
     */
    public static Hashtable<String, Double> suitableDoctorList;

    // For Hoang Dinh Minh's uses only!
    int setDefaultDistance = 9650;

    /**
     * Method to implement imag, choice box, and spinner
     * @param url of images and other elements
     * @param resourceBundle used to store texts and components that are locale sensitive
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Make image displayable
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);
        // Load the symptoms data From database:
        try {
            loadSymptomsDataFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        Implement the choice box -> add all option to the choice box
        choice_symptoms.getItems().addAll(symptoms);
        // Implement help (symptom explaination) tooptip:
        javafx.util.Duration duration;
        duration = javafx.util.Duration.hours(1.0);
        tooltip_help.setShowDuration(duration);
//        Implement distance box
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20000);
        valueFactory.setValue(1);
        spinner_distance.setValueFactory(valueFactory);

        //For Hoang Dinh Minh's uses only!!
        valueFactory.setValue(setDefaultDistance);

    }

    /**
     * Method to load symptom categories from database for use
     * @throws SQLException that provides information on a database access error or other errors
     */
    public void loadSymptomsDataFromDB() throws SQLException {
        Statement stm = DBControl.dbConnection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM healthdept");
        while (rs.next()) {
            symptoms.add(rs.getString(1));
            symptomsDescription.add(rs.getString(2));
        }
    }


    /**
     * Method to search for list of doctor based on user search conditions
     * @author Hoang Dinh Minh
     * @param event when clicking search button
     * @throws IOException when encounter an I/O exception to some sort has occurred
     */
    @FXML
    public void searchButtonOnAction(ActionEvent event) throws IOException {
        String chosenHealthCategory = choice_symptoms.getValue();
        String chosenAdditionalInfo = tf_additionalinfo.getText();
        Integer chosenDistance = spinner_distance.getValue();

        //Static fields to use when confirm an appointment.
        moreHealthInfo = tf_additionalinfo.getText();
        chosenHealthDept = choice_symptoms.getValue();

        //Get Suitable doctor list from Health Category and Distance of Search:
        LocationService locService = new LocationService();
        suitableDoctorList = locService.selectDoctorBasedOnDistanceAndHealthProblem(chosenHealthCategory, chosenDistance);
//        System.out.println(suitableDoctorList.size());
//        LocationService.printDoctorList(suitableDoctorList);

        //Switch to list_doctor scene

        Parent root = FXMLLoader.load(getClass().getResource("list_doctor.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to return to after login page on click
     * @param event when clicking back cancel button
     * @throws IOException when encounter an I/O exception to some sort has occurred
     */
    @FXML
    public void cancelButtonOnAction(ActionEvent event) throws IOException {
//        After click, return to the after login stage
        Parent root = FXMLLoader.load(getClass().getResource("after_login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to use function helpTooltipOnShow
     */
    @FXML
    public void helpTooltipOnShow()
    {
        helpTextShowSymptomDescription();
    }

    //For detecting when changing option, use in fxml
    //onAction="#symptomChoiceBoxOnAction"
    //Does not exist in Scene Builder.
//    public void symptomChoiceBoxOnAction()
//    {
//        helpTextShowSymptomDescription();
//    }

    /**
     * Method to display tooltip help of the symptoms
     */
    public void helpTextShowSymptomDescription() {
        String selectedSymptom = choice_symptoms.getValue();
        if (selectedSymptom == null)
            return;
        int symptomIndex = symptoms.indexOf(selectedSymptom);
        String selectedSymptomDescription = symptomsDescription.get(symptomIndex);
        tooltip_help.setText(selectedSymptomDescription);
    }

    /**
     * Method for delay before execute action
     * @param ms is the variable of millisecond
     */
    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
