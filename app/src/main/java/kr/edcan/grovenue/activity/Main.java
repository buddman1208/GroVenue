package kr.edcan.grovenue.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.nitrico.lastadapter.LastAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kr.edcan.grovenue.BR;
import kr.edcan.grovenue.R;
import kr.edcan.grovenue.databinding.ActivityMainBinding;
import kr.edcan.grovenue.model.Spot;
import kr.edcan.grovenue.model.SpotQuery;
import kr.edcan.grovenue.model.SpotResult;
import kr.edcan.grovenue.utils.DataManager;
import kr.edcan.grovenue.utils.MyGoogleApi;
import kr.edcan.grovenue.utils.NetworkHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends BaseActivity {

    public static void finishThis() {
        if (activity != null) activity.finish();
    }

    static Activity activity;
    ActivityMainBinding mainBind;
    ObservableArrayList<Object> spotList;

    LastAdapter adapter;
    private boolean isLoading = false;
    private boolean hasMore = true;
    private int currentPosition = 0;
    private final static int loadSize = 10;

    Call<SpotResult> call;
    private String query = "";
    private SearchView searchView;


    private static class RecyclerHeader {
    }

    private static class RecyclerProgress {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        spotList = new ObservableArrayList<>();
        spotList.add(new RecyclerHeader());
        spotList.add(new RecyclerProgress());
        setDefault();
    }


    private void loadData() {
        DataManager manager = DataManager.INSTANCE;
        if(call != null) call.cancel();

        SpotQuery spotQuery = manager.getSpotQuery();
        call = NetworkHelper.getNetworkInstance().getSpotList(
                manager.getUser().getToken(), currentPosition, loadSize,
                manager.getLocation()!=null?manager.getLocation().getLongitude():null,
                manager.getLocation()!=null?manager.getLocation().getLatitude():null,
                "".equals(query)?null:query,
                spotQuery.getPurpose(),
                (spotQuery.getBudget() == -1)?null:spotQuery.getBudget(),
                manager.getLocation()!=null?spotQuery.getMaxDistance():null,
                spotQuery.getMinScore()
        );

        call.enqueue(new Callback<SpotResult>() {
            @Override
            public void onResponse(Call<SpotResult> call, Response<SpotResult> response) {
                switch (response.code()) {
                    case 200:
                        SpotResult result = response.body();
                        List<Spot> resultSpots = result.getResult();
                        for (Spot spot : resultSpots) {
                            spot.calcDistance();
                        }
                        spotList.addAll(spotList.size() - 1, resultSpots);
                        currentPosition += loadSize;

                        hasMore = result.isHasMore();
                        if(!hasMore) {
                            spotList.remove(spotList.size() - 1);
                        }
                        Main.this.call = null;
                        break;
                }
            }

            @Override
            public void onFailure(Call<SpotResult> call, Throwable t) {
                if(!call.isCanceled()) {
                    t.printStackTrace();
                    Toast.makeText(Main.this, "인터넷 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                Main.this.call = null;
            }
        });

    }

    private void setDefault() {
        mainBind = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mainBind.toolbar);
        setTitle("찾아보기");
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mainBind.mainRecycler.setLayoutManager(layoutManager);

        adapter = LastAdapter.with(spotList, BR.item)
                .map(RecyclerHeader.class, R.layout.header_main_recycler)
                .map(RecyclerProgress.class, R.layout.progress_main_recycler)
                .map(Spot.class, R.layout.item_main_recycler)
                .onClickListener(new LastAdapter.OnClickListener() {
                    @Override
                    public void onClick(Object item, View view, int type, int position) {
                        if (item instanceof Spot) {
                            Gson gson = DataManager.INSTANCE.getGson();
                            startActivity(new Intent(Main.this, MapActivity.class).putExtra("spot", gson.toJson(item)));
                        }
                    }
                })
                .into(mainBind.mainRecycler);

        mainBind.mainRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(hasMore) {
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                    if (!isLoading && totalItemCount <= lastVisibleItem + 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });

    }

    public void loadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                isLoading = false;
            }
        }, 500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MyPage.class));
                break;
            case R.id.filter:
                startActivity(new Intent(getApplicationContext(), FilterActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                spotList.subList(1, spotList.size()).clear();
                hasMore = true;
                spotList.add(new RecyclerProgress());
                currentPosition = 0;
                Main.this.query = query;
                loadData();
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyGoogleApi.INSTANCE.getGoogleApiClient().connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyGoogleApi.INSTANCE.getGoogleApiClient().disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(DataManager.INSTANCE.isSpotQueryChanged()){
            spotList.subList(1, spotList.size()).clear();
            hasMore = true;
            spotList.add(new RecyclerProgress());
            currentPosition = 0;
            loadData();
            DataManager.INSTANCE.setSpotQueryChangedFalse();
        }
    }


}
