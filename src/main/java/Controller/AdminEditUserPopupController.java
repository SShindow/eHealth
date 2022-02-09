package Controller;

import Connection.DBControl;
import Models.Doctor;
import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller class of admin_edit_user_popup.fxml, where admin can edit user profile
 * @author Hoang Dinh Minh
 */
public class AdminEditUserPopupController implements Initializable {

    @FXML
    private ImageView image_background;
    @FXML
    TextField tf_username;
    @FXML
    TextField tf_email;
    @FXML
    TextField tf_firstName;
    @FXML
    TextField tf_lastName;
    @FXML
    TextField tf_address;
    @FXML
    TextField tf_insuranceID;

    @FXML
    ChoiceBox cb_insuranceType;
    @FXML
    ChoiceBox cb_gender;
    ObservableList<String> insuranceTypeList = FXCollections.observableArrayList();
    ObservableList<String> genderList = FXCollections.observableArrayList();

    @FXML
    DatePicker dp_dateOfBirth;
    @FXML
    Button btn_clearDate;

    @FXML
    Button btn_cancel;
    @FXML
    Button btn_save;
    @FXML
    Button btn_delete;

    //Enums in User
    //User.Gender
    //User.InsuranceType

    private User UserToEdit;
    final String UserUpdateStatement = "UPDATE user SET firstName = ?, lastName = ?, address = ?, insuranceID = ?, insuranceType = ?, gender = ? ,dateOfBirth = ? WHERE username = ?";
    final String UserDeleteStatement = "DELETE FROM user WHERE username = ?";


    /**
     * Method to implement images and choice boxes
     * @param url of images
     * @param resourceBundle used to store texts and components that are locale sensitive
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);

        //Load InsuranceID and Gender to choice box
        addInsuranceTypeToChoiceBox();
        addGenderToChoiceBox();

    }

    void addInsuranceTypeToChoiceBox()
    {
        insuranceTypeList.clear();
        //Can add more type here:
        insuranceTypeList.add(User.InsuranceType.Private.toString());
        insuranceTypeList.add(User.InsuranceType.Public.toString());
        insuranceTypeList.add(null);

        cb_insuranceType.setItems(insuranceTypeList);

    }

    void addGenderToChoiceBox()
    {
        genderList.clear();
        //Can add more gender here:
        genderList.add(User.Gender.M.toString());
        genderList.add(User.Gender.F.toString());
        genderList.add(User.Gender.O.toString());
        genderList.add(null);

        cb_gender.setItems(genderList);
    }


    @FXML
    void btnClearDateOnAction()
    {
        dp_dateOfBirth.setValue(null);
    }


    @FXML
    void btnCancelOnAction()
    {
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }
    @FXML
    void btnSaveOnAction() throws SQLException {
        updateUserData();
        Stage stage = (Stage) btn_save.getScene().getWindow();
        stage.close();
    }
    @FXML
    void btnDeleteOnAction() throws SQLException {
        deleteUserData();
        Stage stage = (Stage) btn_save.getScene().getWindow();
        stage.close();
    }

    private void updateUserData() throws SQLException {
        PreparedStatement statement = DBControl.dbConnection.prepareStatement(UserUpdateStatement);

        statement.setString(1, tf_firstName.getText().isEmpty() ? null : tf_firstName.getText());
        statement.setString(2, tf_lastName.getText().isEmpty() ? null : tf_lastName.getText());
        statement.setString(3, tf_address.getText().isEmpty() ? null : tf_address.getText());
        statement.setString(4, tf_insuranceID.getText().isEmpty() ? null : tf_insuranceID.getText());

        var insuranceType = cb_insuranceType.getValue();
        if(insuranceType != null && insuranceType.equals("")) insuranceType = null;
        statement.setString(5, insuranceType == null ? null : insuranceType.toString());
        var gender = cb_gender.getValue();
        if(gender!= null && gender.equals("")) gender = null;
        statement.setString(6, gender == null ? null : gender.toString());

        statement.setDate(7, dp_dateOfBirth.getValue() == null ? null : Date.valueOf(dp_dateOfBirth.getValue()));
        statement.setString(8, UserToEdit.getUsername());

        statement.executeUpdate();
    }

    private void deleteUserData() throws SQLException {
        PreparedStatement statement = DBControl.dbConnection.prepareStatement(UserDeleteStatement);
        statement.setString(1, UserToEdit.getUsername());
        statement.executeUpdate();
    }
    /**
     * Method to initialize user data
     * @param user of the application
     */
    public void initUserData(User user) {
        UserToEdit = user;

        tf_username.setText(user.getUsername());
        tf_email.setText(user.getEmail());
        tf_firstName.setText(user.getFirstName() == null ? "" : user.getFirstName());
        tf_lastName.setText(user.getLastName() == null ? "" : user.getLastName());
        tf_address.setText(user.getAddress() == null ? "" : user.getAddress());
        tf_insuranceID.setText(user.getInsuranceID() == null ? "" : user.getInsuranceID());
        cb_insuranceType.setValue(user.getInsuranceType() == null ? "" : user.getInsuranceType());
        cb_gender.setValue(user.getGender() == null ? "" : user.getGender());
        dp_dateOfBirth.setValue(user.getDateOfBirth() == null ? null : user.getDateOfBirth().toLocalDate());


        if (user.getDateOfBirth() != null) {
            dp_dateOfBirth.setValue(user.getDateOfBirth().toLocalDate());
        }
    }

}
