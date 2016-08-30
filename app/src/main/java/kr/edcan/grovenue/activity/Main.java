package kr.edcan.grovenue.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.adapter.MainRecyclerAdapter;
import kr.edcan.grovenue.databinding.ActivityMainBinding;
import kr.edcan.grovenue.model.Place;
import kr.edcan.grovenue.utils.ImageHelper;

public class Main extends AppCompatActivity {

    ActivityMainBinding mainBind;
    ArrayList<Place> placeList;

    RecyclerView mainRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        setDefault();
    }

    private void loadData() {
        placeList = new ArrayList<>();
        placeList.add(new Place("준석이네 원조 해장국 24시", "033-1234-1234", 200));
        placeList.add(new Place("준석이네 원조 해장국 24시", "033-1234-1234", 200));
        placeList.add(new Place("준석이네 원조 해장국 24시", "033-1234-1234", 200));
        placeList.add(new Place("준석이네 원조 해장국 24시", "033-1234-1234", 200));
        placeList.add(new Place("준석이네 원조 해장국 24시", "033-1234-1234", 200));
        placeList.add(new Place("준석이네 원조 해장국 24시", "033-1234-1234", 200));
        placeList.add(new Place("준석이네 원조 해장국 24시", "033-1234-1234", 200));
        placeList.add(new Place("준석이네 원조 해장국 24시", "033-1234-1234", 200));

    }

    private void setDefault() {
        mainBind = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainRecycler = mainBind.mainRecyclerView;
        mainRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        MainRecyclerAdapter adapter = new MainRecyclerAdapter(getApplicationContext(), placeList);

        mainRecycler.setAdapter(adapter);
    }
}
