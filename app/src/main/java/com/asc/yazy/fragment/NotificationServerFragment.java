package com.asc.yazy.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.MainActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelNotification;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.pagination.notification.NotificationPagedAdapter;
import com.asc.yazy.api.pagination.notification.PagedNotificationViewModel;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.api.pagination.onLoadData;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.ClearNotificationDialogBinding;
import com.asc.yazy.databinding.FragmentNotificationsMergeBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.IUpdatableFragment;
import com.asc.yazy.interfaces.OnNotificationListener;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationServerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, IUpdatableFragment, View.OnClickListener, IFragment, OnNotificationListener {

    private FragmentNotificationsMergeBinding binding;
    private NotificationPagedAdapter allStoresPagedAdapter;
    private final onLoadData updateData = new onLoadData() {
        @Override
        public void onDataLoaded(int count) {

            switch (count) {
                case NetworkState.UN_AUTHORISE:
                    // binding.layoutGuest.getRoot().setVisibility(View.VISIBLE);
                    // binding.layoutNoNotifications.getRoot().setVisibility(View.GONE);
                    // binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
                    break;
                case NetworkState.LOADING:
                    binding.layoutNoNotifications.getRoot().setVisibility(View.GONE);
                    binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
                    //  loadingDate();
                    break;
                case NetworkState.NO_NET:
                    binding.layoutNoNotifications.getRoot().setVisibility(View.GONE);
                    binding.layoutNoInternet.getRoot().setVisibility(View.VISIBLE);
                    break;
                case 0:
                    binding.layoutNoNotifications.getRoot().setVisibility(View.VISIBLE);
                    break;
                case NetworkState.LOADED:
                default:
                    binding.layoutNoNotifications.getRoot().setVisibility(View.GONE);
                    binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
                    binding.rvNotification.setAdapter(allStoresPagedAdapter);

            }
        }
    };
    private PagedNotificationViewModel itemViewModel;
    private Dialog dialog;

    public NotificationServerFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_notifications_merge, null, false);
        AnalyticsUtility.logEvent(AnalyticsUtility.Events.NOTIFICATIONS, AnalyticsUtility.Events.NOTIFICATIONS);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.tvClear.setOnClickListener(this);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPagedData();
    }

    private void loadPagedData() {

        allStoresPagedAdapter = new NotificationPagedAdapter(getActivity(), NotificationServerFragment.this);
        binding.rvNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvNotification.setHasFixedSize(true);
        // binding.rvNotification.setNestedScrollingEnabled(true);


        itemViewModel = new PagedNotificationViewModel();
        itemViewModel.setUpdate(updateData);

        itemViewModel.itemPagedList.observe(this, allStoresPagedAdapter::submitList);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.imgBack:
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.layoutNoTrips:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.container, new Home3Fragment())
                        .addToBackStack("HomeFragment")
                        .commit();
                break;
            case R.id.btnTryAgain:
                if (itemViewModel != null)
                    itemViewModel.change();
                break;
            case R.id.btnExplore:
                if (getActivity() == null)
                    return;
                getActivity().getSupportFragmentManager().popBackStack();
                MainActivity.updateBottomNavigation(0);
                break;
            case R.id.tvClear:
                dialog = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_DeviceDefault_Dialog);
                dialog.setCancelable(true);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ClearNotificationDialogBinding DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.clear_notification_dialog, null, false);
                dialog.setContentView(DialogBinding.getRoot());
                DialogBinding.btnCancel.setOnClickListener(this);
                DialogBinding.btnClear.setOnClickListener(this);
                dialog.show();

                break;
            case R.id.btnCancel:
                dialog.dismiss();
                break;
            case R.id.btnClear:
                clearAll();
                dialog.dismiss();
                break;

        }
    }


    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onFragmentUpdate() {
        binding.rvNotification.smoothScrollToPosition(0);
    }


    @Override
    public void OnNotificationClicked(ModelNotification notification) {

    }


    private void clearAll() {
        ApiInterface mApiService = ApiClient.getInterfaceService();
        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            return;
        }
        Call<ModelStatic> call = mApiService.clearNotification("Bearer " + token, Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {

                if (getActivity() == null)
                    return;
                if (NotificationServerFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;


                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(NotificationServerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(NotificationServerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200) {
                    loadPagedData();
                } else {
                    GlobalInfoDialog.getInstance(NotificationServerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (NotificationServerFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                // GlobalInfoDialog.getInstance(NotificationServerFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(NotificationServerFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }


    /*
    @Override
    public void onNotification(ModelNotification notification) {
        if (allStoresPagedAdapter != null) {
            allStoresPagedAdapter.insetNotification(notification);
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                binding.layoutNoNotifications.getRoot().setVisibility(View.GONE);
                binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
                binding.rvNotification.setAdapter(allStoresPagedAdapter);
            });

        }
    }
    */

    @Override
    public void onRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
        loadPagedData();
    }
}
