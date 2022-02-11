package Controller;

import Appointment.Appointment;
import Connection.DBControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewAppointmentController {

    @FXML
    private Label label_caution_show;
    @FXML
    private Label label_caution_show_final;
    @FXML
    private Button button_back;

    @FXML
    private Button button_cancel;

    @FXML
    private Button button_show;
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

    @FXML
    void backButtonOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("after_login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    private static Appointment getAppointment(String username) throws SQLException {
        Statement stmt = DBControl.connectToDatabaseWithReturnConnection().createStatement();
        //select * from appointment where patientID in
        //(select accountID from user
        //where username ='chautruong12345');
        String sql = "select * from appointment where patientID in (select accountID from user "+
                "where username ='"+username+"');";
        ResultSet rs = stmt.executeQuery(sql);
        Appointment appointment = new Appointment();
        while(rs.next()){
            appointment.setPatientID(rs.getString(2));
            appointment.setDoctorID(rs.getString(3));
            appointment.setHealthDeptName(rs.getString(4));
            appointment.setHealthDescription(rs.getString(5));
            appointment.setSessionStartDate(rs.getString(6));
            appointment.setSessionStartTime(rs.getString(7));
            appointment.setSessionEndTime(rs.getString(8));
        }
        return appointment;
    }

    @FXML
    void showButtonOnAction(ActionEvent event) throws SQLException {
        String username= LoginController.loggedInUsername;
        Appointment userAppointment = getAppointment(username);
        if(userAppointment.getPatientID()==""){
            label_caution_show.setText("You haven't book any appointment yet");
            label_caution_show.setTextFill(Color.RED);
            return;
        }
        String patientName = null;
        Statement stmt = DBControl.connectToDatabaseWithReturnConnection().createStatement();
        String sql = "select firstName, lastName from user where accountID='"+userAppointment.getPatientID()+"'";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            patientName = rs.getString(1) +" "+ rs.getString(2);
        }

        String doctorName = null;
        String clinicName = null;
        Statement stmt2 = DBControl.connectToDatabaseWithReturnConnection().createStatement();
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

    private void deleteAppointmentFromDatabase(String username) throws SQLException {
        //SET SQL_SAFE_UPDATES = 0;
        //delete from appointment where patientID in
        //(select accountID from user
        //where username ='chautruong12345');
        //SET SQL_SAFE_UPDATES =1;
        String sql = "SET SQL_SAFE_UPDATES = 0;"+
                     "delete from appointment where patientID in"+
                     "(select accountID from user where username ='"+username+"');SET SQL_SAFE_UPDATES =1;";
        Statement stmt = DBControl.connectToDatabaseWithReturnConnection().createStatement();
        int rs = stmt.executeUpdate(sql);
    }
    @FXML
    void cancelButtonOnAction(ActionEvent event){
        String username= LoginController.loggedInUsername;
        SQLException exception=null;
        try {

            deleteAppointmentFromDatabase(username);
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
            return;
        }
    }

    @FXML
    void shiftButtonOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("shift_appointment.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
