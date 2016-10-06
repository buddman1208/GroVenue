package kr.edcan.grovenue.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import kr.edcan.grovenue.R;

/**
 * Created by 최예찬 on 2016-10-05.
 */
public class BaseActivity extends AppCompatActivity {

    private TextView titleText;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void setTitle(CharSequence title) {

        if(titleText == null) titleText = (TextView) findViewById(R.id.tv_toolbar_title);

        if (getSupportActionBar() == null)
            return;

        if (titleText != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            titleText.setText(title);
        } else {

            getSupportActionBar().setDisplayShowTitleEnabled(true);
            super.setTitle(title);
        }
    }
}
