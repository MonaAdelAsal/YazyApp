package com.asc.yazy.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.asc.yazy.R;
import com.asc.yazy.activity.QuickKAccessActivity;
import com.asc.yazy.adapter.FavLoadingOffersAdapter;
import com.asc.yazy.api.model.ModelFav;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.pagination.PaginationProvider;
import com.asc.yazy.api.pagination.favorite.FavoritePagedAdapter;
import com.asc.yazy.api.pagination.favorite.PagedFavoriteViewModel;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.api.pagination.onLoadData;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentFavoritesBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.IFragmentAccess;
import com.asc.yazy.interfaces.IUpdatableFragment;
import com.asc.yazy.interfaces.OnFavoriteItemListener;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FavoritesFragment extends Fragment implements OnFavoriteItemListener, IFragment, IFragmentAccess, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, IUpdatableFragment {

    private static final String TAG = "FavoritesFragment";
    private long mLastClickTime = System.currentTimeMillis();
    private FragmentFavoritesBinding binding;
    private FavoritePagedAdapter allStoresPagedAdapter;
    private PagedFavoriteViewModel itemViewModel;
    private final onLoadData updateData = new onLoadData() {
        @Override
        public void onDataLoaded(int count) {

            switch (count) {
                case NetworkState.UN_AUTHORISE:
                    Log.d(TAG, "onDataLoaded: UN_AUTHORISE");
                    if (getActivity() == null)
                        return;
                    binding.layoutNoOffers.getRoot().setVisibility(View.VISIBLE);
                    binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_ACCOUNT_INFO);
                    if (!UnAuthorizedFragment.isAlive)
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.fullContent, new UnAuthorizedFragment())
                                .addToBackStack(null)
                                .commit();
                    break;
                case NetworkState.LOADING:
                    Log.d(TAG, "onDataLoaded: Loading");
                    binding.layoutNoOffers.getRoot().setVisibility(View.GONE);
                    binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
                    loadingDate();
                    break;
                case NetworkState.NO_NET:
                    Log.d(TAG, "onDataLoaded: No net");
                    binding.layoutNoOffers.getRoot().setVisibility(View.GONE);
                    binding.layoutNoInternet.getRoot().setVisibility(View.VISIBLE);
                    break;
                case 0:
                    Log.d(TAG, "onDataLoaded: 0");
                    binding.layoutNoOffers.getRoot().setVisibility(View.VISIBLE);
                    break;
                case NetworkState.LOADED:
                    if (itemViewModel != null) {
                        loadingDate();
                        itemViewModel.change();
                    }
                    break;
                default:
                    binding.rvOffers.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onDataLoaded: Default");
                    binding.layoutNoOffers.getRoot().setVisibility(View.GONE);
                    binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
                    binding.rvOffers.setAdapter(allStoresPagedAdapter);


            }
        }
    };

    public FavoritesFragment() {
    }

    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.FAVORITE);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        PaginationProvider.USER_TOKEN = UtilsPreference.getInstance(FavoritesFragment.this.getContext()).getUser().getAccess_token();
        binding.imgBack.setOnClickListener(this);
        binding.layoutNoInternet.btnTryAgain.setOnClickListener(this);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        loadPagedData();
        return binding.getRoot();
    }

    private void loadPagedData() {


        allStoresPagedAdapter = new FavoritePagedAdapter(getActivity(), FavoritesFragment.this, updateData);
        binding.rvOffers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvOffers.setHasFixedSize(true);

        itemViewModel = new PagedFavoriteViewModel();
        itemViewModel.setUpdate(updateData);

        itemViewModel.itemPagedList.observe(this, modelFavs -> allStoresPagedAdapter.submitList(modelFavs));


    }

    private void loadingDate() {

        List<ModelOffer> offersList = new ArrayList<>();
        ModelOffer offer = new ModelOffer();
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        FavLoadingOffersAdapter offersAdapter = new FavLoadingOffersAdapter(offersList, null, true);
        binding.rvOffers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvOffers.setItemAnimator(new DefaultItemAnimator());
        binding.rvOffers.setHasFixedSize(true);
        binding.rvOffers.setAdapter(offersAdapter);

    }


    @Override
    public void onFaveClicked(ModelFav offer) {

        if (getActivity() == null)
            return;

        ModelOffer modelOffer = new ModelOffer();
        modelOffer.setId(offer.getId());
        modelOffer.setTitle(offer.getTitle());
        modelOffer.setDate_from(offer.getDate_from());
        modelOffer.setDate_to(offer.getDate_to());
        modelOffer.setImage(offer.getImage());
        modelOffer.setPrice(offer.getPrice());
        modelOffer.setCurrency(offer.getCurrency());
        AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_OFFER_DETAILS);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                .add(R.id.fullContent, new OfferDetailsFragment(modelOffer))
                .addToBackStack("OfferDetailsFragment")
                .commit();

    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
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

        switch (v.getId()) {

            case R.id.imgBack:
                if (getActivity() == null)
                    return;
                if (getActivity() instanceof QuickKAccessActivity)
                    getActivity().finish();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btnTryAgain:
                if (itemViewModel != null)
                    itemViewModel.change();
                break;

        }
    }

    @Override
    public void onRefresh() {
        if (itemViewModel != null)
            itemViewModel.change();
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFragmentUpdate() {
        if (itemViewModel != null)
            itemViewModel.change();
    }

    @Override
    public boolean onBackAccess() {
        return true;
    }
}
