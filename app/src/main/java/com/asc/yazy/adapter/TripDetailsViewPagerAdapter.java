package com.asc.yazy.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.asc.yazy.api.model.City;
import com.asc.yazy.api.model.TDetailsModel;
import com.asc.yazy.fragment.TripCityDetailsFragment;

import java.util.List;


public class TripDetailsViewPagerAdapter extends FragmentPagerAdapter {

    private final TDetailsModel model;
    private final List<City> cities;

    @SuppressWarnings("deprecation")
    public TripDetailsViewPagerAdapter(TDetailsModel model, List<City> cities, FragmentManager activity) {
        super(activity);
        this.model = model;
        this.cities = cities;
    }

    @Override
    public int getCount() {
        if (cities == null) return 0;
        return cities.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new TripCityDetailsFragment(cities.get(position), model);

    }


}



