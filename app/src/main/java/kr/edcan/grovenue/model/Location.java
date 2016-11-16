package kr.edcan.grovenue.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 최예찬 on 2016-10-10.
 */

public class Location {
    private List<Float> coordinates = Arrays.asList(0.f, 0.f);
    private String type;

    public Location(List<Float> coordinates, String type) {
        this.coordinates = coordinates;
        this.type = type;
    }

    public Location(android.location.Location location){
            this.coordinates.set(0, (float) location.getLongitude());
            this.coordinates.set(1, (float) location.getLatitude());
    }

    public List<Float> getCoordinates() {
        return coordinates;
    }

    public int getLength(){return coordinates.size();}

    public float getLongitude() {
        return coordinates.get(0);
    }

    public float getLatitude() {
        return coordinates.get(1);
    }

    public String getType() {
        return type;
    }
}