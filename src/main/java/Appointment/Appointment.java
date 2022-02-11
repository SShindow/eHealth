package Appointment;
public class Appointment {
    private String patientID;
    private String doctorID;
    private String healthDeptName;
    private String healthDescription;
    private String sessionStartDate;
    private String sessionStartTime;
    private String sessionEndTime;
    public void setPatientID(String patientID){
        this.patientID=patientID;
    }
    public String getPatientID(){
        return patientID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setSessionStartDate(String sessionStartDate){this.sessionStartDate=sessionStartDate;}
    public String getSessionStartDate(){return sessionStartDate;}

    public void setHealthDeptName(String healthDeptName) {
        this.healthDeptName = healthDeptName;
    }

    public String getHealthDeptName() {
        return healthDeptName;
    }

    public void setHealthDescription(String healthDescription) {
        this.healthDescription = healthDescription;
    }

    public String getHealthDescription() {
        return healthDescription;
    }

    public void setSessionEndTime(String sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public String getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionStartTime(String sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public String getSessionStartTime() {
        return sessionStartTime;
    }
}