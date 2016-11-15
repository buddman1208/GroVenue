package kr.edcan.grovenue.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import kr.edcan.grovenue.model.Location;
import kr.edcan.grovenue.model.User;


/**
 * Created by KOHA on 7/9/16.
 */

public class MyGoogleApi implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static MyGoogleApi INSTANCE;

    public static void init(Context context) {
        INSTANCE = new MyGoogleApi(context);
    }


    private final GoogleApiClient googleApiClient;
    private Context context;

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    private LocationRequest locationRequest = new LocationRequest()
            .setInterval(10000)
            .setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    private MyGoogleApi(Context context) {
        this.context = context;
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "위치 권한 설정에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        android.location.Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(lastLocation != null) DataManager.INSTANCE.setLocation(new Location(lastLocation));
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        DataManager.INSTANCE.setLocation(new Location(location));
        Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show();
    }
}