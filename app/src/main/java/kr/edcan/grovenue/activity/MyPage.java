package kr.edcan.grovenue.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.databinding.ActivityMyPageBinding;
import kr.edcan.grovenue.utils.DataManager;

public class MyPage extends AppCompatActivity {

    DataManager manager;
    ActivityMyPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_page);
        setDefault();
    }

    private void setDefault() {
        manager = new DataManager(getApplicationContext());
        binding.myPageName.setText(Html.fromHtml("<b>" + manager.getActiveUser().second.getName() + "</b><br>님 환영합니다!<br><b>Grovenue</b>입니다!"));
        binding.myPageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FilterActivity.class));
                finish();
            }
        });
        binding.myPageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.removeAllData();
                Toast.makeText(MyPage.this, "로그아웃 되었습니다!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Auth.class));
                Main.finishThis();
                finish();
            }
        });
    }
}
