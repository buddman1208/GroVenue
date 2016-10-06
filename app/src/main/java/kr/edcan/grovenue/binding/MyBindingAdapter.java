package kr.edcan.grovenue.binding;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by 최예찬 on 2016-10-05.
 */
public class MyBindingAdapter {

    @BindingAdapter({"bind_image"})
    public static void loadImage(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
    }

    @BindingAdapter("bind_weight")
    public static void loadWeight(View view, float weight) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.weight = weight;
        view.setLayoutParams(params);
    }
}
