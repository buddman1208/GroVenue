package kr.edcan.grovenue.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Junseok on 2016-09-30.
 */

public class NetworkHelper {
    private Context context;
    private final static String url = "http://gangwon.applepi.kr";
    private final static int port = 80;

    public NetworkHelper(Context context) {
        this.context = context;
    }

    public static Retrofit retrofit;

    public static NetworkInterface getNetworkInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url + ":" + port)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setExclusionStrategies(new AnnotationBasedExclusionStrategy()).create()))
                    .build();
        }
        return retrofit.create(NetworkInterface.class);
    }

    public static boolean returnNetworkState(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
