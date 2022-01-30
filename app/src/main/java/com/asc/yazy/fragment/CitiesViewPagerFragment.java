package com.asc.yazy.fragment;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.adapter.CitiesViewPagerAdapter;
import com.asc.yazy.api.model.CityModel;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentCitesBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;

import java.util.List;


public class CitiesViewPagerFragment extends Fragment implements IFragment, View.OnClickListener {

    private final List<CityModel> cityModelList;
    private final int open_package;
    private final int position;
    public CitiesViewPagerAdapter mainContainerViewPagerAdapter;
    private FragmentCitesBinding binding;

    public CitiesViewPagerFragment(List<CityModel> cityModelList, int position, int open_package) {
        this.cityModelList = cityModelList;
        this.position = position;
        this.open_package = open_package;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_cites, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.CITIES);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.imgBack.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new SetUpViewPagerTask().execute();
    }

    @Override
    public void onBack() {
       /* if (binding.mainContent.getCurrentItem() > 0)
            binding.mainContent.setCurrentItem(0);
        else {*/
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        //}
    }

    @Override
    public void onClick(View v) {

        if (v == null)
            return;
        if (v.getId() == R.id.imgBack) {
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        }

    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class SetUpViewPagerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mainContainerViewPagerAdapter = new CitiesViewPagerAdapter(open_package, cityModelList, getChildFragmentManager());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            binding.mainContent.setOffscreenPageLimit(cityModelList.size());
            binding.mainContent.setAdapter(mainContainerViewPagerAdapter);
            binding.mainContent.setCurrentItem(position);
            binding.indicatorView.setViewPager(binding.mainContent);

        }
    }


}
