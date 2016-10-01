package kr.edcan.grovenue.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.databinding.ActivityFilterBinding;
import kr.edcan.grovenue.views.CartaTagView;

public class FilterActivity extends AppCompatActivity {

    ActivityFilterBinding binding;
    ArrayList<String> travel, money, location;
    CartaTagView travelTag[] = {};
    CartaTagView moneyTag[] = {};
    CartaTagView locationTag[] = {};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        initData();
        setDefault();
        setAppbarLayout();
    }

    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("검색 필터 설정하기");
        getSupportActionBar().setElevation(5);
    }

    private void initData() {
        travel = new ArrayList<>();
        money = new ArrayList<>();
        location = new ArrayList<>();
        Collections.addAll(travel, new String[]{"가족 여행", "관광", "도심", "낭만", "럭셔리", "레저", "비즈니스", "식도락"});
        Collections.addAll(money, new String[]{"예산 적음", "예산 적당함", "예산 많음"});
        Collections.addAll(location, new String[]{"100m 이내", "500m 이내", "1km 이내", "10km 이내", "50km 이내"});
    }

    private void setDefault() {
        travelTag = new CartaTagView[]
                {binding.filterTravel1, binding.filterTravel2, binding.filterTravel3, binding.filterTravel4, binding.filterTravel5, binding.filterTravel6, binding.filterTravel7, binding.filterTravel8};
        moneyTag = new CartaTagView[]
                {binding.filterMoney1, binding.filterMoney2, binding.filterMoney3};
        locationTag = new CartaTagView[]
                {binding.filterLocation1, binding.filterLocation2, binding.filterLocation3, binding.filterLocation4, binding.filterLocation5};
        for (final CartaTagView c : travelTag) {
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.setFullMode(!c.getFullMode());
                    changeTravelData(c);
                }
            });
        }
        for (final CartaTagView c : moneyTag) {
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.setFullMode(!c.getFullMode());
                    changeMoneyData(c);
                }
            });
        }
        for (final CartaTagView c : locationTag) {
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.setFullMode(!c.getFullMode());
                    changeLocationData(c);
                }
            });
        }

    }

    private void changeTravelData(CartaTagView c) {
        Log.e("asdf", travel.indexOf(c.getText().toString()) + "");
    }

    private void changeMoneyData(CartaTagView c) {
        Log.e("asdf", money.indexOf(c.getText().toString()) + "");
    }

    private void changeLocationData(CartaTagView c) {
        Log.e("asdf", location.indexOf(c.getText().toString()) + "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
