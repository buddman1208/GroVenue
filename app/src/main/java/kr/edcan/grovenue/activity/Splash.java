package kr.edcan.grovenue.activity;

import android.content.Intent;
import android.net.Network;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.model.User;
import kr.edcan.grovenue.utils.DataManager;
import kr.edcan.grovenue.utils.NetworkHelper;
import kr.edcan.grovenue.utils.NetworkInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {

    Call<User> getUserInfo;
    NetworkInterface service;
    DataManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setDefault();
    }

    private void setDefault() {
        manager = new DataManager(getApplicationContext());
        service = NetworkHelper.getNetworkInstance();
        if (!manager.getActiveUser().first) {
            startActivity(new Intent(getApplicationContext(), Auth.class));
            finish();
        } else {
            getUserInfo = service.getUserInfo(manager.getActiveUser().second.getToken());
            getUserInfo.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    switch (response.code()) {
                        case 200:
                            manager.saveNativeLoginUserInfo(response.body());
                            Toast.makeText(Splash.this, manager.getActiveUser().second.getName() + " 님 안녕하세요!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Main.class));
                            finish();
                            break;
                        default:
                            Toast.makeText(Splash.this, "인증 오류가 발생하였습니다. 다시 로그인해주세요", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Auth.class));
                            finish();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }
}
