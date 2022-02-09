/**
 * This class provides model for a doctor
 * @Author Hai Cao Xuan
 */

package Models;

import java.util.UUID;

public class Doctor {
    UUID doctorID;
    String firstName;
    String lastName;
    String address;
    String clinicName;
    double clinicLongitude;
    double clinicLatitude;

    /**
     * Constructor for fetching doctor from database
     * @param firstName
     * @param lastName
     * @param address
     * @param clinicName
     */
    public Doctor(String firstName, String lastName, String address, String clinicName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.clinicName = clinicName;
    }

    /**
     * Constructor to create a doctor
     * @author Hai Cao Xuan
     * @param firstName
     * @param lastName
     * @param address
     * @param clinicName
     * @param clinicLongitude
     * @param clinicLatitude
     */
    public Doctor(String firstName, String lastName, String address, String clinicName, double clinicLongitude, double clinicLatitude)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.clinicName = clinicName;
        this.clinicLongitude = clinicLongitude;
        this.clinicLatitude = clinicLatitude;
    }
    public Doctor(UUID doctorID, String firstName, String lastName, String address, String clinicName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.clinicName = clinicName;
        this.doctorID=doctorID;

    }
    /**
     * Constructor for creating a doctor with the ID
     * @author Hoang Dinh Minh
     * @param doctorID
     * @param firstName
     * @param lastName
     * @param address
     * @param clinicName
     * @param clinicLongitude
     * @param clinicLatitude
     */
    public Doctor(UUID doctorID, String firstName, String lastName, String address, String clinicName, double clinicLongitude, double clinicLatitude)
    {
        this.doctorID = doctorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.clinicName = clinicName;
        this.clinicLongitude = clinicLongitude;
        this.clinicLatitude = clinicLatitude;
    }
//    Getter
    public String getFirstName() {
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getAddress() {
        return address;
    }
    public String getClinicName(){
        return clinicName;
    }
    public double getClinicLongitude() {return clinicLongitude;}
    public double getClinicLatitude() {return clinicLatitude;}
    public UUID getDoctorID() {return doctorID;}

    //    Setter
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setClinicName(String clinicName){
        this.clinicName = clinicName;
    }
    public void setClinicLongitude(double clinicLongitude) {this.clinicLongitude = clinicLongitude;}
    public void setClinicLatitude(double clinicLatitude) {this.clinicLatitude = clinicLatitude;}
    public void setDoctorID(UUID doctorID) {this.doctorID = doctorID;}
}
