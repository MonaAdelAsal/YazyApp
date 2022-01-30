package com.asc.yazy.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.asc.yazy.R;
import com.asc.yazy.fragment.CanceledTripsFragment;
import com.asc.yazy.fragment.ConfirmedTripsFragment;
import com.asc.yazy.fragment.PendingTripsFragment;
import com.asc.yazy.interfaces.IUpdatableFragment;


public class MyTripsPagerAdapter extends FragmentStatePagerAdapter {


    private static final int NUM_ITEMS = 3;
    private final Context context;

    @SuppressWarnings("deprecation")
    public MyTripsPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        Fragment fragment = (Fragment) object;
        System.out.println("getItemPosition " + fragment.getClass().getSimpleName());
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
            case 0: // Fragment # 0 - This will show FirstFragment
                return new PendingTripsFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new ConfirmedTripsFragment();
            case 2: // Fragment # 0 - This will show FirstFragment different title
                return new CanceledTripsFragment();
        }
        return null;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return context.getString(R.string.pending);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return context.getString(R.string.confirmed);
            case 2: // Fragment # 0 - This will show FirstFragment different title
                return context.getString(R.string.canceled);
        }
        return null;
    }

}


