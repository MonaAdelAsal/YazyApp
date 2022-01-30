package com.asc.yazy.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.asc.yazy.api.model.CityModel;
import com.asc.yazy.fragment.CityDetailsFragment;

import java.util.List;


public class CitiesViewPagerAdapter extends FragmentPagerAdapter {

    private final List<CityModel> fragmentList;
    private final int open_package;

    public CitiesViewPagerAdapter(int open_package, List<CityModel> fragmentList, FragmentManager fragmentManager) {
        //noinspection deprecation
        super(fragmentManager);
        this.fragmentList = fragmentList;
        this.open_package = open_package;
    }

    @Override
    public int getCount() {
        if (fragmentList == null) return 0;
        return fragmentList.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new CityDetailsFragment(fragmentList, position, open_package);

    }


}



