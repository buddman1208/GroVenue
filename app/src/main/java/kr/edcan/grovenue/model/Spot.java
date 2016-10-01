package kr.edcan.grovenue.model;

import java.util.ArrayList;

/**
 * Created by Junseok on 2016-09-30.
 */
public class Spot {
    private String name, oldAddress, roadAddress, businessDetail;
    private ArrayList<String> phone;
    private int businessType;
    private ArrayList<Star> stars;
    private Location location;

    public Spot() {
    }

    public Spot(String name, String oldAddress, String roadAddress, String businessDetail, ArrayList<String> phone, int businessType, ArrayList<Star> stars, Location location) {
        this.name = name;
        this.oldAddress = oldAddress;
        this.roadAddress = roadAddress;
        this.businessDetail = businessDetail;
        this.phone = phone;
        this.businessType = businessType;
        this.stars = stars;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getOldAddress() {
        return oldAddress;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public String getBusinessDetail() {
        return businessDetail;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public int getBusinessType() {
        return businessType;
    }

    public ArrayList<Star> getStars() {
        return stars;
    }

    public Location getLocation() {
        return location;
    }

    public class Location{
        private ArrayList<Double> coordinates;
        private String type;

        public Location(ArrayList<Double> coordinates, String type) {
            this.coordinates = coordinates;
            this.type = type;
        }

        public ArrayList<Double> getCoordinates() {
            return coordinates;
        }

        public String getType() {
            return type;
        }
    }
}
