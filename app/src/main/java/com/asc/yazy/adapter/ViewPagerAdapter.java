package com.asc.yazy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.asc.yazy.R;
import com.bumptech.glide.Glide;

import java.util.List;


public class ViewPagerAdapter extends PagerAdapter {

    private final Context context;
    private final List<String> sliderImg;


    public ViewPagerAdapter(List<String> sliderImg, Context context) {
        this.sliderImg = sliderImg;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (sliderImg == null) return 0;
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.images_cell, null);
        if(sliderImg!=null){
            if (sliderImg.get(position) != null && !sliderImg.get(position).trim().isEmpty()){
                ImageView img=view.findViewById(R.id.img);
                Glide.with(this.context)
                        .load(sliderImg.get(position))
                       // .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                        .into(img);
            }
        }


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}