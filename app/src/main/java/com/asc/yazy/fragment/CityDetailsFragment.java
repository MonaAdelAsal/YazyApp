package com.asc.yazy.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.asc.yazy.R;
import com.asc.yazy.adapter.AccommodationAdapter;
import com.asc.yazy.adapter.ViewPagerAdapter;
import com.asc.yazy.api.model.CityModel;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CityDetailsFragment extends Fragment {

    //private static final String TAG = "CityDetailsFragment";
    private final CityModel cityModel;
    private final String fromCityName;
    private final int open_package;
    List<String> list = new ArrayList<>();

    public CityDetailsFragment(List<CityModel> fragmentList, int position, int open_package) {
        this.cityModel = fragmentList.get(position);
        this.open_package = open_package;
        if (position > 0) {
            this.fromCityName = fragmentList.get(position - 1).getCity_name();
        } else {
            this.fromCityName = fragmentList.get(position).getCountry_name();
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.asc.yazy.databinding.FragmentCityDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city_details, container, false);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.CITY_DETAILS);
        AnalyticsUtility.setScreen(this);
        binding.tvDescription.stopShimmerAnimation();
        binding.tvDescriptionDate.setBackground(null);
        binding.loaderDate.stopShimmerAnimation();
        binding.tvDate.setBackground(null);
        binding.tvFlightName.stopShimmerAnimation();
        binding.tvFlightData.setBackground(null);
        binding.loaderHotel.stopShimmerAnimation();
        binding.tvHotelData.setBackground(null);
        binding.tvRoomType.stopShimmerAnimation();
        binding.tvRoomTypeData.setBackground(null);
        binding.tvClass.stopShimmerAnimation();
        binding.tvClassData.setBackground(null);
        binding.tvFromCity.setText(fromCityName);
        if (open_package == 1 && cityModel.getDuration() != null) {
            binding.tvDate.setText(cityModel.getDuration());

        } else {
            binding.tvDate.setText(Constants.getFormattedDate(cityModel));
        }
        binding.setDetailsData(cityModel);
        list.add(cityModel.getImage());
        binding.vpImages.setAdapter(new ViewPagerAdapter(list, getActivity()));
        if (cityModel.getImage() != null && !cityModel.getImage().trim().isEmpty())
            Glide.with(Objects.requireNonNull(CityDetailsFragment.this.getContext())).asBitmap().load(cityModel.getImage()).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.color.gray).into(binding.img);
        else
            binding.img.setImageResource(R.color.gray);


        if (cityModel.getAccommodation_services() != null && cityModel.getAccommodation_services().size() > 0) {
            AccommodationAdapter offersAdapter = new AccommodationAdapter(cityModel.getAccommodation_services());
            binding.rvAccommodationServices.setLayoutManager(new GridLayoutManager(getContext(), 1));
            binding.rvAccommodationServices.setHasFixedSize(true);
            binding.rvAccommodationServices.setAdapter(offersAdapter);
            binding.tvAccommodationServices.setVisibility(View.VISIBLE);
            binding.rvAccommodationServices.setVisibility(View.VISIBLE);
        } else {
            binding.tvAccommodationServices.setVisibility(View.GONE);
            binding.rvAccommodationServices.setVisibility(View.GONE);
        }


        return binding.getRoot();

    }


}
