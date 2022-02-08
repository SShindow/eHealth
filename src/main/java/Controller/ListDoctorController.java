package Controller;

import Connection.DBControl;
import Models.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class of list_doctor.fxml, to display a moderate amount of doctor based on patent's need
 * @author  Hai Cao Xuan, Hoang Dinh Minh
 */
public class ListDoctorController implements Initializable {

    @FXML
    private TableView<Doctor> table_doctors;
    @FXML
    private TableColumn<Doctor, String> col_firstname;
    @FXML
    private TableColumn<Doctor, String> col_lastname;
    @FXML
    private TableColumn<Doctor, String> col_address;
    @FXML
    private TableColumn<Doctor, String> col_clinicname;
    @FXML
    private TableColumn<Doctor, String> col_button;

    @FXML
    private TableColumn<Doctor, UUID> col_id;

    @FXML
    private Button button_back;

    @FXML
    private ImageView image_background;


    @FXML
    public void adminDoctorTableViewOnMouseClicked(MouseEvent event) throws IOException, SQLException {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
            adminDoctorTableOnMouseDoubleClicked();
        }
    }

    private void adminDoctorTableOnMouseDoubleClicked() throws IOException, SQLException {
        //Get the doctor from the doctor table view
        var selectedDoctor = table_doctors.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            showEditDoctorDialog(selectedDoctor);
            loadDoctorFromDB();
        }
    }
    void loadDoctorFromDB() throws SQLException
    {

        doctorSearchModelObserverableList.clear();

        Statement stm = DBControl.dbConnection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT doctorID, firstName, lastName, address, clinicName\n" +
                "FROM doctor");
        while(rs.next())
        {
            String _doctorID = rs.getString(1);
            String _firstname = rs.getString(2);
            String _lastName = rs.getString(3);
            String _address = rs.getString(4);
            String _clinicName = rs.getString(5);

            doctorSearchModelObserverableList.add(
                    new Doctor(UUID.fromString(_doctorID),_firstname, _lastName, _address, _clinicName)
            );
        }

        col_id.setCellValueFactory(new PropertyValueFactory<Doctor, UUID>("doctorID"));
        col_firstname.setCellValueFactory(new PropertyValueFactory<Doctor, String>("firstName"));
        col_lastname.setCellValueFactory(new PropertyValueFactory<Doctor, String>("lastName"));
        col_address.setCellValueFactory(new PropertyValueFactory<Doctor, String>("address"));
        col_clinicname.setCellValueFactory(new PropertyValueFactory<Doctor, String>("clinicName"));


        table_doctors.setItems(doctorSearchModelObserverableList);
    }

    private void showEditDoctorDialog(Doctor selectedDoctor) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "appointment_time.fxml"
                )
        );

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(
                new Scene(loader.load())
        );

        AppointmentTimeController controller = loader.getController();
        controller.initDoctorData(selectedDoctor);

        // We want to disable the parent stage when the user edit dialog is on
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    ObservableList<Doctor> doctorSearchModelObserverableList = FXCollections.observableArrayList();

    /**
     * Method which makes the background image displayable, also initialize the table to display list of doctor
     * @param url indicates url of image and other elements
     * @param resourceBundle used to store texts and components that are locale sensitive
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Make image displayable
        File backgroundFile = new File("stuff/background.jpg");
        Image backgroundImage = new Image(backgroundFile.toURI().toString());
        image_background.setImage(backgroundImage);

        String doctorViewQuery = "SELECT firstName, lastName, address, clinicName FROM doctor";
        try {
            loadDoctorFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*
        try{
            Statement statement = DBControl.dbConnection.createStatement();
            ResultSet queryOutput = statement.executeQuery(doctorViewQuery);

            while(queryOutput.next()){
                String queryFirstName = queryOutput.getString("firstName");
                String queryLastName = queryOutput.getString("lastName");
                String queryAddress = queryOutput.getString("address");
                String queryClinicName = queryOutput.getString("clinicName");

                doctorSearchModelObserverableList.add(new Doctor(queryFirstName, queryLastName, queryAddress, queryClinicName));

                col_firstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
                col_lastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
                col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
                col_clinicname.setCellValueFactory(new PropertyValueFactory<>("clinicName"));

                table_doctors.setItems(doctorSearchModelObserverableList);
            }

        } catch (Exception e) {
            Logger.getLogger(ListDoctorController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
            e.getCause();
        }

         */
    }

    /**
     * Method to return to after login page on click
     * @param event when clicking back button
     * @throws Exception in case of encountering unexpected exception
     */
    public void cancelButtonOnAction(ActionEvent event) throws Exception  {
        //        After click, return to the after login stage
        Parent root = FXMLLoader.load(getClass().getResource("book_appointment.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
