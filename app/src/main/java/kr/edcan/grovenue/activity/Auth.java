package kr.edcan.grovenue.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.databinding.ActivityAuthBinding;
import kr.edcan.grovenue.model.User;
import kr.edcan.grovenue.utils.DataManager;
import kr.edcan.grovenue.utils.NetworkHelper;
import kr.edcan.grovenue.utils.NetworkInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth extends BaseActivity {

    ActivityAuthBinding binding;
    Call<User> userLogin;
    NetworkInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        setDefault();
    }

    private void setDefault() {
        final DataManager manager = DataManager.INSTANCE;
        service = NetworkHelper.getNetworkInstance();
        binding.authLoginExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = binding.authIDInput.getText().toString().trim();
                String pw = binding.authPWInput.getText().toString().trim();
                if (!id.equals("") && !pw.equals("")) {
                    userLogin = service.userLogin(id, pw);
                    userLogin.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            switch (response.code()) {
                                case 200:
                                    Log.e("asdf", response.body().getToken());
                                    manager.setUser(response.body());
                                    manager.saveUser();
                                    Toast.makeText(Auth.this, response.body().getName() + "님 환영합니다!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Main.class));

                                    finish();
                                    break;

                                case 401:
                                    Toast.makeText(Auth.this, "아이디 혹은 비밀번호가 잘못되었습니다", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(Auth.this, "서버와의 연결에 문제가 있습니다!", Toast.LENGTH_SHORT).show();
                            Log.e("asdf", t.getMessage());
                        }
                    });
                }
            }
        });
        binding.authRegisterExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }
}
