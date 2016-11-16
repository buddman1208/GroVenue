package kr.edcan.grovenue.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.model.User;
import kr.edcan.grovenue.utils.DataManager;
import kr.edcan.grovenue.utils.MyGoogleApi;
import kr.edcan.grovenue.utils.NetworkHelper;
import kr.edcan.grovenue.utils.NetworkInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkMPermission();
    }

    private void startGPSCheck() {
        if (checkGPS()) startSplash();
        else alertCheckGPS();
    }

    private void startSplash() {
        MyGoogleApi.init(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setDefault();
            }
        }, 1500);
    }

    private void setDefault() {
        final DataManager manager = DataManager.INSTANCE;

        if (manager.getUser() == null) {
            startActivity(new Intent(getApplicationContext(), Auth.class));
            finish();
        } else {
            NetworkHelper.getNetworkInstance().getUserInfo(manager.getUser().getToken()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.d("reponse code", response.code() + " HTTP");
                    switch (response.code()) {
                        case 200:
                            manager.setUser(response.body());
                            manager.saveUser();
                            Toast.makeText(Splash.this, manager.getUser().getName() + " 님 안녕하세요!", Toast.LENGTH_SHORT).show();
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
                    t.printStackTrace();
                    Toast.makeText(Splash.this, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private static final int REQUEST_CODE = 234;

    private void checkMPermission() {
        // 갤러리 사용 권한 체크( 사용권한이 없을경우 -1 )
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없을경우

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_CODE);
        } else {
            // 사용 권한이 있음을 확인한 경우
            startGPSCheck();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // 갤러리 사용권한에 대한 콜백을 받음
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 동의 버튼 선택
                    startGPSCheck();
                } else {
                    // 사용자가 권한 동의를 안함
                    // 권한 동의안함 버튼 선택
                    Toast.makeText(Splash.this, "권한을 동의해주셔야 이용이 가능합니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            // 예외케이스
        }
    }

    private boolean checkGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void alertCheckGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS 센서가 꺼져 있습니다. 켜시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveConfigGPS();
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                startSplash();
                                if (!checkGPS()) {
                                    Toast.makeText(Splash.this, "GPS 센서가 켜지지 않았습니다. GPS를 켜주셔야 정상적인 사용이 가능합니다!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private static final int REQUEST_GPS = 35;

    // GPS 설정화면으로 이동
    private void moveConfigGPS() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(gpsOptionsIntent, REQUEST_GPS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GPS) {
            startSplash();
            if (!checkGPS()) {
                Toast.makeText(this, "GPS 센서가 켜지지 않았습니다. GPS를 켜주셔야 정상적인 사용이 가능합니다!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
