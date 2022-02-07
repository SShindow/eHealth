package Controller;

import Connection.DBControl;
import Models.Doctor;
import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Controller class of admin_edit.fxml, after logging in successfully, admin can view list of user and doctor information
 * @Author Hoang Dinh Minh
 */
public class AdminEditController implements Initializable {
    @FXML
    ImageView image_background;

    @FXML
    TableView<User> userTableView;
    @FXML
    TableColumn<User, String> usernameCol;
    @FXML
    TableColumn<User, String> emailCol;
    @FXML
    TableColumn<User, String> firstNameCol;
    @FXML
    TableColumn<User, String> lastNameCol;
    @FXML
    TableColumn<User, String> addressCol;
    @FXML
    TableColumn<User, String> insuranceIDCol;
    @FXML
    TableColumn<User, String> insuranceTypeCol;
    @FXML
    TableColumn<User, String> genderCol;
    @FXML
    TableColumn<User, Date> dateOfBirthCol;

    @FXML
    TableView<Doctor> doctorTableView;
    @FXML
    TableColumn<Doctor, UUID> doctorIDCol;
    @FXML
    TableColumn<Doctor, String> doctorFirstNameCol;
    @FXML
    TableColumn<Doctor, String> doctorLastNameCol;
    @FXML
    TableColumn<Doctor, String> doctorAddressCol;
    @FXML
    TableColumn<Doctor, String> clinicNameCol;
    @FXML
    TableColumn<Doctor, String> clinicLongitudeCol;
    @FXML
    TableColumn<Doctor, String> clinicLatitudeCol;

    @FXML
    ScrollPane userScrollPane;
    @FXML
    ScrollPane doctorScrollPane;

    @FXML
    Button btn_cancel;

    final java.sql.Date DEFAULT_DATE = java.sql.Date.valueOf("1900-01-31");

    ObservableList<User> userObserverableList = FXCollections.observableArrayList();
    ObservableList<Doctor> doctorObservableList = FXCollections.observableArrayList();

    /**
     * Method to implement images and other elements
     * @param url of images
     * @param resourceBundle used to store texts and components that are locale sensitive
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);

        try {
            loadDataFromDB();
        } catch (SQLException e) {
            System.out.println(e);
        }

        userScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        doctorScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    }

    @FXML
    void btnCancelOnAction(ActionEvent event) throws IOException {
        //Switch to admin log in scene
        Parent root = FXMLLoader.load(getClass().getResource("admin_login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    void loadDataFromDB() throws SQLException {
        loadUserFromDB();
        loadDoctorFromDB();
    }

    //Load User information to User table
    void loadUserFromDB() throws SQLException {
        userObserverableList.clear();

        Statement stm = DBControl.dbConnection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT username, email, firstName, lastName, address, insuranceID, insuranceType, gender, dateOfBirth\n" +
                "FROM user");
        while (rs.next()) {
            String _username = rs.getString(1);
            String _email = rs.getString(2);
            String _firstName = "";
            String _lastName = "";
            String _address = "";
            String _insuranceID = "";
            String _insuranceType = "";
            String _gender = "";
            java.sql.Date _dateOfBirth = null;

            try {
                _firstName = rs.getString(3);
                _lastName = rs.getString(4);
                _address = rs.getString(5);
                _insuranceID = rs.getString(6);
                _insuranceType = rs.getString(7);
                _gender = rs.getString(8);
                _dateOfBirth = rs.getDate(9);
            } catch (Exception e) {
                System.out.println(e);
            }

//            if(_dateOfBirth == null)
//                _dateOfBirth = DEFAULT_DATE;

            //Populate the observable list
            userObserverableList.add(
                    new User(_username, _email, _firstName, _lastName, _address,
                            _insuranceID, _insuranceType, _gender, _dateOfBirth));

        }

        //aColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        usernameCol.setCellValueFactory(new PropertyValueFactory<User, String>("username"));

        emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<User, String>("address"));
        insuranceIDCol.setCellValueFactory(new PropertyValueFactory<User, String>("insuranceID"));
        insuranceTypeCol.setCellValueFactory(new PropertyValueFactory<User, String>("insuranceType"));
        genderCol.setCellValueFactory(new PropertyValueFactory<User, String>("gender"));
        dateOfBirthCol.setCellValueFactory(new PropertyValueFactory<User, java.sql.Date>("dateOfBirth"));

        userTableView.setItems(userObserverableList);

    }

    void loadDoctorFromDB() throws SQLException
    {
        doctorObservableList.clear();

        Statement stm = DBControl.dbConnection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT doctorID, firstName, lastName, address, clinicName, doctorClinicLongitude, doctorClinicLatitude\n" +
                "FROM doctor");
        while(rs.next())
        {
            String _doctorID = rs.getString(1);
            String _firstname = rs.getString(2);
            String _lastName = rs.getString(3);
            String _address = rs.getString(4);
            String _clinicName = rs.getString(5);
            double _clinicLongitude = rs.getDouble(6);
            double _clinicLatitude = rs.getDouble(7);

            doctorObservableList.add(
                    new Doctor(UUID.fromString(_doctorID),_firstname, _lastName, _address, _clinicName,
                    _clinicLongitude, _clinicLatitude)
            );
        }

        doctorIDCol.setCellValueFactory(new PropertyValueFactory<Doctor, UUID>("doctorID"));
        doctorFirstNameCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("firstName"));
        doctorLastNameCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("lastName"));
        doctorAddressCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("address"));
        clinicNameCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("clinicName"));
        clinicLongitudeCol.setCellValueFactory(new PropertyValueFactory<>("clinicLongitude"));
        clinicLatitudeCol.setCellValueFactory(new PropertyValueFactory<>("clinicLatitude"));

        doctorTableView.setItems(doctorObservableList);
    }

    @FXML
    public void adminUserTableViewOnMouseClicked(MouseEvent event) throws IOException, SQLException {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
            adminUserTableOnMouseDoubleClicked();
        }
    }

    private void adminUserTableOnMouseDoubleClicked() throws IOException, SQLException {
        //Get the user from the user table view
        var selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            showEditUserDialog(selectedUser);
            loadUserFromDB();
        }
    }

    @FXML
    public void adminDoctorTableViewOnMouseClicked(MouseEvent event) throws IOException, SQLException {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
            adminDoctorTableOnMouseDoubleClicked();
        }
    }

    private void adminDoctorTableOnMouseDoubleClicked() throws IOException, SQLException {
        //Get the doctor from the doctor table view
        var selectedDoctor = doctorTableView.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            showEditDoctorDialog(selectedDoctor);
            loadDoctorFromDB();
        }
    }

    private void showEditUserDialog(User selectedUser) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "admin_edit_user_popup.fxml"
                )
        );

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(
                new Scene(loader.load())
        );

        AdminEditUserPopupController controller = loader.getController();
        controller.initUserData(selectedUser);

        // We want to disable the parent stage when the user edit dialog is on
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private void showEditDoctorDialog(Doctor selectedDoctor) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "admin_edit_doctor_popup.fxml"
                )
        );

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(
                new Scene(loader.load())
        );

        AdminEditDoctorPopupController controller = loader.getController();
        controller.initDoctorData(selectedDoctor);

        // We want to disable the parent stage when the user edit dialog is on
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
