package com.asc.yazy.fragment;

import static com.asc.yazy.activity.MainActivity.NotificationCount;
import static com.asc.yazy.utils.Constants.newNotification;

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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.NotificationAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.api.model.ModelNotification;
import com.asc.yazy.api.model.ModelNotificationAPI;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.pagination.PaginationProvider;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.NotificationsFabFragment;
import com.asc.yazy.core.RatingDialog;
import com.asc.yazy.databinding.ClearNotificationDialogBinding;
import com.asc.yazy.databinding.FragmentNotificationsBinding;
import com.asc.yazy.interfaces.OnNotificationListener;
import com.asc.yazy.interfaces.OnNotificationsSettingsListener;
import com.asc.yazy.interfaces.OnReschedule;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private FragmentNotificationsBinding binding;
    private Dialog clearNotification;
    private int notSeen = 0;

    OnReschedule listenerReschedule = res -> {
        if (res == 1) {
            getNotificationList();
        }
    };
    private final OnNotificationListener notificationListener = notification -> {
        if (notification.getRead().equals("0")) {
            notification.setRead("1");
            SetNotificationSeen(notification.getId() + "");
            notSeen--;
            UtilsPreference.getInstance(requireContext()).savePreference(Constants.NOTIFICATION_COUNT, notSeen);
            if (notSeen > 0) {
                NotificationCount.setVisibility(View.VISIBLE);
                NotificationCount.setText(String.valueOf(notSeen));
            } else {
                NotificationCount.setVisibility(View.GONE);
            }
        }
        NavigateToNotificationTarget(notification.getType() + "", (notification.getTarget_id()), notification);
    };
    private final OnNotificationsSettingsListener listener = new OnNotificationsSettingsListener() {
        @Override
        public void onClearAll() {
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.NOTIFICATIONS_CLEAR_ALL);
            clearNotification = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_DeviceDefault_Dialog);
            clearNotification.setCancelable(true);
            Objects.requireNonNull(clearNotification.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ClearNotificationDialogBinding DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.clear_notification_dialog, null, false);
            clearNotification.setContentView(DialogBinding.getRoot());
            DialogBinding.btnCancel.setOnClickListener(NotificationsFragment.this);
            DialogBinding.btnClear.setOnClickListener(NotificationsFragment.this);
            clearNotification.show();
        }

        @Override
        public void onMarkAllAsRead() {
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.NOTIFICATIONS_MARK_ALL_AS_READ);
            //call api make all read
            SetNotificationSeen("");


        }
    };


    private void getNotificationList() {

        if (!Constants.isNetworkAvailable(NotificationsFragment.this.getContext())) {
            binding.layoutNoNotifications.getRoot().setVisibility(View.GONE);
            binding.layoutNoInternet.getRoot().setVisibility(View.VISIBLE);
            binding.tvOptions.setVisibility(View.GONE);
            return;
        }
        notSeen = 0;
        UtilsPreference.getInstance(requireContext()).savePreference(Constants.NOTIFICATION_COUNT, notSeen);
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelNotificationAPI> call = mApiService.getNotificationsUser("Bearer " + PaginationProvider.USER_TOKEN, Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelNotificationAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelNotificationAPI> call, @NonNull Response<ModelNotificationAPI> response) {
                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(NotificationsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (response.body() != null) {

                    List<ModelNotification> notifications = response.body().getData();
                    if (notifications == null || notifications.size() == 0) {
                        binding.layoutNoNotifications.getRoot().setVisibility(View.VISIBLE);
                        binding.layoutNoInternet.getRoot().setVisibility(View.GONE);
                        binding.tvOptions.setVisibility(View.GONE);
                        NotificationCount.setVisibility(View.GONE);
                    } else {

                        for (int i = 0; i < notifications.size(); i++) {
                            if (notifications.get(i).getRead().equals("0")) {
                                notSeen++;
                            }
                        }
                        UtilsPreference.getInstance(requireContext()).savePreference(Constants.NOTIFICATION_COUNT, notSeen);
                        if (notSeen > 0) {
                            NotificationCount.setVisibility(View.VISIBLE);
                            NotificationCount.setText(String.valueOf(notSeen));
                        } else {
                            NotificationCount.setVisibility(View.GONE);
                        }
                        NotificationAdapter adapter = new NotificationAdapter(NotificationsFragment.this.getContext(), response.body().getData(), notificationListener);
                        binding.rvNotification.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.rvNotification.setAdapter(adapter);

                        binding.layoutNoNotifications.getRoot().setVisibility(View.GONE);
                        binding.tvOptions.setVisibility(View.VISIBLE);
                        binding.layoutNoInternet.getRoot().setVisibility(View.GONE);

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelNotificationAPI> call, @NonNull Throwable t) {
                if (NotificationsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.PENDING_TRIPS);
                //   GlobalInfoDialog.getInstance(NotificationsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(NotificationsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }

    @Override
    public void onRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
        loadPagedData();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.btnCancel:
                clearNotification.dismiss();
                break;
            case R.id.btnClear:
                clearAll();
                clearNotification.dismiss();
                break;
            case R.id.tvOptions:
                NotificationsFabFragment dialogFrag = new NotificationsFabFragment(listener);
                dialogFrag.setParentFab(binding.tvOptions);
                dialogFrag.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), dialogFrag.getTag());
                break;
            case R.id.btnTryAgain:
                loadPagedData();
                break;
        }
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
                if (NotificationsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;


                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(NotificationsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(NotificationsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200) {
                    NotificationCount.setText("0");
                    loadPagedData();
                } else {
                    GlobalInfoDialog.getInstance(NotificationsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (NotificationsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                //GlobalInfoDialog.getInstance(NotificationsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(NotificationsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }

    private void loadPagedData() {
        binding.swipeRefreshLayout.setRefreshing(false);
        List<ModelNotification> NotificationList = new ArrayList<>();
        NotificationAdapter adapter = new NotificationAdapter(NotificationsFragment.this.getContext(), NotificationList, notificationListener);
        binding.rvNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvNotification.setAdapter(adapter);
        getNotificationList();
    }


    private void NavigateToNotificationTarget(String type, String targetID, ModelNotification notification) {

        switch (type) {
            case "2":
            case "6":
                //   GlobalInfoDialog.getInstance(getActivity()).setTitle(notification.getTitle()).setMessage(notification.getBody()).show();
                break;
            case "1":
                ModelOffer offer = new ModelOffer();
                offer.setId(targetID + "");
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.NOTIFICATION_OPEN_OFFER);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new OfferDetailsFragment(offer))
                        .addToBackStack("OfferDetailsFragment")
                        .commit();
                break;
            case "3":
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.NOTIFICATION_OPEN_RATE);

                RatingDialog.getInstance(getActivity()).setNotificationID(Integer.parseInt(targetID)).show();
                break;
            case "4":
                ModelBooking trip = new ModelBooking();
                trip.setBooking_id(targetID);
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_BOOK_DETAILS);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new NewTripDetailsFragment(trip, listenerReschedule))
                        .addToBackStack("OfferDetailsFragment")
                        .commit();
                break;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_notifications, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.NOTIFICATIONS);
        AnalyticsUtility.setScreen(this);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        getNotificationList();
        newNotification = 0;
        binding.tvOptions.setOnClickListener(this);
        binding.layoutNoInternet.btnTryAgain.setOnClickListener(this);
        return binding.getRoot();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getNotificationList();
        }

    }

    private void SetNotificationSeen(String id) {
        ApiInterface mApiService = ApiClient.getInterfaceService();
        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            return;
        }
        Call<ModelStatic> call = mApiService.SetNotificationSeen("Bearer " + token, Constants.getLANGUAGE(), id);
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {

                if (getActivity() == null)
                    return;

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(getActivity()).setTitle(getActivity().getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelStatic body = response.body();
                if (body == null) {
                    GlobalInfoDialog.getInstance(getActivity()).setTitle(getActivity().getString(R.string.error)).setMessage(getActivity().getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    if (id.length() == 0) {
                        loadPagedData();
                        NotificationCount.setText("0");
                    }

                    //GlobalInfoDialog.getInstance(getActivity()).setTitle(getActivity().getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (getActivity() == null)
                    return;
                // GlobalInfoDialog.getInstance(getActivity()).setTitle(getActivity().getString(R.string.error)).setMessage(getActivity().getResources().getString(R.string.cant_connect)).show();

                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }
}