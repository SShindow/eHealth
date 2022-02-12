/**
 * Class to construct an appointment with the
 * @author Chau Truong Vinh Hoang
 */

package Appointment;

/**
 * General class for appointment
 * @author Chau Truong Vinh Hoang
 */
public class Appointment {
    private String appointmentID;
    private String patientID;
    private String doctorID;
    private String healthDeptName;
    private String healthDescription;
    private String sessionStartDate;
    private String sessionStartTime;
    private String sessionEndTime;

    /**
     * Null constructor
     */
    public Appointment(){}

    /**
     * Method to construct appointment
     * @param appointmentID ID of the appointment generated automatically
     * @param patientID ID of the patients (users)
     * @param doctorID ID of the doctor
     * @param healthDeptName Department of the doctor
     * @param healthDescription short description about health status of user
     * @param sessionStartDate date of the appointment
     * @param sessionStartTime start time of the appointment
     * @param sessionEndTime end time of the appointment
     */
    public Appointment(String appointmentID, String patientID, String doctorID, String healthDeptName,
                       String healthDescription, String sessionStartDate,
                       String sessionStartTime, String sessionEndTime) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.healthDeptName = healthDeptName;
        this.healthDescription = healthDescription;
        this.sessionStartDate = sessionStartDate;
        this.sessionStartTime = sessionStartTime;
        this.sessionEndTime = sessionEndTime;
    }

    /**
     * Getter for appointmentID
     * @return
     */
    public String getAppointmentID() {return appointmentID;}

    /**
     * Setter for appointmentID
     * @param appointmentID
     */
    public void setAppointmentID(String appointmentID) {this.appointmentID = appointmentID;}

    /**
     * Setter for Patient ID
     * @param patientID is the patient ID
     */
    public void setPatientID(String patientID){
        this.patientID=patientID;
    }

    /**
     * Getter for Patient ID
     * @return the patient ID
     */
    public String getPatientID(){
        return patientID;
    }

    /**
     * Setter for Doctor ID
     * @param doctorID is the doctor ID
     */
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    /**
     * Getter for the Doctor ID
     * @return doctor ID
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Setter for start date
     * @param sessionStartDate date of appointment
     */
    public void setSessionStartDate(String sessionStartDate){this.sessionStartDate=sessionStartDate;}

    /**
     * Getter to return appointment date
     * @return appointment date
     */
    public String getSessionStartDate(){return sessionStartDate;}

    /**
     * Setter for the department name
     * @param healthDeptName name of the department
     */
    public void setHealthDeptName(String healthDeptName) {
        this.healthDeptName = healthDeptName;
    }

    /**
     * Getter for the department name
     * @return department name
     */
    public String getHealthDeptName() {
        return healthDeptName;
    }

    /**
     * Setter for the health description
     * @param healthDescription is the health description
     */
    public void setHealthDescription(String healthDescription) {
        this.healthDescription = healthDescription;
    }

    /**
     * Getter for the health description
     * @return health description
     */
    public String getHealthDescription() {
        return healthDescription;
    }

    /**
     * Setter for the session time
     * @param sessionEndTime is the session time
     */
    public void setSessionEndTime(String sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    /**
     * Getter for the session time
     * @return session time
     */
    public String getSessionEndTime() {
        return sessionEndTime;
    }

    /**
     * Setter for the star time of the specific session
     * @param sessionStartTime is the start time
     */
    public void setSessionStartTime(String sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    /**
     * Getter for the star time of the specific session
     * @return session start time
     */
    public String getSessionStartTime() {
        return sessionStartTime;
    }
}