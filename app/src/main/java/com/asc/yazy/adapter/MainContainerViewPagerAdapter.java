package com.asc.yazy.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.asc.yazy.fragment.Home3Fragment;
import com.asc.yazy.fragment.NotificationsFragment;
import com.asc.yazy.fragment.ProfileFragment;
import com.asc.yazy.fragment.SearchFragment;
import com.asc.yazy.fragment.SettingsFragment;
import com.asc.yazy.interfaces.IUpdatableFragment;


public class MainContainerViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_ITEMS = 5;

    @SuppressWarnings("deprecation")
    public MainContainerViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment instanceof IUpdatableFragment)
            ((IUpdatableFragment) fragment).onFragmentUpdate();
        return super.getItemPosition(object);
    }
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Home3Fragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new ProfileFragment();
            case 3:
                return new NotificationsFragment();
            case 4:
                return new SettingsFragment();

        }
        return null;

    }


}



