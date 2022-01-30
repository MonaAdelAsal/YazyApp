package com.asc.yazy.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.asc.yazy.fragment.BookingAdultsInfoFragment;
import com.asc.yazy.fragment.BookingChildrenInfoFragment;
import com.asc.yazy.fragment.BookingContactInfoFragment;
import com.asc.yazy.fragment.BookingPassengersCountFragment;
import com.asc.yazy.fragment.BookingTravellersFragment;
import com.asc.yazy.interfaces.IFragmentCount;


public class BookingViewPagerAdapter extends FragmentStatePagerAdapter {


    private static final int NUM_ITEMS = 5;
    public static int type;


    public BookingViewPagerAdapter(FragmentManager fragmentManager) {
        //noinspection deprecation
        super(fragmentManager);

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment instanceof IFragmentCount) {
            if (type == AdultsObserverAdapter.ADULT_OBSERVER_TYPE)
                ((IFragmentCount) fragment).onAdultChangeListener();
            if (type == AdultsObserverAdapter.CHILD_OBSERVER_TYPE)
                ((IFragmentCount) fragment).onChildChangeListener();
        }
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BookingPassengersCountFragment();
            case 1:
                return new BookingTravellersFragment();
            case 2:
                return new BookingAdultsInfoFragment();
            case 3:
                return new BookingChildrenInfoFragment();
            case 4:
                return new BookingContactInfoFragment();


        }
        return null;

    }


}



