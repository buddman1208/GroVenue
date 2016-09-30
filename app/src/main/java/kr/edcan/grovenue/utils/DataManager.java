package kr.edcan.grovenue.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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

    /* Data Keys */
    public static final String HAS_ACTIVE_USER = "has_active_user";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String USER_JOB = "user_job";
    public static final String USER_PURPOSE = "user_purpose";
    public static final String USER_BUDGET = "user_budget";
    public static final String USER_AGE = "user_age";
    public static final String ISMALE = "ismale";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public DataManager(Context c) {
        this.context = c;
        preferences = context.getSharedPreferences("Grovenue", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void save(String key, String data) {
        editor.putString(key, data);
        editor.apply();
    }

    public void saveNativeLoginUserInfo(User user) {
        editor.putBoolean(HAS_ACTIVE_USER, true);
        editor.putBoolean(ISMALE, user.isMale());
        editor.putInt(USER_AGE, user.getAge());
        editor.putInt(USER_PURPOSE, user.getPurpose());
        editor.putInt(USER_BUDGET, user.getPurpose());
        editor.putString(USER_JOB, user.getJob());
        editor.putString(USER_NAME, user.getName());
        editor.putString(USER_ID, user.getId());
        editor.putString(USER_TOKEN, user.getToken());
        editor.apply();
    }

    public Pair<Boolean, User> getActiveUser() {
        if (preferences.getBoolean(HAS_ACTIVE_USER, false)) {
            User user = new User(
                    preferences.getBoolean(ISMALE, false),
                    preferences.getInt(USER_AGE, -1),
                    preferences.getInt(USER_PURPOSE, -1),
                    preferences.getInt(USER_BUDGET, -1),
                    preferences.getString(USER_JOB, ""),
                    preferences.getString(USER_NAME, ""),
                    preferences.getString(USER_ID, ""),
                    preferences.getString(USER_TOKEN, "")
            );
            return Pair.create(true, user);
        } else return Pair.create(false, null);
    }

    public void removeAllData() {
        editor.clear();
        editor.apply();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean isFirst() {
        return preferences.getBoolean("IS_FIRST", true);
    }

    public void notFirst() {
        editor.putBoolean("IS_FIRST", false);
        editor.apply();
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0);
    }

}