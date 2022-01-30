package com.asc.yazy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.MainActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelAuthenticate;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.FragmentNotificationSettingsBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationSettingsFragment extends Fragment implements View.OnClickListener, IFragment {

    private long mLastClickTime = System.currentTimeMillis();
    private FragmentNotificationSettingsBinding binding;
    private ModelUser user;

    private boolean preState;
    private final SwitchButton.OnCheckedChangeListener onSwitchListener = (view, isChecked) -> {
        if (isChecked) {
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.NOTIFICATIONS_SETTINGS_ENABLED);
            changeNotificationStatue("1");
        } else {
            changeNotificationStatue("0");
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.NOTIFICATIONS_SETTINGS_DISABLED);
        }
    };


    public NotificationSettingsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_notification_settings, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.NOTIFICATIONS_SETTINGS);
        AnalyticsUtility.setScreen(this);
        binding.imgBack.setOnClickListener(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.switchButton.setOnClickListener(this);

        user = UtilsPreference.getInstance(getActivity()).getUser();
        if (user != null && user.getAccess_token() != null && !user.getAccess_token().isEmpty()) {
            System.out.println("switchButton " + user.getEnable_notification());
            if (user.getEnable_notification() == 1) {
                binding.switchButton.setChecked(true);
                preState = true;
            } else {
                binding.switchButton.setChecked(false);
                preState = false;

            }
        }

        binding.switchButton.setOnCheckedChangeListener(onSwitchListener);

        return binding.getRoot();
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

    private void changeNotificationStatue(String enable) {

        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            return;
        }
        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelAuthenticate> call = mApiService.NotificationSettings("Bearer " + token, Constants.getLANGUAGE(), enable);
        call.enqueue(new Callback<ModelAuthenticate>() {
            @Override
            public void onResponse(@NonNull Call<ModelAuthenticate> call, @NonNull Response<ModelAuthenticate> response) {

                if (getActivity() == null)
                    return;
                if (NotificationSettingsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                binding.switchButton.setOnCheckedChangeListener(null);

                planeDialog.Dismiss();


                if (response.code() == 401) {
                    binding.switchButton.setChecked(preState);
                    binding.switchButton.setOnCheckedChangeListener(onSwitchListener);
                    Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    getActivity().startActivity(intent);
                    return;
                }

                if (response.code() != 200) {
                    binding.switchButton.setChecked(preState);
                    binding.switchButton.setOnCheckedChangeListener(onSwitchListener);
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(NotificationSettingsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    return;
                }
                ModelAuthenticate body = response.body();

                if (body == null) {
                    binding.switchButton.setChecked(preState);
                    binding.switchButton.setOnCheckedChangeListener(onSwitchListener);
                    GlobalInfoDialog.getInstance(NotificationSettingsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.NOTIFICATIONS_SETTINGS);
                    binding.switchButton.setChecked(!preState);
                    binding.switchButton.setOnCheckedChangeListener(onSwitchListener);
                    user.setEnable_notification(body.getData().getEnable_notification());
                    UtilsPreference.getInstance(getActivity()).saveUser(user);
                    MainActivity.mainContainerViewPagerAdapter.notifyDataSetChanged();
                    preState = !preState;
                } else {
                    binding.switchButton.setChecked(preState);
                    binding.switchButton.setOnCheckedChangeListener(onSwitchListener);
                    GlobalInfoDialog.getInstance(NotificationSettingsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelAuthenticate> call, @NonNull Throwable t) {

                if (NotificationSettingsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                planeDialog.Dismiss();
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.NOTIFICATIONS_SETTINGS);
                // GlobalInfoDialog.getInstance(NotificationSettingsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(NotificationSettingsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                binding.switchButton.setOnCheckedChangeListener(null);
                binding.switchButton.setChecked(preState);
                binding.switchButton.setOnCheckedChangeListener(onSwitchListener);

                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }
}
