package kr.edcan.grovenue.model;

/**
 * Created by JunseokOh on 2016. 8. 27..
 */
public class Place {
    private String resTitle, resNumber;
    private int distance;

    public Place(String resTitle, String resNumber, int distance) {
        this.resTitle = resTitle;
        this.resNumber = resNumber;
        this.distance = distance;
    }

    public String getResTitle() {
        return resTitle;
    }

    public String getResNumber() {
        return resNumber;
    }

    public int getDistance() {
        return distance;
    }
}
