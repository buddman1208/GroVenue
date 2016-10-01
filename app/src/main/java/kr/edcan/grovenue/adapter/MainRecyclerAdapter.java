package kr.edcan.grovenue.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import kr.edcan.grovenue.R;
import kr.edcan.grovenue.databinding.MainRecyclerviewContentBinding;
import kr.edcan.grovenue.model.Place;
import kr.edcan.grovenue.model.Spot;

/**
 * Created by JunseokOh on 2016. 8. 27..
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private ArrayList<Spot> arrayList;
    private LayoutInflater inflater;
    private Context context;
    private MainRecyclerviewContentBinding binding;
    private Spot data;
    public MainRecyclerAdapter(Context context, ArrayList<Spot> items) {
        this.context = context;
        this.arrayList = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_recyclerview_content, parent, false);
        return new ViewHolder(binding.getRoot());
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        data = arrayList.get(position);
        if(position==0){
            holder.header.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.GONE);
        } else {
            holder.header.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.VISIBLE);
            holder.title.setText(data.getName());
            holder.subtitle.setText(data.getBusinessDetail());
            holder.targetImage.setImageResource(
                    new int[]{R.drawable.rsz_mockup_haejangguk, R.drawable.rsz_mockup_hotel}[position%2]);
            holder.targetImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout header;
        CardView cardView;
        TextView title, subtitle;
        ImageView targetImage;
        public ViewHolder(View itemView) {
            super(itemView);
            targetImage = binding.mainRecyclerImage;
            header = binding.mainRecyclerHeader;
            cardView = binding.mainRecyclerCardView;
            title = binding.mainRecyclerTitle;
            subtitle = binding.mainRecyclerSubTitle;
        }
    }
}