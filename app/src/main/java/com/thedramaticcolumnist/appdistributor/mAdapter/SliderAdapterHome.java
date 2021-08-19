package com.thedramaticcolumnist.appdistributor.mAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.thedramaticcolumnist.appdistributor.R;
import com.thedramaticcolumnist.appdistributor.models.SliderData;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterHome extends SliderViewAdapter<SliderAdapterHome.SliderAdapterHomeViewHolder> {


    private final List<SliderData> mSliderItems;

    public SliderAdapterHome(Context context, ArrayList<SliderData> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    @Override
    public SliderAdapterHomeViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterHomeViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterHomeViewHolder viewHolder, int position) {

        final SliderData sliderItem = mSliderItems.get(position);

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImgUrl())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageViewBackground);
    }


    static class SliderAdapterHomeViewHolder extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterHomeViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }
    }
}