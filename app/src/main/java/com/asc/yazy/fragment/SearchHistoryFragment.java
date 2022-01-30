package com.asc.yazy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asc.yazy.R;
import com.asc.yazy.adapter.FullSearchHistoryAdapter;
import com.asc.yazy.cash.room.model.FullSearchHistoryModel;
import com.asc.yazy.cash.room.viewmodel.FullSearchHistoryRoomViewModel;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentSearchHistoryBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.OnFullSearchHListener;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;


public class SearchHistoryFragment extends Fragment implements View.OnClickListener, IFragment, OnFullSearchHListener {

    private long mLastClickTime = System.currentTimeMillis();
    private FragmentSearchHistoryBinding binding;


    public SearchHistoryFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_search_history, null, false);
        AnalyticsUtility.setScreen(this);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.SEARCH_HISTORY);
        binding.imgBack.setOnClickListener(this);
        bindSearchHistory();
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        return binding.getRoot();
    }


    private void bindSearchHistory() {


        FullSearchHistoryRoomViewModel notificationRoomViewModel = ViewModelProviders.of(SearchHistoryFragment.this).get(FullSearchHistoryRoomViewModel.class);
        notificationRoomViewModel.getAll().observe(SearchHistoryFragment.this, fullSearchHistoryModels -> {
            AnalyticsUtility.logEventLoadDate(AnalyticsUtility.Events.SEARCH_HISTORY);
            if (fullSearchHistoryModels == null || fullSearchHistoryModels.size() == 0) {
                binding.rvSearchHistory.setVisibility(View.GONE);

                return;
            }
            FullSearchHistoryAdapter fullSearchHistoryAdapter = new FullSearchHistoryAdapter(fullSearchHistoryModels, SearchHistoryFragment.this);
            binding.rvSearchHistory.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvSearchHistory.setAdapter(fullSearchHistoryAdapter);
            binding.rvSearchHistory.setVisibility(View.VISIBLE);

        });
    }

    @Override
    public void onClick(View v) {
        if (v == null)
            return;

        long now = System.currentTimeMillis();
        if (now - mLastClickTime < Constants.CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;
        if (v.getId() == R.id.imgBack) {
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        }

    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onFullSearchItemClicked(FullSearchHistoryModel model) {

        if (getActivity() == null)
            return;
        if (model == null)
            return;
        AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                .add(R.id.fullContent, new SearchResultFragment(null, null, model.getDestination(), model.getDataFrom(), model.getDataTo(), null, null, null, null, null))
                .addToBackStack("SearchResultFragment")
                .commit();

    }
}
