package Controller;

import Appointment.PDFAppointmentControl;
import Connection.DBControl;
import Models.Doctor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AppointmentTimeController {

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

    Doctor selectedDoctor;

    private boolean isStartDateValid(){
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
        //int selectedMonth = Integer.parseInt(String.valueOf(calendar_field.getMonth()));
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

    // add button handling menu button
    MenuItem s1 = new MenuItem("From 8:00 to 9:00");
    MenuItem s2 = new MenuItem("From 9:30 to 10:30");
    MenuItem s3 = new MenuItem("From 11:00 to 12:00");
    MenuItem s4 = new MenuItem("From 13:00 to 14:00");
    MenuItem s5 = new MenuItem("From 14:30 to 15:00");
    private String userChoosenSession="";
    private String sessionStartTime="";
    private String sessionEndTime ="";
    @FXML
    void selectSessionOnAction(MouseEvent event) {
        //MenuItem s1 = new MenuItem("From 8:00 to 9:00");
        session_selection.getItems().add(s1);
        session_selection.getItems().add(s2);
        session_selection.getItems().add(s3);
        session_selection.getItems().add(s4);
        session_selection.getItems().add(s5);

        s1.setOnAction(setUpEventSession(s1));
        s2.setOnAction(setUpEventSession(s2));
        s3.setOnAction(setUpEventSession(s3));
        s4.setOnAction(setUpEventSession(s4));
        s5.setOnAction(setUpEventSession(s5));
        System.out.println("User choose 2:"+userChoosenSession);
    }

    private EventHandler<ActionEvent> setUpEventSession (MenuItem s){
        EventHandler<ActionEvent> myEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                session_selection.setText(s.getText());
                userChoosenSession = s.getText();
                //System.out.println("User choose:"+userChoosenSession);
                //get start time session
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
        };
        return myEvent;
    }
    MenuItem r1 = new MenuItem("After  1 min");
    MenuItem r2 = new MenuItem("After  2 min");
    MenuItem r3 = new MenuItem("Before 1 week");
    MenuItem r4 = new MenuItem("Before 3 days");
    MenuItem r5 = new MenuItem("Before 1 hours");
    MenuItem r6 = new MenuItem("Before 10 min");
    private String userChoosenReminder="";
    public String reminder_time="";
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
    public void applyButtonOnAction(ActionEvent event) {
        /* For testing
        System.out.println("User choose final:"+userChoosenSession);
        System.out.println("start time final:"+sessionStartTime);
        System.out.println("end time final:"+sessionEndTime);
        System.out.println("User reminder time final:"+reminder_time);
         */
        String appointmentID = ForgotPasswordController.generateRandomString(8);
        String userID = "";
        try {
            userID = getAccountID("chautruong1234");
            System.out.println(userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String userEmail ="";

        String doctorID= "01229eb6-0c64-4957-bda0-372292460107";
        String healthDeptName="Internal medicine";
        String healthDescription = "I am not feeling good";
        String sessionDate = String.valueOf(calendar_field.getValue());
        String sessionStartAt = sessionStartTime;
        String sessionEndAt =sessionEndTime;
        System.out.println("Session date:"+sessionDate);
        // insert into appointment values(appointmentID, userID, doctorID, healthDeptName, healthDescription, sessionDate, sessionStartTime, sessionEndTime);
        String sql = "insert into appointment values('"+appointmentID+"'"+
                ",'"+userID+"','"+doctorID+"','"+healthDeptName+"','"+healthDescription+"','"+
                sessionDate+"','"+sessionStartTime+"','"+sessionEndTime+"')";
        System.out.println(sql);
        SQLException exception=null;
        try {
            addAppointment2Database(sql);
        } catch (SQLException e) {
            exception=e;
            System.out.println("User enter wrong field");

            label_caution_message_2.setText("Please check available and set up all requirements!");
            label_caution_message_2.setTextFill(Color.RED);
        }
        System.out.println(exception);
        if (exception==null) {
            System.out.println("User create appointment");
            //if(reminder_time!=null && reminder_time)

            try {
                userEmail=getUserEmail("chautruong1234");
                System.out.println("userEmail:"+userEmail);
                PDFAppointmentControl.createPDFAppointment(appointmentID);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    private String getUserEmail(String username) throws SQLException {
        String sql ="select email from user where username='"+username+"'";
        Statement stmt = DBControl.connectToDatabaseWithReturnConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String email="";
        while(rs.next()){
            email = rs.getString(1);
        }
        return email;
    }
    private void addAppointment2Database(String sql) throws SQLException {
        Statement stmt = DBControl.connectToDatabaseWithReturnConnection().createStatement();
        int rs = stmt.executeUpdate(sql);
    }
    private String getAccountID(String username) throws SQLException {
        String sql ="select accountID from user where username='"+username+"'";
        Statement stmt = DBControl.connectToDatabaseWithReturnConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String accountID="";
        while(rs.next()){
            accountID = rs.getString(1);
        }
        return accountID;
    }
    /**
     * Hello
     *
     * */
    public void backButtonOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("list_doctor.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //this button will check if user booking a valid date (not date in the past)
    // and show available time of doctor in that day
    public void checkAvailableButtonOnAction(ActionEvent event) throws SQLException {
        //check if user booking a valid date
        if(isStartDateValid()==false){
            label_caution_message.setText("Your setting date is not valid!!!!!");
            label_caution_message.setTextFill(Color.RED);
            return;
        }
        System.out.println("Check Available button is passed the 1st cond!");
        //get doctor available time
        //String choosenDoctorID = getChoosenDoctorID();

        String choosenDoctorID = "01229eb6-0c64-4957-bda0-372292460107"; //change accordingly in the doctor-list scene
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
    public static ArrayList<String> showStartTimeWhichDoctorisBusy(String userPickingDate, String choosenDoctorID) throws SQLException {
        //"select startTime from appointment where doctorID = '"+choosenDoctorID+"' and sessionDate = '"+userPickingDate';
        String sql="select startTime from appointment where doctorID = '"+choosenDoctorID+"' and sessionDate='"+userPickingDate+"'";
        //System.out.println(sql);
        Statement stmt = DBControl.connectToDatabaseWithReturnConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<String> startTimeList = new ArrayList<String>();
        while(rs.next()){
            startTimeList.add(rs.getString(1));
        }
        return startTimeList;
    }
    public static String addTimeReturnString(String inputTime, int amount) {
        java.sql.Time myTime = java.sql.Time.valueOf(inputTime);
        LocalTime localtime = myTime.toLocalTime();
        localtime = localtime.plusHours(amount);
        String outputTime = localtime.toString();
        return outputTime;
    }
    public static String getRangeOfTimeFromList(ArrayList<String> startTimeList){
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
    @FXML
    void startDateFieldOnAction(ActionEvent event) {

    }

    public void initDoctorData(Doctor doctor) {
        selectedDoctor = doctor;
        System.out.println("Doctor ID:"+selectedDoctor.getDoctorID());
        String healthDescription= BookAppointmentController.moreHealthInfo;
        String healthDept = BookAppointmentController.chosenHealthDept;
        System.out.println(healthDept+" "+healthDescription);
    }


}
