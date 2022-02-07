package Location;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class LocationService {
    private static final String HOSTNAME = "ehealth-db.cqajckw84dii.us-east-1.rds.amazonaws.com";
    private static final String PORT = "3306";
    private static final String DATABASENAME = "ehealth"; // Or Schema Name

    private static final String URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/ " + DATABASENAME;
    private static final String DBUSERNAME = "admin";
    private static final String DBPASSWORD = "vgustudent";
    private Hashtable<String, Location> locationStorage = new Hashtable<>();
    private Map<String, List<String>> healthDeptStorage = new HashMap<>();

    public LocationService(){}

    public void setLocationStorage(Hashtable<String, Location> locationStorage){
        this.locationStorage = locationStorage;
    }

    public void setHealthDeptStorage(Map<String, List<String>> healthDeptStorage){
        this.healthDeptStorage = healthDeptStorage;
    }

    public Hashtable<String, Location> getLocationOfDoctorFromDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://" +
                    HOSTNAME + ":" + PORT + "/ " + DATABASENAME, "admin", "vgustudent");
            Statement statement = connection.createStatement();
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

    public Map<String, List<String>> getHealthDeptOfDoctors() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + HOSTNAME + ":" + PORT + "/ " + DATABASENAME, "admin", "vgustudent");
            Statement statement = connection.createStatement();
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


    public static void main(String[] args) {
        LocationService locationService = new LocationService();
        Hashtable<String, Double> out = locationService.selectDoctorBasedOnDistanceAndHealthProblem("Surgery", 26);

        Enumeration<String> enumeration = out.keys();
        while (enumeration.hasMoreElements()){
            String key = enumeration.nextElement();
            System.out.println("DoctorID: " + key + ", Distance: " + out.get(key));
        }
    }
}
