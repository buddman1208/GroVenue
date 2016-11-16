package kr.edcan.grovenue.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.databinding.ActivityMapBinding;
import kr.edcan.grovenue.databinding.DialogAddStarBinding;
import kr.edcan.grovenue.databinding.IncludeBtnStarBinding;
import kr.edcan.grovenue.model.Spot;
import kr.edcan.grovenue.utils.DataManager;
import kr.edcan.grovenue.utils.NetworkHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    private Spot spot;

    private AlertDialog dialog;
    private int dialogScore = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


        spot = DataManager.INSTANCE.getGson().fromJson(getIntent().getStringExtra("spot"), Spot.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        binding.setItem(spot);
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        setTitle("찾아보기");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        binding.contentMap.transparentImage.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        binding.contentMap.scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        binding.contentMap.scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        binding.contentMap.scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!spot.getPhone().isEmpty() && !spot.getPhone().get(0).equals("")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + spot.getPhone().get(0)));
                    if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MapActivity.this, "전화 권한 설정에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startActivity(intent);
                } else
                    Toast.makeText(MapActivity.this, "전화번호 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.contentMap.contentStarsList.btnWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(MapActivity.this).setView(getReviewDialog()).show();
            }
        });

        reloadSpot();
    }

    private void reloadSpot() {

        NetworkHelper.getNetworkInstance().getSpotInfo(
                DataManager.INSTANCE.getUser().getToken(),
                spot.get_id()
        ).enqueue(new Callback<Spot>() {
            @Override
            public void onResponse(Call<Spot> call, Response<Spot> response) {
                switch (response.code()) {
                    case 200:
                        spot = response.body();
                        spot.calcDistance();
                        binding.setItem(spot);
                        break;
                    default:
                        Toast.makeText(MapActivity.this, "정보를 업데이트하는 데 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Spot> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MapActivity.this, "서버와의 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private View getReviewDialog() {
        final DialogAddStarBinding dialogBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_add_star, null, false);

        final ArrayList<IncludeBtnStarBinding> starBtnList = new ArrayList<>(Arrays.asList(dialogBinding.btnStar0, dialogBinding.btnStar1, dialogBinding.btnStar2, dialogBinding.btnStar3, dialogBinding.btnStar4));
        for (IncludeBtnStarBinding starBinding : starBtnList) {
            starBinding.group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean finded = false;
                    dialogScore = 0;
                    for (IncludeBtnStarBinding starBinding : starBtnList) {
                        if (!finded) {
                            dialogScore++;
                            starBinding.star.setImageResource(R.drawable.ic_review_star_big);
                            if (starBinding.group == view) finded = true;
                        } else {
                            starBinding.star.setImageResource(R.drawable.ic_review_star_big_none);
                        }
                    }
                }
            });
        }


        dialogBinding.btnReviewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataManager manager = DataManager.INSTANCE;
                NetworkHelper.getNetworkInstance().postSpotStar(
                        manager.getUser().getToken(), spot.get_id(),
                        dialogScore,
                        dialogBinding.editReviewTitle.getText().toString(),
                        dialogBinding.editReviewContent.getText().toString()
                ).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                Toast.makeText(MapActivity.this, "성공적으로 업로드되었습니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                dialogScore = 3;
                                reloadSpot();
                                break;
                            default:
                                Toast.makeText(MapActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                dialogScore = 3;
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(MapActivity.this, "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        dialogScore = 3;
                    }
                });
            }
        });


        return dialogBinding.getRoot();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        LatLng spotLocation = null;
        if (spot.getLocation().getLength() == 2) spotLocation = new LatLng(
                spot.getLocation().getLatitude(),
                spot.getLocation().getLongitude()
        );

//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_card_pin_on))
        if (spotLocation != null) {
            mMap.addMarker(new MarkerOptions()
                    .title(spot.getName())
                    .position(spotLocation)
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLng(spotLocation));
        }


        if (DataManager.INSTANCE.getLocation() != null) {
            LatLng myLocation = new LatLng(
                    DataManager.INSTANCE.getLocation().getLatitude(),
                    DataManager.INSTANCE.getLocation().getLongitude()
            );
            mMap.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .title("내 위치")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_mylocation))
            );


            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(myLocation);
            if (spotLocation != null) builder.include(spotLocation);
            int padding = getResources().getDimensionPixelSize(R.dimen.map_padding); // offset from edges of the map in pixels
            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), padding);

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.animateCamera(cu);
                }
            });

        } else {
            final CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(spotLocation, 16);
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.animateCamera(cu);
                }
            });
        }


        mMap.getUiSettings().setScrollGesturesEnabled(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
