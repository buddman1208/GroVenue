package kr.edcan.grovenue.activity;

import android.databinding.DataBindingUtil;
import android.net.Network;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.databinding.ActivityAuthBinding;
import kr.edcan.grovenue.databinding.ActivityRegisterBinding;
import kr.edcan.grovenue.model.User;
import kr.edcan.grovenue.utils.DataManager;
import kr.edcan.grovenue.utils.NetworkHelper;
import kr.edcan.grovenue.utils.NetworkInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    ActivityRegisterBinding binding;
    Call<User> userRegister;
    DataManager manager;
    NetworkInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        setDefault();
    }

    private void setDefault() {
        manager = new DataManager(getApplicationContext());
        service = NetworkHelper.getNetworkInstance();
        ArrayAdapter adapter = new ArrayAdapter(
                this,
                R.layout.textview, // 레이아웃
                new String[]{"남자", "여자"});
        binding.authRegistrSpinner.setAdapter(adapter);
        binding.authRegisterExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = binding.authRegisterID.getText().toString().trim();
                String pw = binding.authRegisterPW.getText().toString().trim();
                final String repw = binding.authRegisterRePW.getText().toString().trim();
                String name = binding.authRegisterName.getText().toString().trim();
                int age = Integer.parseInt(binding.authRegisterAge.getText().toString().trim());
                boolean isMale = (binding.authRegistrSpinner.getSelectedItemPosition() == 0);
                userRegister = service.userRegister(isMale, age, 0, 0, "", name, id, pw);
                userRegister.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        switch (response.code()) {
                            case 200:
                                manager.saveNativeLoginUserInfo(response.body());
                                Toast.makeText(Register.this, response.body().getName()+" 님 가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case 409:
                                Toast.makeText(Register.this, "중복된 아이디입니다!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("asdf", t.getMessage());
                    }
                });
            }
        });
        binding.authRegisterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
