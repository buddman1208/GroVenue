package kr.edcan.grovenue.application;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

import kr.edcan.grovenue.utils.DataManager;
import kr.edcan.grovenue.utils.MyGoogleApi;

/**
 * Created by 최예찬 on 2016-10-05.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addCustom1(Typekit.createFromAsset(this, "NanumBarunGothicLight.otf"))
                .addNormal(Typekit.createFromAsset(this, "NanumBarunGothic.otf"))
                .addBold(Typekit.createFromAsset(this, "NanumBarunGothicBold.otf"));
        DataManager.init(this);
    }


}
