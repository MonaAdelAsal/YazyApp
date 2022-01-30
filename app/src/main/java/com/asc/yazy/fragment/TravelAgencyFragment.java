package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asc.yazy.R;
import com.asc.yazy.adapter.PhonesAdapter;
import com.asc.yazy.api.model.ModelTravelAgency;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentTravelAgencyBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.asc.yazy.utils.UtilsShare;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Objects;

public class TravelAgencyFragment extends Fragment implements View.OnClickListener , IFragment {


    private long mLastClickTime = System.currentTimeMillis();
    private ModelTravelAgency travelAgency;


    public TravelAgencyFragment() {
    }

    TravelAgencyFragment(ModelTravelAgency travelAgency) {
        this.travelAgency = travelAgency;
    }

    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentTravelAgencyBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_travel_agency, container, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.TRAVEL_AGENCY_PROFILE);
        AnalyticsUtility.setScreen(this);
        binding.setTravelAgency(travelAgency);
        if (travelAgency != null && travelAgency.getImage() != null && !travelAgency.getImage().trim().isEmpty())
            Glide.with(TravelAgencyFragment.this).asBitmap().load(travelAgency.getImage()).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.color.gray).into(binding.img);
        else
            binding.img.setImageResource(R.color.gray);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
       // binding.rate.setRating(Float.parseFloat(String.valueOf(Math.floor(travelAgency.getTotal_rate()))));
        binding.rate.setRating(Float.parseFloat(String.valueOf(Math.floor(travelAgency.getTotal_rate()))));
        binding.layoutEmail.setOnClickListener(this);
        binding.layoutLocation.setOnClickListener(this);
        binding.layoutPhone.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.btnSearch.setOnClickListener(this);
        binding.layoutRate.setOnClickListener(this);
        List<String> phones = travelAgency.getPhones();
        PhonesAdapter PhonesAdapter = new PhonesAdapter(phones, true, getActivity());
        binding.rvPhones.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPhones.setHasFixedSize(true);
        binding.rvPhones.setAdapter(PhonesAdapter);
        return binding.getRoot();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        long now = System.currentTimeMillis();
        if (now - mLastClickTime < Constants.CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;
        switch (v.getId()){
            case R.id.imgBack:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.layoutLocation:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_TRAVEL_AGENCY_LOCATION);
                if (travelAgency.getAddress() != null && !travelAgency.getAddress().isEmpty()) {
                    UtilsShare.openMap(getActivity(), travelAgency.getAddress());
                } else {
                    Toast.makeText(TravelAgencyFragment.this.getContext(), Objects.requireNonNull(TravelAgencyFragment.this.getContext()).getResources().getString(R.string.can_not_perfrom), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.layoutEmail:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_TRAVEL_AGENCY_EMAIL);
                if (travelAgency != null && travelAgency.getEmail() != null && !travelAgency.getEmail().isEmpty()) {
                    UtilsShare.sendEmail(getActivity(), getResources().getString(R.string.app_name), travelAgency.getEmail());
                } else {
                    Toast.makeText(TravelAgencyFragment.this.getContext(), Objects.requireNonNull(TravelAgencyFragment.this.getContext()).getResources().getString(R.string.can_not_perfrom), Toast.LENGTH_SHORT).show();
                }
                break;
          /*  case R.id.layoutPhone:
                if (travelAgency != null && travelAgency.getContact_number() != null && !travelAgency.getContact_number().isEmpty()) {
                    UtilsShare.dial(getActivity(), travelAgency.getContact_number());
                } else {
                    Toast.makeText(TravelAgencyFragment.this.getContext(), Objects.requireNonNull(TravelAgencyFragment.this.getContext()).getResources().getString(R.string.can_not_perfrom), Toast.LENGTH_SHORT).show();
                }
                break;*/
            case R.id.btnSearch:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_SEARCH);
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new SearchResultFragment(travelAgency.getTravel_agency_id(), null, null, null, null, null, null, null, null, null))
                            .addToBackStack("TAFragment")
                            .commit();
                break;
            case R.id.layoutRate:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_RATE);
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new RatingFragment(travelAgency))
                            .addToBackStack("RatingFragment")
                            .commit();
                break;
        }
    }

    @Override
    public void onBack() {

        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }


}
