package Controller;

import Appointment.Appointment;
import Connection.DBControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
//import java.sql.Connection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static Controller.AppointmentTimeController.appointmentID;

/**
 * Controller class of view_appointment.fxml, for user to view their appointment if any
 * @author Chau Truong Vinh Hoang
 */
public class ViewAppointmentController implements Initializable {

    @FXML
    private Label label_caution_show;
    @FXML
    private Label label_caution_show_final;
    @FXML
    private Button button_back;

    @FXML
    private Button button_cancel;

    @FXML
    private Button button_shift;

    @FXML
    private Text department_name_field;

    @FXML
    private Text doctor_name_field;

    @FXML
    private Text end_time_field;

    @FXML
    private Text health_category_field;

    @FXML
    private Text health_description_field;

    @FXML
    private Text patient_name_field;

    @FXML
    private Text start_date_field;

    @FXML
    private Text start_time_field;

    String invalidColorCSS = "-fx-control-inner-background: red;";
    String validColorCSS = "-fx-control-inner-background: white;";
    Appointment userAppointment = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String username= LoginController.loggedInUsername;
        try {
            userAppointment = getAppointmentFromDB(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(userAppointment == null)
        {
            button_cancel.setDisable(true);
            button_shift.setDisable(true);
            return;
        }

            if(userAppointment.getPatientID()==""){
                label_caution_show.setText("You haven't book any appointment yet");
                label_caution_show.setTextFill(Color.RED);
                return;
            }


        String patientName = null;
        Statement stmt = null;
        try {
            stmt = DBControl.dbConnection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "select firstName, lastName from user where accountID='"+userAppointment.getPatientID()+"'";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                patientName = rs.getString(1) +" "+ rs.getString(2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String doctorName = null;
        String clinicName = null;
        try {
            Statement stmt2 = DBControl.dbConnection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql2 = "select firstName, lastName, clinicName from doctor where doctorID='"+userAppointment.getDoctorID()+"'";
        ResultSet rs2 = null;
        try {
            rs2 = stmt.executeQuery(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                if (!rs2.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                doctorName = rs2.getString(1) + " "+rs2.getString(2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                clinicName = rs2.getString(3);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        patient_name_field.setText(patientName);
        doctor_name_field.setText(doctorName);
        department_name_field.setText(clinicName);
        health_category_field.setText(userAppointment.getHealthDeptName());
        health_description_field.setText(userAppointment.getHealthDescription());
        start_date_field.setText(userAppointment.getSessionStartDate());
        start_time_field.setText(userAppointment.getSessionStartTime());
        end_time_field.setText(userAppointment.getSessionEndTime());

    }
    /**
     * Method which allow user to get back to after login scene on click
     * @param event when clicking on session time button
     * @throws IOException handle user input output error
     */

    @FXML
    void backButtonOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("after_login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    private Appointment getAppointmentFromDB(String username) throws SQLException {
        String sql = "SELECT * FROM appointment a "
                + "INNER JOIN user u ON a.patientID = u.accountID "
                + "WHERE u.username = ?";
        PreparedStatement stmt = DBControl.dbConnection.prepareStatement(sql);

        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (!rs.isBeforeFirst() ) {
            System.out.println("No appointment");
            return null;
        }

        rs.next();
        String appointmentID = rs.getString(1);
        String patientID = rs.getString(2);
        String doctorID = rs.getString(3);
        String healthDeptName = rs.getString(4);
        String healthDescription = rs.getString(5);
        var sessionDate = rs.getDate(6);
        var startTime = rs.getTime(7);
        var endTime = rs.getTime(8);

        Appointment appointment = new Appointment(appointmentID, patientID, doctorID, healthDeptName, healthDescription,
                sessionDate.toString(), startTime.toString(), endTime.toString());

        button_cancel.setDisable(false);

        return appointment;
    }
    /**
     * Method to show user appointment if any
     * @param event when clicking on Show Appointment button
     * @throws SQLException catch SQL error if any
     * */
    @FXML
    void showButtonOnAction(ActionEvent event) throws SQLException {
        String username= LoginController.loggedInUsername;
        Appointment userAppointment = getAppointmentFromDB(username);
        if(userAppointment.getPatientID()==""){
            label_caution_show.setText("You haven't book any appointment yet");
            label_caution_show.setTextFill(Color.RED);
            return;
        }
        String patientName = null;
        Statement stmt = DBControl.dbConnection.createStatement();
        String sql = "select firstName, lastName from user where accountID='"+userAppointment.getPatientID()+"'";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            patientName = rs.getString(1) +" "+ rs.getString(2);
        }

        String doctorName = null;
        String clinicName = null;
        Statement stmt2 = DBControl.dbConnection.createStatement();
        String sql2 = "select firstName, lastName, clinicName from doctor where doctorID='"+userAppointment.getDoctorID()+"'";
        ResultSet rs2 = stmt.executeQuery(sql2);
        while(rs2.next()){
            doctorName = rs2.getString(1) + " "+rs2.getString(2);
            clinicName = rs2.getString(3);
        }

        patient_name_field.setText(patientName);
        doctor_name_field.setText(doctorName);
        department_name_field.setText(clinicName);
        health_category_field.setText(userAppointment.getHealthDeptName());
        health_description_field.setText(userAppointment.getHealthDescription());
        start_date_field.setText(userAppointment.getSessionStartDate());
        start_time_field.setText(userAppointment.getSessionStartTime());
        end_time_field.setText(userAppointment.getSessionEndTime());
    }
    /**
     * Method to delete user appointment on database
     * @param username specify the username
     * @throws SQLException catch SQL error if any
     * */
    private void deleteAppointmentFromDatabase(String username) throws SQLException {
        //SET SQL_SAFE_UPDATES = 0;
        //delete from appointment where patientID in
        //(select accountID from user
        //where username ='chautruong12345');
        //SET SQL_SAFE_UPDATES =1;

//        DELETE P
//        FROM Product P
//        LEFT JOIN OrderItem I ON P.Id = I.ProductId
//        WHERE I.Id IS NULL

        String sql = "DELETE a FROM appointment a "
            + "INNER JOIN user u ON a.patientID = u.accountID "
            + "WHERE u.username = ?";
        PreparedStatement stmt = DBControl.dbConnection.prepareStatement(sql);

        stmt.setString(1, username);
        try
        {
            stmt.execute();
        }catch(SQLException e)
        {
            System.out.println(e);
        }

    }


    /**
     * Method to allow user to cancel their appointment
     * @param event when clicking on Cancel button
     * */
    @FXML
    void cancelButtonOnAction(ActionEvent event){
        String username= LoginController.loggedInUsername;
        SQLException exception=null;
        try {
            deleteAppointmentFromDatabase(username);
            patient_name_field.setText("");
            doctor_name_field.setText("");
            department_name_field.setText("");
            health_category_field.setText("");
            health_description_field.setText("");
            start_date_field.setText("");
            start_time_field.setText("");
            end_time_field.setText("");
        } catch (SQLException e) {
            exception=e;
            e.printStackTrace();
            label_caution_show_final.setText("You don't have any appointment");
            label_caution_show_final.setTextFill(Color.RED);
            return;
        }

        if(exception==null){
            label_caution_show_final.setText("You have deleted your appointment");
            label_caution_show_final.setTextFill(Color.GREEN);
            button_cancel.setDisable(true);
            button_shift.setDisable(true);
            return;
        }
    }
    /**
     * Method allow user to switch on shift appointment scene
     * @param event when clicking on Shift Appointment button
     * @throws IOException catch user interaction error if any
     * */
    @FXML
    void shiftButtonOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("shift_appointment.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
