package kr.edcan.grovenue.model;

import android.util.Log;

import com.google.android.gms.fitness.data.DataUpdateNotification;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.utils.DataManager;
import kr.edcan.grovenue.utils.Exclude;

/**
 * Created by Junseok on 2016-09-30.
 */
public class Spot {
    private String _id, name, oldAddress, roadAddress, businessDetail;
    private ArrayList<String> phone;
    private int businessType;
    private ArrayList<Star> stars;
    private float rating;
    private Location location;


    public Spot(String _id, String name, String oldAddress, String roadAddress, String businessDetail, ArrayList<String> phone, int businessType, float rating, ArrayList<Star> stars, Location location) {
        this._id = _id;
        this.name = name;
        this.oldAddress = oldAddress;
        this.roadAddress = roadAddress;
        this.businessDetail = businessDetail;
        this.phone = phone;
        this.businessType = businessType;
        this.rating = rating;
        this.stars = stars;
        this.location = location;
    }

    public String get_id() {
        return _id;
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

    @Exclude
    private static final String[] businessStrArr = {"일반음식점", "관광명소", "숙박업소"};

    public String getBusinessStr() {
        return businessStrArr[businessType];
    }


    public int getBackgroundDrawable() {
        switch (businessType) {
            case 0: // 일반음식점
                return R.drawable.rsz_mockup_haejangguk;
            case 1: // 관광명소
                return R.drawable.rsz_mockup_hotel;
            default/*case 2*/: // 숙박업소
                return R.drawable.rsz_mockup_hotel;
        }

    }

    public float getRating() {
        return rating;
    }

    public ArrayList<Star> getStars() {
        return stars;
    }

    @Exclude
    private int[] countStar;
    @Exclude
    private int maxStarIndex = 0;
    @Exclude
    private boolean calced = false;

    private void calcStar() {
        calced = true;
        countStar = new int[]{0, 0, 0, 0, 0, 0};
        maxStarIndex = 0;
        for (Star star : stars) {
            countStar[star.getScore()]++;
        }
        int max = 0;
        for (int i = 1; i <= 5; i++) {
            if (max < countStar[i]) {
                max = countStar[i];
                maxStarIndex = i;
            }
        }
    }

    public float getStarWeight(int index) {
        if (!calced) calcStar();
        int max = countStar[maxStarIndex];
        if (max == 0) {
            Log.d("asdfasdf", String.format("index : %d, countStar : %d", index, countStar[index]));
            return 0;
        } else {
            Log.d("asdfasdf", String.format("index : %d, countStar : %d, max : %d, result : %.1f", index, countStar[index], max, (float) countStar[index] / (float) max));
            return (float) countStar[index] / (float) max;
        }
    }


    public Location getLocation() {
        return location;
    }


    private Float distance = null;

    @Exclude
    private static final int KM_IN_RADIUS = 6371; // Radius of the earth in km

    public void calcDistance() {
        if (DataManager.INSTANCE.getLocation() != null && location.getLength() == 2) {
            Log.e("asdf", DataManager.INSTANCE.getLocation().getLength() + "");
            Log.e("asdf", DataManager.INSTANCE.getLocation().getLength() + "");
            double dLat = Math.toRadians(location.getLatitude() - DataManager.INSTANCE.getLocation().getLatitude());  // deg2rad below
            double dLon = Math.toRadians(location.getLongitude() - DataManager.INSTANCE.getLocation().getLongitude());
            double a =
                    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                            Math.cos(Math.toRadians(location.getLatitude()))
                                    * Math.cos(Math.toRadians(location.getLatitude()))
                                    * Math.sin(dLon / 2)
                                    * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double d = KM_IN_RADIUS * c; // Distance in km
            distance = (float) d;
        } else distance = null;
    }

    public float getDistance() {
        return distance;
    }

    public String getDistanceStr() {
        if (distance == null) {
            return ("위치 정보 없음");
        } else if (distance < 1) {
            distance = (distance * 100) / 100 * 100;
            return distance + "m";
        } else {
            return String.format("%.1fkm", distance);
        }
    }


    public String getSubtitle() {
        StringBuilder sb = new StringBuilder();
        if (distance == null) {
            sb.append("위치 정보 없음");
        } else sb.append(String.format("%s 거리 안에 있음", getDistanceStr()));

        if (phone.size() > 0) {
            sb.append(", ");
            sb.append(phone.get(0));
            if (phone.size() > 1) sb.append("..");
        }

        return sb.toString();
    }
}
