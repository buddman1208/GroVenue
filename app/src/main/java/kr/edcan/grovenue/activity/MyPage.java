package kr.edcan.grovenue.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.databinding.ActivityMyPageBinding;
import kr.edcan.grovenue.utils.DataManager;

public class MyPage extends BaseActivity {

    ActivityMyPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_page);
        setDefault();
    }

    private void setDefault() {
        final DataManager manager = DataManager.INSTANCE;

        binding.myPageName.setText(Html.fromHtml("<b>" + manager.getUser().getName() + "</b><br>님 환영합니다!<br><b>Grovenue</b>입니다!"));
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
                manager.setUser(null);
                manager.saveUser();
                Toast.makeText(MyPage.this, "로그아웃 되었습니다!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Auth.class));
                Main.finishThis();
                finish();
            }
        });
    }
}
