package Controller;

import Connection.DBControl;
import Models.Doctor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;


/**
 * Controller class of admin_edit_doctor_popup.fxml, to modify doctor profile
 * @Author Hoang Dinh Minh
 */
public class AdminEditDoctorPopupController implements Initializable {

    @FXML
    private ImageView image_background;

    @FXML
    private TextField tf_firstName;
    @FXML
    private TextField tf_lastName;
    @FXML
    private TextField tf_address;
    @FXML
    private TextField tf_clinicName;
    @FXML
    private TextField tf_clinicLongitude;
    @FXML
    private TextField tf_clinicLatitude;

    @FXML
    Button btn_cancel;
    @FXML
    Button btn_save;

    @FXML
    private Label lbl_errorMessage;

    private boolean isLongitudeFormatCorrect = true;
    private boolean isLatitudeFormatCorrect = true;

    private Doctor DoctorToEdit;
    final String UserUpdateStatement = "UPDATE doctor SET firstName = ?, lastName = ?, address = ?, clinicName = ?, doctorClinicLongitude = ?, doctorClinicLatitude = ? WHERE doctorID = ?";
    String invalidColorCSS = "-fx-control-inner-background: red;";
    String validColorCSS = "-fx-control-inner-background: white;";


    /**
     * Method to implement images and doctor's location
     * @param url of images
     * @param resourceBundle used to store texts and components that are locale sensitive
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);

        //Detect changes of Longitude and Latitude
        //Missing in range check (0->180 ?)
        registerLongitudeChecker();
        registerLatitudeChecker();
    }

    void registerLongitudeChecker()
    {
        tf_clinicLongitude.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Double.parseDouble(newValue);
                isLongitudeFormatCorrect = true;
                tf_clinicLongitude.setStyle(validColorCSS);
                if(!isLatitudeFormatCorrect)
                {
                    lbl_errorMessage.setText("Clinic's Latitude must be a decimal number!");
                }
                else
                {
                    lbl_errorMessage.setText("");
                    btn_save.setDisable(false);
                }
            } catch (Exception e) {
                lbl_errorMessage.setText("Clinic's Longitude must be a decimal number!");
                isLongitudeFormatCorrect = false;
                tf_clinicLongitude.setStyle(invalidColorCSS);
                btn_save.setDisable(true);
            }
        });
    }

    void registerLatitudeChecker()
    {
        tf_clinicLatitude.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Double.parseDouble(newValue);
                isLatitudeFormatCorrect = true;
                tf_clinicLatitude.setStyle(validColorCSS);
                if(!isLongitudeFormatCorrect)
                {
                    lbl_errorMessage.setText("Clinic's Longitude must be a decimal number!");
                }
                else
                {
                    lbl_errorMessage.setText("");
                    btn_save.setDisable(false);
                }
            } catch (Exception e) {
                lbl_errorMessage.setText("Clinic's Latitude must be a decimal number!");
                isLatitudeFormatCorrect = false;
                tf_clinicLatitude.setStyle(invalidColorCSS);
                btn_save.setDisable(true);
            }
        });
    }


    @FXML
    void btnCancelOnAction() {
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnSaveOnAction() throws SQLException {
        updateDoctorData();
        Stage stage = (Stage) btn_save.getScene().getWindow();
        stage.close();
    }

    private void updateDoctorData() throws SQLException {
        PreparedStatement statement = DBControl.dbConnection.prepareStatement(UserUpdateStatement);

        statement.setString(1, tf_firstName.getText().isEmpty() ? null : tf_firstName.getText());
        statement.setString(2, tf_lastName.getText().isEmpty() ? null : tf_lastName.getText());
        statement.setString(3, tf_address.getText().isEmpty() ? null : tf_address.getText());
        statement.setString(4, tf_clinicName.getText().isEmpty() ? null : tf_clinicName.getText());

        double clinicLongitude = Double.parseDouble(tf_clinicLongitude.getText());
        statement.setDouble(5, clinicLongitude);
        double clinicLatitude = Double.parseDouble(tf_clinicLatitude.getText());
        statement.setDouble(6 ,clinicLatitude);
        statement.setString(7, DoctorToEdit.getDoctorID().toString());

        statement.executeUpdate();
    }

    /**
     * Method to initialize doctor data
     * @param doctor of the application
     */
    public void initDoctorData(Doctor doctor) {
        DoctorToEdit = doctor;

        tf_firstName.setText(doctor.getFirstName() == null ? "" : doctor.getFirstName());
        tf_lastName.setText(doctor.getLastName() == null ? "" : doctor.getLastName());
        tf_address.setText(doctor.getAddress() == null ? "" : doctor.getAddress());
        tf_clinicName.setText(doctor.getClinicName() == null ? "" : doctor.getClinicName());
        tf_clinicLongitude.setText(String.valueOf(doctor.getClinicLongitude()));
        tf_clinicLatitude.setText(String.valueOf(doctor.getClinicLatitude()));
    }
}
