/**
 * Class to construct an appointment with the
 * @author Chau Truong Vinh Hoang
 */

package Appointment;

/**
 * General class for appointment
 */
public class Appointment {
    private String patientID;
    private String doctorID;
    private String healthDeptName;
    private String healthDescription;
    private String sessionStartTime;
    private String sessionEndTime;

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
     * Setter for the health department
     * @param healthDeptName is the name of the department
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