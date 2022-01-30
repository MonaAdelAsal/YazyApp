package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asc.yazy.R;
import com.asc.yazy.adapter.SearchHistoryAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelContinents;
import com.asc.yazy.api.model.ModelDestination;
import com.asc.yazy.api.model.ModelSearchDataAPI;
import com.asc.yazy.api.model.ModelTravelAgencySearch;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.cash.room.FullSearchHistoryRepository;
import com.asc.yazy.cash.room.SearchHistoryRepository;
import com.asc.yazy.cash.room.model.FullSearchHistoryModel;
import com.asc.yazy.cash.room.model.SearchHistoryModel;
import com.asc.yazy.cash.room.viewmodel.SearchHistoryRoomViewModel;
import com.asc.yazy.core.CalenderEnhancedDialog;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentSearchBinding;
import com.asc.yazy.interfaces.IFragmentAccess;
import com.asc.yazy.interfaces.OnDialogDismissListener;
import com.asc.yazy.interfaces.OnRecentSearchListener;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;


public class SearchFragment extends Fragment implements View.OnClickListener, OnRecentSearchListener, IFragmentAccess {

    private static final String TAG = "SearchFragment";
    // private long mLastClickTime = System.currentTimeMillis();
    private final List<ModelDestination> listDestination = new ArrayList<>();
    private final List<ModelTravelAgencySearch> listTravelAgency = new ArrayList<>();
    private final List<String> destinationName = new ArrayList<>();
    private final List<String> ContinentsName = new ArrayList<>();
    private final List<String> TAName = new ArrayList<>();
    private List<ModelContinents> listContinents = new ArrayList<>();
    private FragmentSearchBinding binding;
    private SearchHistoryAdapter searchHistoryAdapter;
    private GuideView caseView;
    private String travelAgencyID;
    private String cityID;
    private String ContinentID;
    private String date;
    private String dateTo;

    private int selectedMonth = -1;
    private int selectedYear = -1;
    private int selectedMonthTo = -1;
    private int selectedYearTo = -1;
    private final OnDialogDismissListener Listener = new OnDialogDismissListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void OnDialogDismissListener(int month, int year, String dateString, String dateValue, String dateType) {

            Log.d("Search", month + " " + year + " " + dateString + " " + dateValue + " " + dateType);
            if (dateType.equals(Constants.START_DATE)) {
                selectedMonth = month;
                selectedYear = year;
                binding.tvMonth.setText(dateString);
                date = dateValue;

            } else {
                selectedMonthTo = month;
                selectedYearTo = year;
                binding.tvMonthTo.setText(dateString);
                dateTo = dateValue;
            }


        }
    };

    public SearchFragment() {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: " + isVisibleToUser);
        if (isVisibleToUser) {
            final Handler handler = new Handler();
            handler.postDelayed(this::introViewBehaviour, 1000);
        } else if (caseView != null) {
            caseView.dismiss();
        }

    }

    private void getData() {
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelSearchDataAPI> call = mApiService.getSearchData(Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelSearchDataAPI>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onResponse(@NonNull Call<ModelSearchDataAPI> call, @NonNull Response<ModelSearchDataAPI> response) {
                if (getActivity() == null)
                    return;
                if (SearchFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(SearchFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelSearchDataAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(SearchFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200 && body.getData() != null && body.getData() != null) {

                    if (body.getData().getDestinations().size() > 0) {

                        for (int i = 0; i < body.getData().getDestinations().size(); i++) {
                            destinationName.add(body.getData().getDestinations().get(i).getName());
                            listDestination.add(new ModelDestination(body.getData().getDestinations().get(i).getName()));

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spiner_item, destinationName);
                        binding.tvDestination.setAdapter(adapter);
                       /* binding.tvDestination.setOnTouchListener((v, event) -> {
                            binding.tvDestination.showDropDown();
                            return false;
                        });*/
                    }
                    if (body.getData().getContinents().size() > 0) {

                        for (int i = 0; i < body.getData().getContinents().size(); i++) {
                            ContinentsName.add(body.getData().getContinents().get(i).getName());
                            listContinents = body.getData().getContinents();

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spiner_item, ContinentsName);
                        binding.tvContinent.setAdapter(adapter);
                        binding.tvContinent.setOnTouchListener((v, event) -> {
                            binding.tvContinent.showDropDown();
                            return false;
                        });

                    }
                    if (body.getData().getTravel_agencies().size() > 0) {

                        for (int i = 0; i < body.getData().getTravel_agencies().size(); i++) {
                            TAName.add(body.getData().getTravel_agencies().get(i).getName());
                            listTravelAgency.add(new ModelTravelAgencySearch(body.getData().getTravel_agencies().get(i).getId(), body.getData().getTravel_agencies().get(i).getName()));

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spiner_item, TAName);
                        binding.tvTA.setAdapter(adapter);
                        binding.tvTA.setOnTouchListener((v, event) -> {
                            binding.tvTA.showDropDown();
                            return false;
                        });
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelSearchDataAPI> call, @NonNull Throwable t) {

                //   GlobalInfoDialog.getInstance(SearchFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(SearchFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initData() {
        binding.tvDestination.setOnItemClickListener((parent, view, position, id) -> cityID = listDestination.get(position).getName());
        binding.tvTA.setOnItemClickListener((parent, view, position, id) -> travelAgencyID = listTravelAgency.get(position).getId());
        binding.tvContinent.setOnItemClickListener((parent, view, position, id) -> ContinentID = listContinents.get(position).getId());

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        AnalyticsUtility.setScreen(this);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.SEARCH);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();


        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("MM");
        Date Months = new Date();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Integer.parseInt(dateFormat.format(Months));
        binding.tvMonth.setText(getMonthName(currentMonth, currentYear));
        selectedYear = currentYear;
        selectedMonth = currentMonth;
        date = currentYear + "-" + currentMonth;

        binding.tvMonth.setOnClickListener(this);

        binding.btnSearch.setOnClickListener(this);
        binding.linearMonth.setOnClickListener(this);
        binding.layoutDate.setOnClickListener(this);
        binding.imgCancelDate.setOnClickListener(this);
        binding.linearMonthTo.setOnClickListener(this);
        binding.layoutDateTo.setOnClickListener(this);
        binding.tvMonthTo.setOnClickListener(this);
        binding.imgCancelDateTo.setOnClickListener(this);


        binding.tvSearchHistory.setOnClickListener(this);


        binding.imgCancelDestination.setOnClickListener(this);
        binding.imgCancelTA.setOnClickListener(this);
        binding.tvDestination.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.shadow));
        binding.tvContinent.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.shadow));
        binding.tvTA.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.shadow));
        binding.tvMonth.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgCancelDate.setVisibility(View.VISIBLE);

                } else {
                    binding.imgCancelDate.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        binding.tvMonthTo.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgCancelDateTo.setVisibility(View.VISIBLE);

                } else {
                    binding.imgCancelDateTo.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        binding.tvDestination.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (searchHistoryAdapter != null) {
                    binding.expandableRecentSearch.expand();
                    searchHistoryAdapter.filter(s.toString());
                }
                if (s.length() > 0) {
                    binding.imgCancelDestination.setVisibility(View.VISIBLE);
                    binding.imgLabel.setImageDrawable(SearchFragment.this.requireActivity().getResources().getDrawable(R.drawable.ic_exchange_active));
                } else {
                    binding.imgCancelDestination.setVisibility(View.INVISIBLE);
                    binding.imgLabel.setImageDrawable(SearchFragment.this.requireActivity().getResources().getDrawable(R.drawable.ic_exchange_in_active));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        binding.tvTA.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.imgCancelTA.setVisibility(View.VISIBLE);
                } else {
                    binding.imgCancelTA.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        getData();
        initData();
        bindSearchHistory();
        binding.tvDestination.clearFocus();
        return binding.getRoot();
    }

    private String getMonthName(int currentMonth, int Year) {
        if (currentMonth == 1)
            return SearchFragment.this.requireContext().getResources().getString(R.string.jan) + "-" + Year;
        else if (currentMonth == 2)
            return SearchFragment.this.requireContext().getResources().getString(R.string.feb) + "-" + Year;
        else if (currentMonth == 3)
            return SearchFragment.this.requireContext().getResources().getString(R.string.mar) + "-" + Year;
        else if (currentMonth == 4)
            return SearchFragment.this.requireContext().getResources().getString(R.string.apr) + "-" + Year;
        else if (currentMonth == 5)
            return SearchFragment.this.requireContext().getResources().getString(R.string.may) + "-" + Year;
        else if (currentMonth == 6)
            return SearchFragment.this.requireContext().getResources().getString(R.string.jun) + "-" + Year;
        else if (currentMonth == 7)
            return SearchFragment.this.requireContext().getResources().getString(R.string.jul) + "-" + Year;
        else if (currentMonth == 8)
            return SearchFragment.this.requireContext().getResources().getString(R.string.aug) + "-" + Year;
        else if (currentMonth == 9)
            return SearchFragment.this.requireContext().getResources().getString(R.string.sep) + "-" + Year;
        else if (currentMonth == 10)
            return SearchFragment.this.requireContext().getResources().getString(R.string.oct) + "-" + Year;
        else if (currentMonth == 11)
            return SearchFragment.this.requireContext().getResources().getString(R.string.nov) + "-" + Year;
        else if (currentMonth == 12)
            return SearchFragment.this.requireContext().getResources().getString(R.string.dec) + "-" + Year;
        return "";
    }


    private void introViewBehaviour() {

        if (SearchFragment.this.getContext() == null)
            return;

        boolean firstRun = UtilsPreference.getInstance(SearchFragment.this.getContext()).getPreference(Constants.IS_SEARCH_FIRST_RUN, true);

        if (!firstRun)
            return;


        Log.d(TAG, "introViewBehaviour: start");

        caseView = new GuideView.Builder(SearchFragment.this.getContext())
                .setTitle(Objects.requireNonNull(SearchFragment.this.getContext()).getResources().getString(R.string.intro_search))
                .setContentText(Objects.requireNonNull(SearchFragment.this.getContext()).getResources().getString(R.string.intro_search_details))
                .setTargetView(binding.layoutSearchContainer)
                .setGravity(Gravity.center)//optional
                .setDismissType(DismissType.anywhere)
                .build();

        caseView.dismiss();
        caseView.show();

        UtilsPreference.getInstance(SearchFragment.this.getContext()).savePreference(Constants.IS_SEARCH_FIRST_RUN, false);


    }

    /* @RequiresApi(api = Build.VERSION_CODES.O)
     @SuppressLint("SetTextI18n")
     private void openDatePicker(String dateType, int Month, int year) {
         new CalenderDialog(Objects.requireNonNull(getActivity()), Listener, dateType, Month, year);
     }
 */


    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v == null || getActivity() == null)
            return;
        String sortBy = "";
        switch (v.getId()) {
            case R.id.btnSearch:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH);
                binding.expandableRecentSearch.collapse();
                cityID = binding.tvDestination.getText().toString().trim();
                // ContinentID = binding.tvContinent.getText().toString().trim();
                Log.d("searchData", travelAgencyID + " " + cityID.length() + " " + date + " " + dateTo + " " + " " + "Continet " + ContinentID);

                if (cityID == null || cityID.trim().isEmpty()) {
                    GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.hint)).setMessage(getString(R.string.des_req)).show();
                    return;
                } else if (cityID.length() < 3 && cityID.length() > 0) {
                    GlobalInfoDialog.getInstance(getActivity()).setTitle(getString(R.string.hint)).setMessage(getString(R.string.valid_destination)).show();
                    return;
                }
                createRecentSearchItem(cityID);
                createSearchHistory(cityID, date, dateTo);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new SearchResultFragment(travelAgencyID, cityID, date, dateTo, "", "", sortBy, "", "", ContinentID))
                        .addToBackStack("SearchResultFragment")
                        .commit();

                break;
            case R.id.tvSearchHistory:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_HISTORY);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new SearchHistoryFragment())
                        .addToBackStack("SearchHistoryFragment")
                        .commit();
                break;
            case R.id.linearMonth:
            case R.id.layoutDate:
            case R.id.tvMonth:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_DATE_FROM);
                binding.expandableRecentSearch.collapse();
                new CalenderEnhancedDialog(Objects.requireNonNull(SearchFragment.this.getContext()), selectedMonth, selectedYear, selectedMonthTo, selectedYearTo, Listener, Constants.START_DATE);
                //  openDatePicker(Constants.START_DATE, selectedMonth, selectedYear);
                break;

            case R.id.linearMonthTo:
            case R.id.layoutDateTo:
            case R.id.tvMonthTo:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_DATE_TO);
                binding.expandableRecentSearch.collapse();
                new CalenderEnhancedDialog(Objects.requireNonNull(SearchFragment.this.getContext()), selectedMonthTo, selectedYearTo, selectedMonth, selectedYear, Listener, Constants.END_DATE);
                // openDatePicker(Constants.END_DATE, selectedMonthTo, selectedYearTo);
                break;
            case R.id.imgCancelDate:
                @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("MM");
                Date Months = new Date();
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int currentMonth = Integer.parseInt(dateFormat.format(Months));
                binding.tvMonth.setText(getMonthName(currentMonth, currentYear));
                selectedYear = currentYear;
                selectedMonth = currentMonth;
                date = currentYear + "-" + currentMonth;
                break;
            case R.id.imgCancelDateTo:
                binding.tvMonthTo.getText().clear();
                selectedYearTo = -1;
                selectedMonthTo = -1;
                dateTo = null;
                break;
            case R.id.imgCancelDestination:
                binding.tvDestination.getText().clear();
                cityID = null;
                break;
            case R.id.imgCancelTA:
                binding.tvTA.getText().clear();
                travelAgencyID = null;
                break;
            case R.id.tvDestination:
            case R.id.layoutTvDestination:
            case R.id.layoutDestination:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_DESTINATION);
                binding.expandableRecentSearch.expand();
                break;
            case R.id.tvTA:
            case R.id.layoutTA:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_TRAVEL_AGENCY);
                binding.expandableRecentSearch.collapse();
                break;
            case R.id.LinearContinent:
            case R.id.imgMore:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_TRAVEL_AGENCY);
                binding.expandableRecentSearch.collapse();
                binding.tvContinent.showDropDown();
                break;


        }
    }

    private void createSearchHistory(String cityID, String date, String dateTo) {
        if (cityID == null || cityID.trim().isEmpty())
            return;
        if ((date == null || date.trim().isEmpty()) && (dateTo == null || dateTo.trim().isEmpty()))
            return;


        FullSearchHistoryRepository repository = new FullSearchHistoryRepository(getActivity());
        FullSearchHistoryModel model = new FullSearchHistoryModel();
        model.setDestination(cityID);
        model.setDataFrom(date);
        model.setDataFromValue(binding.tvMonth.getText().toString());
        model.setDataTo(dateTo);
        model.setDataToValue(binding.tvMonthTo.getText().toString());


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        String time = df.format(Calendar.getInstance().getTime());
        model.setTime(time);

        repository.insert(model);
    }

    private void createRecentSearchItem(String text) {
        SearchHistoryRepository historyRepository = new SearchHistoryRepository(SearchFragment.this.getContext());
        SearchHistoryModel historyModel = new SearchHistoryModel();
        historyModel.setDestination(text);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        String date = df.format(Calendar.getInstance().getTime());
        historyModel.setDate(date);

        historyRepository.insert(historyModel);
    }

    private void bindSearchHistory() {

        SearchHistoryRoomViewModel notificationRoomViewModel = ViewModelProviders.of(SearchFragment.this).get(SearchHistoryRoomViewModel.class);
        notificationRoomViewModel.getAllNotifications().observe(SearchFragment.this, searchHistoryModels -> {

            if (searchHistoryModels == null || searchHistoryModels.size() == 0) {


                return;
            }
            searchHistoryAdapter = new SearchHistoryAdapter(searchHistoryModels, SearchFragment.this);
            binding.rvRecentSearch.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvRecentSearch.setAdapter(searchHistoryAdapter);
            binding.expandableRecentSearch.collapse();

            binding.tvDestination.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus)
                    binding.expandableRecentSearch.expand();
            });
            binding.tvDestination.setOnClickListener(SearchFragment.this);
            binding.layoutDestination.setOnClickListener(SearchFragment.this);
            binding.layoutTvDestination.setOnClickListener(SearchFragment.this);

            binding.tvTA.setOnClickListener(SearchFragment.this);
            binding.layoutTA.setOnClickListener(SearchFragment.this);
            binding.imgMore.setOnClickListener(SearchFragment.this);
            binding.LinearContinent.setOnClickListener(SearchFragment.this);

        });
    }

    @Override
    public void onRecentSearchItemClicked(SearchHistoryModel historyModel) {

        if (historyModel == null)
            return;

        if (historyModel.getDestination().trim().isEmpty())
            return;
        AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_RECENT_SEARCH_SELECTED);
        binding.tvDestination.setText(historyModel.getDestination());
        binding.tvDestination.clearFocus();
        binding.expandableRecentSearch.collapse();

    }

    @Override
    public boolean onBackAccess() {
        return true;
    }


    /*
    @Override
    public void onFragmentUpdate() {

        if (caseView != null && caseView.isShowing()) {
            caseView.dismiss();
        }
    }
    */
}
