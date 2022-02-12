package Location;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import Connection.DBControl;

/**
 * @author Thong Le Thanh
 * This class base on health problems category, input desirable distance to find out stuitable doctorID from user location
 */
public class LocationService {

//    private static final String HOSTNAME = "ehealth-db.cqajckw84dii.us-east-1.rds.amazonaws.com";
//    private static final String PORT = "3306";
//    private static final String DATABASENAME = "ehealth"; // Or Schema Name
//
//    private static final String URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/ " + DATABASENAME;
//    private static final String DBUSERNAME = "admin";
//    private static final String DBPASSWORD = "vgustudent";
    private Hashtable<String, Location> locationStorage = new Hashtable<>();
    private Map<String, List<String>> healthDeptStorage = new HashMap<>();

    /**
     * Null constructor
     */
    public LocationService(){}

    /**
     * Setter for location storage
     * @param locationStorage stores the location info
     */
    public void setLocationStorage(Hashtable<String, Location> locationStorage){
        this.locationStorage = locationStorage;
    }

    /**
     * Setter for the health department name
     * @param healthDeptStorage  name of the department
     */
    public void setHealthDeptStorage(Map<String, List<String>> healthDeptStorage){
        this.healthDeptStorage = healthDeptStorage;
    }

    /**
     * This method is used to get location of doctor through unique doctorID
     * @return Location of Doctor
     */
    public Hashtable<String, Location> getLocationOfDoctorFromDatabase() {
        try {
            Statement statement = DBControl.dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ehealth.doctor\n");
            Location tempLocation = new Location();

            while (resultSet.next()){
                tempLocation.setLatitude(resultSet.getDouble("doctorClinicLatitude"));
                tempLocation.setLongitude(resultSet.getDouble("doctorClinicLongitude"));
                locationStorage.put(resultSet.getString("doctorID"), new Location(tempLocation.getLatitude(), tempLocation.getLongitude()));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return this.locationStorage;
    }

    /**
     *This method is used to get doctorId from health problems
     * @return From one health problem that connect to unique doctorID
     */
    public Map<String, List<String>> getHealthDeptOfDoctors() {
        try {
            Statement statement = DBControl.dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ehealth.doctorhealthdept\n");
            while (resultSet.next()){
                healthDeptStorage.computeIfAbsent(resultSet.getString("healthDeptName"), k -> new ArrayList<>())
                        .add(resultSet.getString("doctorID"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return this.healthDeptStorage;
    }

    /**
     * This method get selected doctor from desirable distance and health problem
     * @param health_problem Chosen health problems from category
     * @param distance Desirable distance input
     * @return Unique doctorID with calculated distance from user's location
     */
    public Hashtable<String, Double> selectDoctorBasedOnDistanceAndHealthProblem(String health_problem, Integer distance){
        this.locationStorage = this.getLocationOfDoctorFromDatabase();
        this.healthDeptStorage = this.getHealthDeptOfDoctors();

        Location myLocation = new Location();
        myLocation.getLocation("https://ipinfo.io/?token=dc6e765ff75814");
        List<String> doctorIDList = healthDeptStorage.get(health_problem);

        Enumeration<String> enumeration = this.locationStorage.keys();
        Hashtable<String, Double> output = new Hashtable<>();

        while (enumeration.hasMoreElements()) {

            String key = enumeration.nextElement(); //
            double distance_from_clinic = locationStorage.get(key).distanceBetween2Points(myLocation);

            for (String doctorID : doctorIDList){
                if (key.equals(doctorID) && (distance_from_clinic <= distance)){
                    output.put(key, distance_from_clinic);
                }
            }
        }
        return output;
    }


    /**
     * Where functions is executed
     */
    public static void main(String[] args) {
        LocationService locationService = new LocationService();
        Hashtable<String, Double> out = locationService.selectDoctorBasedOnDistanceAndHealthProblem("Surgery", 26);

        Enumeration<String> enumeration = out.keys();
        while (enumeration.hasMoreElements()){
            String key = enumeration.nextElement();
            System.out.println("DoctorID: " + key + ", Distance: " + out.get(key));
        }
    }

    /**
     * Method to display doctor list
     * @param doctorList indicates list of doctors
     */
    public static void printDoctorList(Hashtable<String, Double> doctorList)
    {
        Enumeration<String> enumeration = doctorList.keys();
        while (enumeration.hasMoreElements()){
            String key = enumeration.nextElement();
            System.out.println("DoctorID: " + key + ", Distance: " + doctorList.get(key));
        }
    }
}
