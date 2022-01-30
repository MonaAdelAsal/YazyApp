package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.adapter.MyTripsPagerAdapter;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentViewPagerBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.IFragmentAccess;
import com.asc.yazy.utils.AnalyticsUtility;


public class MyTripsPagerFragment extends Fragment implements View.OnClickListener, IFragment, IFragmentAccess {

    @SuppressLint("StaticFieldLeak")
    public static MyTripsPagerAdapter basketViewPagerAdapter;
    private FragmentViewPagerBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_view_pager, null, false);
        AnalyticsUtility.logEvent(AnalyticsUtility.Events.MY_TRIPS, AnalyticsUtility.Events.MY_TRIPS);
        AnalyticsUtility.setScreen(this);
        binding.imgBack.setOnClickListener(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        new SetUpViewPagerTask().execute();
        return binding.getRoot();

    }


    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        if (v.getId() == R.id.imgBack) {
            if (getActivity() == null)
                return;
            if (binding.vpPager.getCurrentItem() > 0) {
                binding.vpPager.setCurrentItem((binding.vpPager.getCurrentItem() - 1));
            } else
                getActivity().finish();
        }
    }

    @Override
    public void onBack() {
        if (getActivity() == null)
            return;
        if (binding.vpPager.getCurrentItem() > 0) {
            binding.vpPager.setCurrentItem((binding.vpPager.getCurrentItem() - 1));
        } else
            getActivity().finish();
    }

    @Override
    public boolean onBackAccess() {
        if (binding.vpPager.getCurrentItem() > 0) {
            binding.vpPager.setCurrentItem(0);
            return false;
        } else {
            binding.vpPager.setCurrentItem((binding.vpPager.getCurrentItem() - 1));
            return true;
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class SetUpViewPagerTask extends AsyncTask<Void, Void, Void> {
        AppCompatActivity context;

        @Override
        protected void onPreExecute() {
            binding.progressBar.setVisibility(View.VISIBLE);
            context = (AppCompatActivity) getActivity();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            basketViewPagerAdapter = new MyTripsPagerAdapter(context, getChildFragmentManager());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            binding.vpPager.setAdapter(basketViewPagerAdapter);
            binding.vpPager.setOffscreenPageLimit(3);
            binding.progressBar.setVisibility(View.GONE);
        }
    }


}
