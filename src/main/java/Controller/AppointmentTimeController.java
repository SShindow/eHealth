package Controller;

import Appointment.PDFAppointmentControl;
import Connection.DBControl;
import Email.EmailControl;
import Models.Doctor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
/**
 * Controller class of appointment_time.fxml, for user to set up their session appointment time
 * @author Chau Truong Vinh Hoang
 */
public class AppointmentTimeController implements Initializable {

    @FXML
    private Button back_button;

    @FXML
    private Button button_apply;

    @FXML
    private DatePicker calendar_field;

    @FXML
    private Button check_available_button;

    @FXML
    private Label label_caution_message;

    @FXML
    private Label label_caution_message_2;
    @FXML
    private SplitMenuButton reminder_time_selection;

    @FXML
    private SplitMenuButton session_selection;


    private MenuItem s1 = new MenuItem("From 8:00 to 9:00");
    private MenuItem s2 = new MenuItem("From 9:30 to 10:30");
    private MenuItem s3 = new MenuItem("From 11:00 to 12:00");
    private MenuItem s4 = new MenuItem("From 13:00 to 14:00");
    private MenuItem s5 = new MenuItem("From 14:30 to 15:00");
    private String userChoosenSession="";
    private String sessionStartTime="";
    private String sessionEndTime ="";

    private MenuItem r1 = new MenuItem("After  1 min");
    private MenuItem r2 = new MenuItem("After  2 min");
    private MenuItem r3 = new MenuItem("Before 1 week");
    private MenuItem r4 = new MenuItem("Before 3 days");
    private MenuItem r5 = new MenuItem("Before 1 hours");
    private MenuItem r6 = new MenuItem("Before 10 min");
    private String userChoosenReminder="";
    private String reminder_time="";

    /**
     * Default set for the appointment ID
     */
    public static String appointmentID=null;
    /**
     * static method for selected doctor
     */
    public static Doctor selectedDoctor;

    /**
     * Method to check if the startdate is valid
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpSessionSelections();
    }

    public boolean isStartDateValid(){
        Calendar now = Calendar.getInstance();

        if(String.valueOf(calendar_field.getValue())==null) return false;

        int currentYear = now.get(Calendar.YEAR);
        System.out.println("Current Year:"+currentYear);
        int selectedYear= Integer.parseInt(String.valueOf(calendar_field.getValue().getYear()));
        System.out.println("selected Year:"+selectedYear);
        if(currentYear-selectedYear<0) {
            System.out.println("return in year");
            return false;
        }
        int currentMonth = now.get(Calendar.MONTH)+1;
        System.out.println("Current month:"+currentMonth);
        int selectedMonth = calendar_field.getValue().getMonthValue();
        System.out.println("selected month:"+selectedMonth);
        if(currentMonth-selectedMonth<0 && currentYear==selectedYear) {
            System.out.println("Return in second condition");
            return false;
        }
        int currentDay = now.get(Calendar.DATE);
        System.out.println("Current day:"+currentDay);
        int selectedDay = Integer.parseInt(String.valueOf(calendar_field.getValue().getDayOfMonth()));
        System.out.println("selected day:"+selectedDay);
        if(currentDay-selectedDay<0 && currentMonth==selectedMonth && currentYear == selectedYear){
            System.out.println("Return in third condition");
            return true;
        }
        return false;
    }
    /**
     * Method which allow user to select session time on click
     * @param event when clicking on session time button
     */
    @FXML
    void selectSessionOnAction(MouseEvent event) {
    }

    private void setUpSessionSelections() {
        session_selection.getItems().add(s1);
        session_selection.getItems().add(s2);
        session_selection.getItems().add(s3);
        session_selection.getItems().add(s4);
        session_selection.getItems().add(s5);

        for (final MenuItem item : session_selection.getItems()) {
            item.setOnAction((event) -> {
                onSessionSelected(event, item);
            });
        }
    }

    private void onSessionSelected(ActionEvent actionEvent, MenuItem menuItem) {
            //System.out.println("User choose:"+userChoosenSession);
            session_selection.setText(menuItem.getText());
            userChoosenSession = menuItem.getText();

            int Sfrom =userChoosenSession.indexOf("From ")+"From ".length();
            int Sto  = userChoosenSession.indexOf("to");
            sessionStartTime = userChoosenSession.substring(Sfrom,Sto-1);
            //System.out.println("start time:"+sessionStartTime);
            //get end time session
            int Efrom = userChoosenSession.indexOf("to ")+"to ".length();
            int Eto = userChoosenSession.length();
            sessionEndTime = userChoosenSession.substring(Efrom,Eto);
            //System.out.println("end time:"+sessionEndTime);
    }

    /**
     * Method to set reminder on action
     * @param event when interact with the reminder box
     */
    public void selectReminderOnAction(ActionEvent event) {
        reminder_time_selection.getItems().add(r1);
        reminder_time_selection.getItems().add(r2);
        reminder_time_selection.getItems().add(r3);
        reminder_time_selection.getItems().add(r4);
        reminder_time_selection.getItems().add(r5);
        reminder_time_selection.getItems().add(r6);

        r1.setOnAction(setUpEventReminder(r1));
        r2.setOnAction(setUpEventReminder(r2));
    }
    //Handling event for send email remind user after 1 min.
    private EventHandler<ActionEvent> setUpEventReminder (MenuItem r){
        EventHandler<ActionEvent> myEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                reminder_time_selection.setText(r.getText());
                userChoosenReminder = r.getText();
                reminder_time = userChoosenReminder.substring(7, userChoosenReminder.length());
                System.out.println("User reminder time:"+reminder_time);
            }
        };
        return myEvent;
    }
    /**
     * Method to allow user apply the update information of appointment on click
     * @param event when clicking on apply button
     * */
    public void applyButtonOnAction(ActionEvent event) {
        /* Uncomment for testing
        System.out.println("User choose final:"+userChoosenSession);
        System.out.println("start time final:"+sessionStartTime);
        System.out.println("end time final:"+sessionEndTime);
         */
        //System.out.println("User reminder time final:"+reminder_time);

        appointmentID = ForgotPasswordController.generateRandomString(8);
        String userID = "";
        try {
            userID = getAccountID(LoginController.loggedInUsername);
            System.out.println(userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String userEmail ="";

        String doctorID= String.valueOf(selectedDoctor.getDoctorID());
        String healthDeptName = BookAppointmentController.chosenHealthDept;
        String healthDescription= BookAppointmentController.moreHealthInfo;
        String sessionDate = String.valueOf(calendar_field.getValue());
        String sessionStartAt = sessionStartTime;
        String sessionEndAt =sessionEndTime;
        System.out.println("Session date:"+sessionDate);

        String sql = "insert into appointment values('"+appointmentID+"'"+
                ",'"+userID+"','"+doctorID+"','"+healthDeptName+"','"+healthDescription+"','"+
                sessionDate+"','"+sessionStartTime+"','"+sessionEndTime+"')";
        //System.out.println(sql);
        SQLException exception=null;
        try {
            addAppointmentToDatabase(sql);
        } catch (SQLException e) {
            exception=e;
            System.out.println("User enter wrong field");

            label_caution_message_2.setText("Please check available and set up all requirements!");
            label_caution_message_2.setTextFill(Color.RED);
        }
        System.out.println(exception);
        if (exception==null) {
            System.out.println("User create appointment!");
            label_caution_message_2.setText("Your appointment is created successfully! Back to main menu");
            label_caution_message_2.setTextFill(Color.GREEN);
            try {
                PDFAppointmentControl.createPDFAppointment(appointmentID);
                EmailControl.sendAppointmentCreateSuccessfully(LoginController.loggedInUsername);

                if(reminder_time.contains("1 min")==true){
                    System.out.println("Reminder time contains 1 min");
                    EmailControl.sendMailReminder(LoginController.loggedInUsername, 60);
                }
                if(reminder_time.contains("2 min")==true){
                    EmailControl.sendMailReminder(LoginController.loggedInUsername, 120);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }


        }
    }
    private static String getUserEmail(String username) throws SQLException {
        String sql ="select email from user where username='"+username+"'";
        Statement stmt = DBControl.dbConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String email="";
        while(rs.next()){
            email = rs.getString(1);
        }
        return email;
    }
    private void addAppointmentToDatabase(String sql) throws SQLException {
        Statement stmt = DBControl.dbConnection.createStatement();
        int rs = stmt.executeUpdate(sql);
    }
    private String getAccountID(String username) throws SQLException {
        String sql ="select accountID from user where username='"+username+"'";
        Statement stmt = DBControl.dbConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String accountID="";
        while(rs.next()){
            accountID = rs.getString(1);
        }
        return accountID;
    }
    /**
     * Method to switch user to list doctor scene on click
     * @param event when clicking on Back button
     * @throws IOException when encounter an I/O exception to some sort has occurred
     * */
    public void backButtonOnAction(ActionEvent event) throws IOException {
        Stage stage1 = (Stage) back_button.getScene().getWindow();
        stage1.close();
    }

    /**
     * Method to notify if user enter a valid date or if their desired doctor is available on that date
     * @param event when clicking on Check Available button
     * @throws SQLException that provides information on a database access error or other errors
     * */
    public void checkAvailableButtonOnAction(ActionEvent event) throws SQLException {
        //check if user booking a valid date
        if(isStartDateValid()==false){
            label_caution_message.setText("Your setting date is not valid!!!!!");
            label_caution_message.setTextFill(Color.RED);
            return;
        }
        //get doctor available time

        String choosenDoctorID = String.valueOf(selectedDoctor.getDoctorID());
        String userPickingDate = String.valueOf(calendar_field.getValue());
        // startTimeList will return List of start time which selected doctor is busy on given date
        ArrayList<String> startTimeList = showStartTimeWhichDoctorisBusy(userPickingDate, choosenDoctorID);
        String rangeOfTime = getRangeOfTimeFromList(startTimeList);
        System.out.println("range of time value:"+rangeOfTime);
        if(rangeOfTime==""){
            label_caution_message.setText("Your choosen doctor is available on this date");
            label_caution_message.setTextFill(Color.GREEN);
        } else {
            // getRangeOfTimeFromList will return String rangeOFTime like: "8:00-9:00, 10:00-11:00"
            //String rangeOfTime = getRangeOfTimeFromList(startTimeList);
            String caution = "Your choosen doctor isn't available" +
                    " for booking in: " + rangeOfTime;
            System.out.println(caution);
            label_caution_message.setText(caution);
            label_caution_message.setTextFill(Color.RED);
        }


    }
    private static ArrayList<String> showStartTimeWhichDoctorisBusy(String userPickingDate, String choosenDoctorID) throws SQLException {
        String sql="select startTime from appointment where doctorID = '"+choosenDoctorID+"' and sessionDate='"+userPickingDate+"'";
        Statement stmt = DBControl.dbConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<String> startTimeList = new ArrayList<String>();
        while(rs.next()){
            startTimeList.add(rs.getString(1));
        }
        return startTimeList;
    }
    private static String addTimeReturnString(String inputTime, int amount) {
        java.sql.Time myTime = java.sql.Time.valueOf(inputTime);
        LocalTime localtime = myTime.toLocalTime();
        localtime = localtime.plusHours(amount);
        String outputTime = localtime.toString();
        return outputTime;
    }
    private static String getRangeOfTimeFromList(ArrayList<String> startTimeList){
        String rangeOfTime ="";
        for(int i=0; i<startTimeList.size();i++) {
            Time startTimeSQL = java.sql.Time.valueOf(startTimeList.get(i));
            String startTimeSQLFormat = startTimeSQL.toString();
            String endTime = addTimeReturnString(startTimeSQLFormat, 1);
            String startTime = addTimeReturnString(startTimeSQLFormat,0);
            rangeOfTime = rangeOfTime + startTime + "-" + endTime;
            if(i!=startTimeList.size()-1){
                rangeOfTime = rangeOfTime+", ";
            }
        }
        return rangeOfTime;
    }


    /**
     * Method to initialize selected doctor data
     * @param doctor object doctor
     */
    public void initDoctorData(Doctor doctor) {
        selectedDoctor = doctor;
        System.out.println("Doctor ID:"+selectedDoctor.getDoctorID());
        String healthDescription= BookAppointmentController.moreHealthInfo;
        String healthDept = BookAppointmentController.chosenHealthDept;
        System.out.println(healthDept+" "+healthDescription);
    }


}
