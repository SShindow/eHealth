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
     * @param firstName is the firstname of the doctor
     * @param lastName is the lastname of the doctor
     * @param address is located by longitude and latitude of the doctor
     * @param clinicName is where the doctor working
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
     * @param firstName is the firstname of the doctor
     * @param lastName is the lastname of the doctor
     * @param address is located by longitude and latitude of the doctor
     * @param clinicName is where the doctor working
     * @param clinicLongitude is the longitude of the doctor workplace
     * @param clinicLatitude is the latitude of the doctor workplace
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
     * @param doctorID ID of the doctor
     * @param firstName is the firstname of the doctor
     * @param lastName is the lastname of the doctor
     * @param address is located by longitude and latitude of the doctor
     * @param clinicName is where the doctor working
     * @param clinicLongitude is the longitude of the doctor workplace
     * @param clinicLatitude is the latitude of the doctor workplace
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
