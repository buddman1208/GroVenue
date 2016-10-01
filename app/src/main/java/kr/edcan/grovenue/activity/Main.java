package kr.edcan.grovenue.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.adapter.MainRecyclerAdapter;
import kr.edcan.grovenue.databinding.ActivityMainBinding;
import kr.edcan.grovenue.model.Place;
import kr.edcan.grovenue.model.Spot;
import kr.edcan.grovenue.utils.DataManager;
import kr.edcan.grovenue.utils.ImageHelper;
import kr.edcan.grovenue.utils.NetworkHelper;
import kr.edcan.grovenue.utils.NetworkInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity {

    public static void finishThis(){
        if(activity!=null) activity.finish();
    }
    static Activity activity;
    ActivityMainBinding mainBind;
    ArrayList<Spot> placeList;
    Call<ArrayList<Spot>> getSpotInfo;
    NetworkInterface service;
    DataManager manager;
    RecyclerView mainRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        loadData();
        setDefault();
    }

    private void loadData() {
        placeList = new ArrayList<>();
        placeList.add(new Spot());
        manager = new DataManager(getApplicationContext());
        service = NetworkHelper.getNetworkInstance();
        getSpotInfo = service.getSpotList(manager.getActiveUser().second.getToken(), 100);
        getSpotInfo.enqueue(new Callback<ArrayList<Spot>>() {
            @Override
            public void onResponse(Call<ArrayList<Spot>> call, Response<ArrayList<Spot>> response) {
                switch (response.code()) {
                    case 200:
                        placeList.addAll(response.body());
                        setDefault();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Spot>> call, Throwable t) {

            }
        });

    }

    private void setDefault() {
        mainBind = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mainBind.toolbar);
        getSupportActionBar().setTitle("");
        mainRecycler = mainBind.mainRecyclerView;
        mainRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        MainRecyclerAdapter adapter = new MainRecyclerAdapter(getApplicationContext(), placeList);

        mainRecycler.setAdapter(adapter);
        mainBind.mainLaunchMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyPage.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.filter:
                startActivity(new Intent(getApplicationContext(), FilterActivity.class));
                break;
            case R.id.search:
                Toast.makeText(activity, "검색 기능은 추후 지원될 예정입니다!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
