package com.asc.yazy.core;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asc.yazy.R;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.FlightClassAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelSearchDataAPI;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.databinding.FragmentFilterDialogBinding;
import com.asc.yazy.fragment.SearchResultFragment;
import com.asc.yazy.interfaces.FlightClassListener;
import com.asc.yazy.interfaces.OnFilterDoneListener;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterDialog extends Dialog implements View.OnClickListener {

    private final Dialog filters;
    private final FragmentFilterDialogBinding binding;
    private final Context context;
    private final OnFilterDoneListener listener;
    private String flightClass = "";
    private String maxPrice = "";
    private String IntialmaxPrice = "";
    private int flightClassCount = 0;
    private boolean isMultiCity = false;
    private boolean isSingleCity = false;
    private boolean isOpen = false;
    private boolean isFixed = false;
    private String accommodations = "";
    private final FlightClassListener flightClassListener = (Class, checked) -> {
        if (checked) {
            if (flightClass.length() > 0) {
                flightClass += ",";
            }
            flightClass += Class;
        } else {
            if (flightClass.contains("," + Class)) {
                flightClass = flightClass.replace("," + Class, "");
            } else {
                flightClass = flightClass.replace(Class, "");
            }
        }
        updateApplyButtonColor();
    };

    public FilterDialog(@NonNull Context context, OnFilterDoneListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        filters = new Dialog(Objects.requireNonNull(context), R.style.DialogStyle);
        filters.setCancelable(true);
        Objects.requireNonNull(filters.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_filter_dialog, null, false);
        filters.setContentView(binding.getRoot());
        binding.tvCurrencyMax.setInAnimation(getContext(), android.R.anim.slide_in_left);
        binding.tvCurrencyMax.setOutAnimation(getContext(), android.R.anim.slide_out_right);
        binding.tvApply.setOnClickListener(this);
        binding.tvClear.setOnClickListener(this);
        binding.tvMultiCity.setOnClickListener(this);
        binding.tvSingleCity.setOnClickListener(this);
        binding.tvOpen.setOnClickListener(this);
        binding.tvFixed.setOnClickListener(this);
        updateApplyButtonColor();
        Window window = filters.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        binding.etAccommodation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                accommodations = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateApplyButtonColor();
            }
        });
        getData();

    }

    private void updateApplyButtonColor() {
        if (!SearchResultFragment.isSearched && (flightClassCount == 4 || flightClassCount == 0) && !isOpen && !isFixed && !isMultiCity && !isSingleCity && flightClass.isEmpty() && maxPrice.isEmpty() && accommodations.trim().isEmpty()) {
            binding.tvClear.setTextColor(context.getResources().getColor(R.color.gray_bold));
            binding.tvApply.setBackgroundResource(R.drawable.border_solid_gray);
        } else {
            binding.tvClear.setTextColor(context.getResources().getColor(R.color.colorAccent));
            binding.tvApply.setBackgroundResource(R.drawable.border_solid);
        }
    }

    public void showFiltrationOptions() {
        filters.show();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        if (v == null || context == null)
            return;
        String type;
        String duration;
        switch (v.getId()) {
            case R.id.tvMultiCity:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_MULTI_CITY);
                if (isMultiCity) {
                    binding.tvMultiCity.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
                    binding.tvMultiCity.setTextColor(context.getResources().getColor(R.color.black));
                    isMultiCity = false;

                } else {
                    binding.tvMultiCity.setBackground(context.getResources().getDrawable(R.drawable.solid_blue));
                    binding.tvMultiCity.setTextColor(context.getResources().getColor(R.color.white));
                    isMultiCity = true;
                }
                updateApplyButtonColor();
                break;
            case R.id.tvSingleCity:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_SINGLE_CITY);
                if (isSingleCity) {
                    binding.tvSingleCity.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
                    binding.tvSingleCity.setTextColor(context.getResources().getColor(R.color.black));
                    isSingleCity = false;

                } else {
                    binding.tvSingleCity.setBackground(context.getResources().getDrawable(R.drawable.solid_blue));
                    binding.tvSingleCity.setTextColor(context.getResources().getColor(R.color.white));
                    isSingleCity = true;
                }
                updateApplyButtonColor();
                break;
            case R.id.tvOpen:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_OPEN);

                if (isOpen) {
                    binding.tvOpen.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
                    binding.tvOpen.setTextColor(context.getResources().getColor(R.color.black));
                    isOpen = false;

                } else {
                    binding.tvOpen.setBackground(context.getResources().getDrawable(R.drawable.solid_blue));
                    binding.tvOpen.setTextColor(context.getResources().getColor(R.color.white));
                    isOpen = true;
                }
                updateApplyButtonColor();
                break;
            case R.id.tvFixed:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_FIXED);
                if (isFixed) {
                    binding.tvFixed.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
                    binding.tvFixed.setTextColor(context.getResources().getColor(R.color.black));
                    isFixed = false;

                } else {
                    binding.tvFixed.setBackground(context.getResources().getDrawable(R.drawable.solid_blue));
                    binding.tvFixed.setTextColor(context.getResources().getColor(R.color.white));
                    isFixed = true;
                }
                updateApplyButtonColor();
                break;
            case R.id.tvApply:
                if (isMultiCity && isSingleCity || !isMultiCity && !isSingleCity) {
                    type = "";
                } else if (isSingleCity) {
                    type = "single";
                } else {
                    type = "multi";
                }
                if (isOpen && isFixed || !isOpen && !isFixed) {
                    duration = "";
                } else if (isOpen) {
                    duration = "open";
                } else {
                    duration = "fixed";
                }
                if (!SearchResultFragment.isSearched && (flightClassCount == 4 || flightClassCount == 0) && !isOpen && !isFixed && !isMultiCity && !isSingleCity && flightClass.isEmpty() && maxPrice.isEmpty())
                    break;
                listener.onFilterDoneListener(flightClassCount, flightClass, type, duration, maxPrice, accommodations);
                filters.dismiss();
                break;
            case R.id.tvClear:
                flightClass = "";
                type = "";
                duration = "";
                flightClassCount = 0;
                isMultiCity = false;
                isSingleCity = false;
                isOpen = false;
                isFixed = false;
                resetAllButtons();
                listener.onFilterDoneListener(flightClassCount, flightClass, type, duration, maxPrice, accommodations);
                filters.dismiss();

                break;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void resetAllButtons() {
        getData();
        binding.tvCurrencyMax.setText(String.valueOf(IntialmaxPrice));
        binding.tvFixed.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
        binding.tvFixed.setTextColor(context.getResources().getColor(R.color.black));

        binding.tvOpen.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
        binding.tvOpen.setTextColor(context.getResources().getColor(R.color.black));

        binding.tvMultiCity.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
        binding.tvMultiCity.setTextColor(context.getResources().getColor(R.color.black));

        binding.tvSingleCity.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
        binding.tvSingleCity.setTextColor(context.getResources().getColor(R.color.black));


    }

    private void getData() {
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelSearchDataAPI> call = mApiService.getSearchData(Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelSearchDataAPI>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onResponse(@NonNull Call<ModelSearchDataAPI> call, @NonNull Response<ModelSearchDataAPI> response) {

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelSearchDataAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200 && body.getData() != null && body.getData() != null) {

                    if (body.getData().getFlight_class() != null && body.getData().getFlight_class() != null && body.getData().getFlight_class().size() > 0) {
                        FlightClassAdapter offersAdapter = new FlightClassAdapter(context, body.getData().getFlight_class(), flightClassListener);
                        flightClassCount = body.getData().getFlight_class().size();
                        binding.rvFlightClasses.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                        binding.rvFlightClasses.setItemAnimator(new DefaultItemAnimator());
                        binding.rvFlightClasses.setHasFixedSize(true);
                        binding.rvFlightClasses.setAdapter(offersAdapter);
                    }

                    if (body.getData().getPrice_limits() != null) {

                        binding.tvCurrency.setText(UtilsPreference.getInstance(context).getPreference(Constants.COUNTRY_CURRENCY, context.getResources().getString(R.string.currency)));
                        binding.tvCurrencyCurrency.setText(UtilsPreference.getInstance(context).getPreference(Constants.COUNTRY_CURRENCY, context.getResources().getString(R.string.currency)));
                        binding.seekBarPriceRange.setMax(Integer.parseInt(body.getData().getPrice_limits().getMAX_PRICE()));
                        binding.seekBarPriceRange.setProgress(Integer.parseInt(body.getData().getPrice_limits().getMIN_PRICE()));
                        binding.tvMinPrice.setText(body.getData().getPrice_limits().getMIN_PRICE());
                        binding.seekBarPriceRange.setSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                maxPrice = String.valueOf(progress);
                                Toast.makeText(context, maxPrice + "", Toast.LENGTH_LONG).show();
                                IntialmaxPrice = String.valueOf(progress);
                                binding.tvCurrencyMax.setText(String.valueOf(progress));
                                updateApplyButtonColor();
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }
                        });

                    } else {
                        GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(body.getMessage()).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelSearchDataAPI> call, @NonNull Throwable t) {
                if (context == null)
                    return;
                // GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getResources().getString(R.string.cant_connect)).show();
                context.startActivity(new Intent(context, NoNetActivity.class));
            }
        });
    }

}