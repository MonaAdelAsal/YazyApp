package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.adapter.OffersAdapter;
import com.asc.yazy.adapter.OffersGridAdapter;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.pagination.PaginationProvider;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.api.pagination.offers.OffersPagedAdapter;
import com.asc.yazy.api.pagination.offers.OffersPagedGridAdapter;
import com.asc.yazy.api.pagination.onLoadData;
import com.asc.yazy.api.pagination.search.PagedOffersSearchViewModel;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.cash.room.model.OfferRoomModel;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.FilterDialog;
import com.asc.yazy.core.SortDialog;
import com.asc.yazy.databinding.FragmentSearchResultBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.OnFilterDoneListener;
import com.asc.yazy.interfaces.OnOfferItemListener;
import com.asc.yazy.interfaces.OnSortChangeListener;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchResultFragment extends Fragment implements IFragment, SwipeRefreshLayout.OnRefreshListener, OnOfferItemListener, View.OnClickListener {

    public static boolean isSearched = false;
    private long mLastClickTime = System.currentTimeMillis();
    private FragmentSearchResultBinding binding;
    private OffersPagedAdapter allStoresPagedAdapter;
    private OffersPagedGridAdapter offersPagedGridAdapter;
    private PagedOffersSearchViewModel itemViewModel;
    private String travelAgencyID;
    private String cityID;
    private String date;
    private String dateTo;
    private String flightClass;
    private String maxPrice;
    private String sortBy;
    private String type;
    private String duration;
    private String tvAccommodations;
    private String continent;
    private SortDialog dialogFrag;
    private FilterDialog filterDialog;
    private boolean isGridView = false;
    private final onLoadData updateData = new onLoadData() {
        @Override
        public void onDataLoaded(int count) {

            switch (count) {
                case NetworkState.UN_AUTHORISE:
                    if (getActivity() == null)
                        return;
                    binding.layoutNoResult.getRoot().setVisibility(View.GONE);
                    binding.layoutNoNetWork.getRoot().setVisibility(View.GONE);
                    loadingDate();
                    Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    getActivity().startActivity(intent);
                    break;
                case NetworkState.LOADING:
                    binding.layoutNoResult.getRoot().setVisibility(View.GONE);
                    binding.layoutNoNetWork.getRoot().setVisibility(View.GONE);
                    loadingDate();
                    break;
                case NetworkState.NO_NET:
                    binding.layoutNoResult.getRoot().setVisibility(View.GONE);
                    binding.layoutNoNetWork.getRoot().setVisibility(View.VISIBLE);
                    break;
                case 0:
                    binding.layoutNoResult.getRoot().setVisibility(View.VISIBLE);
                    binding.layoutControllers.setVisibility(View.GONE);
                    break;
                case NetworkState.LOADED:
                default:
                    binding.layoutControllers.setVisibility(View.VISIBLE);
                    binding.layoutNoResult.getRoot().setVisibility(View.GONE);
                    binding.layoutNoNetWork.getRoot().setVisibility(View.GONE);
                    if (isGridView)
                        binding.rvOffers.setAdapter(offersPagedGridAdapter);
                    else
                        binding.rvOffers.setAdapter(allStoresPagedAdapter);


            }
        }
    };
    private PagedList<ModelOffer> dataList;
    private final OnFilterDoneListener listener = new OnFilterDoneListener() {
        @Override
        public void onFilterDoneListener(int FlightClassCount, String FlightClass, String TripType, String TripDuration, String TripPrice, String accommodations) {
            if ((FlightClass.split(",", -1).length - 1) == FlightClassCount - 1)
                FlightClass = "";
            flightClass = FlightClass;
            type = TripType;
            duration = TripDuration;
            maxPrice = TripPrice;
            tvAccommodations = accommodations;
            //isSearched = true;
            loadPagedData();
        }
    };
    private final OnSortChangeListener Listener = new OnSortChangeListener() {
        @Override
        public void onSortChangeListener(String sort) {
            sortBy = sort;
            if (sortBy.length() > 0) {
                loadPagedData();
            }
        }
    };

    public SearchResultFragment() {
    }

    SearchResultFragment(String travelAgencyID, String cityID, String date, String dateTo, String flightClass, String maxPrice, String sortBy, String type, String duration, String continent) {
        this.travelAgencyID = travelAgencyID;
        this.cityID = cityID;
        this.date = date;
        this.flightClass = flightClass;
        this.maxPrice = maxPrice;
        this.sortBy = sortBy;
        this.dateTo = dateTo;
        this.type = type;
        this.duration = duration;
        this.continent = continent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Constants.isNetworkAvailable(SearchResultFragment.this.getContext())) {
            binding.layoutNoNetWork.getRoot().setVisibility(View.GONE);
            loadingDate();
            loadPagedData();
        } else {
            binding.layoutNoNetWork.getRoot().setVisibility(View.VISIBLE);
            binding.layoutNoNetWork.btnTryAgain.setOnClickListener(this);

        }

    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        long now = System.currentTimeMillis();
        if (now - mLastClickTime < Constants.CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;
        switch (v.getId()) {
            case R.id.imgBack:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btnTryAgain:
                if (itemViewModel != null)
                    itemViewModel.change();
                break;
            case R.id.SortingLin:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_FILTER);
                dialogFrag.showSortingOptions();
                break;
            case R.id.filterLinear:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_FILTER);
                filterDialog.showFiltrationOptions();
                break;
            case R.id.layoutView:

                isGridView = !isGridView;
                if (isGridView) {
                    binding.tvView.setText(SearchResultFragment.this.requireContext().getString(R.string.lieaner_view));
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_GRID_VIEW);
                    binding.imgGridView.setImageDrawable(Objects.requireNonNull(SearchResultFragment.this.getContext()).getResources().getDrawable(R.drawable.ic_list_view));
                    offersPagedGridAdapter = new OffersPagedGridAdapter(getActivity(), SearchResultFragment.this, SearchResultFragment.this);

                    binding.rvOffers.setLayoutManager(new GridLayoutManager(getContext(), 2));
                } else {
                    binding.tvView.setText(SearchResultFragment.this.requireContext().getString(R.string.grid_view));
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH_LINEAR_VIEW);
                    binding.imgGridView.setImageDrawable(Objects.requireNonNull(SearchResultFragment.this.getContext()).getResources().getDrawable(R.drawable.ic_grid_view));
                    allStoresPagedAdapter = new OffersPagedAdapter(getActivity(), SearchResultFragment.this, SearchResultFragment.this);
                    binding.rvOffers.setLayoutManager(new LinearLayoutManager(getContext()));
                }
                binding.rvOffers.setItemAnimator(new DefaultItemAnimator());
              //  binding.rvOffers.setHasFixedSize(true);

                if (itemViewModel != null)
                    itemViewModel.change();

                break;
        }

    }

    @Override
    public void onBack() {
        if (allStoresPagedAdapter != null && allStoresPagedAdapter.isShowCase()) {
            allStoresPagedAdapter.dismissShowCase();
            return;
        }
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();


    }

    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_result, container, false);
        AnalyticsUtility.setScreen(this);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.SEARCH_RESULT);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        PaginationProvider.USER_TOKEN = UtilsPreference.getInstance(SearchResultFragment.this.getContext()).getUser().getAccess_token();
        binding.imgBack.setOnClickListener(this);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.SortingLin.setOnClickListener(this);
        binding.layoutView.setOnClickListener(this);
        binding.filterLinear.setOnClickListener(this);

        dialogFrag = new SortDialog(Listener, getActivity());
        filterDialog = new FilterDialog(Objects.requireNonNull(getActivity()), listener);
        return binding.getRoot();

    }

    private void loadPagedData() {


        if (isGridView) {
            offersPagedGridAdapter = new OffersPagedGridAdapter(getActivity(), SearchResultFragment.this, SearchResultFragment.this);
            binding.rvOffers.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            allStoresPagedAdapter = new OffersPagedAdapter(getActivity(), SearchResultFragment.this, SearchResultFragment.this);
            binding.rvOffers.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        //   binding.rvOffers.setItemAnimator(new DefaultItemAnimator());
        //   binding.rvOffers.setHasFixedSize(true);


        itemViewModel = new PagedOffersSearchViewModel(travelAgencyID, cityID, date, dateTo, flightClass, maxPrice, sortBy, type, duration, tvAccommodations, continent);


        Log.d("SearchData", travelAgencyID + ", " + cityID + ", " + date + ", " + flightClass + ", " + maxPrice + ", " + sortBy);
        itemViewModel.setUpdate(updateData);

        itemViewModel.itemPagedList.observe(this, modelOffers -> {
            dataList = modelOffers;
            if (isGridView)
                offersPagedGridAdapter.submitList(dataList);
            else
                allStoresPagedAdapter.submitList(dataList);
        });


    }

    @Override
    public void OnOfferClicked(ModelOffer offer) {


        if (getActivity() == null)
            return;

        if (offer.getMulti_cities() == 1) {
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_MULTI_CITIES_OFFER_DETAILS);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                    .add(R.id.fullContent, new MultiOfferDetailsFragment(offer))
                    .addToBackStack("OfferDetailsFragment")
                    .commit();
        } else {
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_OFFER_DETAILS);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                    .add(R.id.fullContent, new OfferDetailsFragment(offer))
                    .addToBackStack("OfferDetailsFragment")
                    .commit();
        }


    }

    @Override
    public void OnCashedOfferClicked(OfferRoomModel offer) {

    }

    private void loadingDate() {


        if (isGridView) {
            List<ModelOffer> offersList = new ArrayList<>();
            ModelOffer offer = new ModelOffer();
            offersList.add(offer);
            offersList.add(offer);
            offersList.add(offer);
            offersList.add(offer);
            offersList.add(offer);
            OffersGridAdapter offersAdapter = new OffersGridAdapter(SearchResultFragment.this.getContext(), offersList, null, true);
            binding.rvOffers.setLayoutManager(new GridLayoutManager(getContext(), 2));
            //     binding.rvOffers.setHasFixedSize(true);
            binding.rvOffers.setAdapter(offersAdapter);
        } else {
            List<ModelOffer> offersList = new ArrayList<>();
            ModelOffer offer = new ModelOffer();
            offersList.add(offer);
            offersList.add(offer);
            offersList.add(offer);
            OffersAdapter offersAdapter = new OffersAdapter(offersList, null, true);
            binding.rvOffers.setLayoutManager(new LinearLayoutManager(getContext()));
            //    binding.rvOffers.setHasFixedSize(true);
            binding.rvOffers.setAdapter(offersAdapter);
        }


    }


    @Override
    public void onRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
        if (itemViewModel != null)
            itemViewModel.change();
    }
}
