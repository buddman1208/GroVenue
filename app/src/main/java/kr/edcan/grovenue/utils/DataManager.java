package kr.edcan.grovenue.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.edcan.grovenue.model.Location;
import kr.edcan.grovenue.model.Spot;
import kr.edcan.grovenue.model.SpotQuery;
import kr.edcan.grovenue.model.User;


/**
 * Created by KOHA on 7/9/16.
 */

public class DataManager {
    /* Login Type
    * 0 Facebook
    * 1: Twitter
    * 2 Kakao
    * 3 Naver
    * 4 Native Login
    * */

    public static DataManager INSTANCE;

    public static void init(Context context){
        INSTANCE = new DataManager(context);
    }

    private SharedPreferences pref;

    private Gson gson = new GsonBuilder().setExclusionStrategies(new AnnotationBasedExclusionStrategy()).create();

    public Gson getGson() {
        return gson;
    }

    private User user;
    private SpotQuery spotQuery;
    private Location location;

    private boolean isSpotQueryChanged = false;

    public boolean isSpotQueryChanged() {
        return isSpotQueryChanged;
    }

    private static final String USER_KEY = "user";
    private static final String SPOT_QUERY_KEY = "user";


    private DataManager(Context context){
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        loadUser();
        loadSpotQuery();
    }

    public void loadUser(){
        user = gson.fromJson(pref.getString(USER_KEY, null), User.class);
    }

    public void saveUser(){
        pref.edit().putString(USER_KEY, gson.toJson(user)).apply();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }


    public void loadSpotQuery() {
        spotQuery = gson.fromJson(pref.getString(SPOT_QUERY_KEY, null), SpotQuery.class);
        if(spotQuery == null) spotQuery = new SpotQuery();
    }

    public void saveSpotQuery() {
        pref.edit().putString(SPOT_QUERY_KEY, gson.toJson(spotQuery)).apply();
        isSpotQueryChanged = true;
    }

    public void setSpotQueryChangedFalse() {
        isSpotQueryChanged = false;
    }

    public SpotQuery getSpotQuery() {
        return spotQuery;
    }

    public void setSpotQuery(SpotQuery spotQuery) {
        this.spotQuery = spotQuery;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location){
        this.location = location;
    }




}