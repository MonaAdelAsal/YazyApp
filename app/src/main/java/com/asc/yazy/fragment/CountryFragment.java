package com.asc.yazy.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieDrawable;
import com.asc.yazy.R;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.activity.SplashActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelCountry;
import com.asc.yazy.api.model.ModelCountryAPi;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentCountryBinding;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryFragment extends Fragment implements View.OnClickListener {

    private FragmentCountryBinding binding;
    private List<ModelCountry> countriesList;
    private ModelCountry country;
    private final AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (countriesList != null && countriesList.size() > 0) {
                country = countriesList.get(position);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_country, null, false);
        binding.likeAnimation.setRepeatMode(LottieDrawable.RESTART);
        binding.likeAnimation.setRepeatCount(10000);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.likeAnimation.playAnimation();
        binding.btnSubmit.setOnClickListener(this);
        boolean IsDefaultLanguage = getResources().getBoolean(R.bool.english_lan);
        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 || android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP)
            if (!IsDefaultLanguage)
                binding.spinner.setRotationY(180);
        AnalyticsUtility.logEvent(AnalyticsUtility.Events.COUNTRY, AnalyticsUtility.Events.ABOUT);
        AnalyticsUtility.setScreen(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDate();
    }

    private void loadDate() {

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelCountryAPi> call = mApiService.getCountries(Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelCountryAPi>() {
            @Override
            public void onResponse(@NonNull Call<ModelCountryAPi> call, @NonNull Response<ModelCountryAPi> response) {
                if (getActivity() == null)
                    return;
                if (CountryFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(CountryFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                ModelCountryAPi body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(CountryFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200 && body.getData() != null && body.getData() != null) {

                    if (body.getData().size() > 0) {
                        List<String> countriesName = new ArrayList<>();
                        for (ModelCountry temp : body.getData()) {
                            countriesName.add(temp.getName());
                        }
                        countriesList = body.getData();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spiner_item, countriesName);
                        adapter.setDropDownViewResource(R.layout.spiner_item);
                        binding.spinner.setAdapter(adapter);
                        binding.spinner.setOnItemSelectedListener(listener);
                    }
                } else {
                    GlobalInfoDialog.getInstance(CountryFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelCountryAPi> call, @NonNull Throwable t) {

                if (CountryFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                // GlobalInfoDialog.getInstance(CountryFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(CountryFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));


            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        if (v.getId() == R.id.btnSubmit) {
            if (CountryFragment.this.getActivity() == null)
                return;

            UtilsPreference.getInstance(CountryFragment.this.getActivity()).saveCountry(country);
            UtilsPreference.getInstance(CountryFragment.this.getActivity()).savePreference(Constants.IS_FIRST_RUN, false);
            CountryFragment.this.getActivity().startActivity(new Intent(CountryFragment.this.getActivity(), SplashActivity.class));
            CountryFragment.this.getActivity().finish();
        }
    }
}
