package Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Location {
    private double latitude;
    private double longitude;
    // create a location
    public Location(){
    }
    // create a location with the specific longitude and latitude
    public Location(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void getLocation(String urlLink){
        URL url = null;
        ArrayList<String> content = new ArrayList<>();
        try {
            url = new URL(urlLink);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = br.readLine()) != null) {
                content.add(line);
            }
            double lat = -1, lon = -1;
            for (String str : content) {
                if (str.contains("loc")) {
                    str = str.split(": ")[1];
                    String[] loc = str.split(",");

                    lat = Double.parseDouble(loc[0].substring(1));
                    lon = Double.parseDouble(loc[1].substring(0, loc[1].length() - 1));
                }
            }
            this.setLatitude(lat);
            this.setLongitude(lon);
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//

    public void setLatitude(double latitude){
        this.latitude = latitude;
    } // set value(double) to latitude

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    // Calculate distance between two location
    public double distanceBetween2Points(double la1, double lo1, double la2, double lo2) {
        double R = 6371;
        double dLat = (la2 - la1) * (Math.PI / 180);
        double dLon = (lo2 - lo1) * (Math.PI / 180);
        double la1ToRad = la1 * (Math.PI / 180);
        double la2ToRad = la2 * (Math.PI / 180);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(la1ToRad)
                * Math.cos(la2ToRad) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }//Return the value(Double) after given two different latitude and longitude

    public double distanceBetween2Points(Location destinationPoint){
        double destination_longitude = destinationPoint.getLongitude();
        double destination_latitude = destinationPoint.getLatitude();
        return this.distanceBetween2Points(destination_latitude, destination_longitude, this.latitude, this.longitude);
    }//Return the value(Double) after given index latitude and longitude of destination

    @Override
    public String toString(){
        return "Latitude: " + this.getLatitude() + ", longitude: " + this.getLongitude();
    }
}
