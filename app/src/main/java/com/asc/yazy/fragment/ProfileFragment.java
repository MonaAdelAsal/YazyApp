package com.asc.yazy.fragment;

import static com.asc.yazy.activity.MainActivity.NotificationCount;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.activity.TripsActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.api.pagination.PaginationProvider;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.cash.room.SearchHistoryRepository;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.FragmentProfileBinding;
import com.asc.yazy.databinding.LogoutDialogBinding;
import com.asc.yazy.interfaces.IUpdatableFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener, IUpdatableFragment {

    private FragmentProfileBinding binding;
    private ModelUser user;
    private Dialog Logout;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_profile, null, false);
        AnalyticsUtility.setScreen(this);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.PROFILE);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindDate();
    }

    private void bindDate() {
        user = UtilsPreference.getInstance(getActivity()).getUser();
        if (user != null && user.getAccess_token() != null && !user.getAccess_token().isEmpty()) {
            binding.setProfileData(user);
            /*
            try {
                String[] name = user.getName().split(" ");
                if (name.length == 1)
                    binding.tvNameLetters.setText(String.valueOf(name[0].charAt(0)));
                if (name.length > 1)
                    binding.tvNameLetters.setText((String.valueOf(name[0].charAt(0)) + name[1].charAt(0)));
            } catch (Exception ignored) {

            }
             */
            binding.llGuest.setVisibility(View.GONE);
            binding.llRegisterUser.setVisibility(View.VISIBLE);
            binding.linearNotifications.setOnClickListener(this);
            binding.llPass.setOnClickListener(this);
            binding.llFav.setOnClickListener(this);
            binding.llTrips.setOnClickListener(this);
            binding.llAccountInfo.setOnClickListener(this);
            binding.btnLogout.setOnClickListener(this);
            binding.LayoutFrequent.setOnClickListener(this);
            binding.llPoints.setOnClickListener(this);
            notifyToPendingBookings();

        } else {
            binding.llGuest.setVisibility(View.VISIBLE);
            binding.llRegisterUser.setVisibility(View.GONE);
            binding.btnLogin.setOnClickListener(this);
            binding.llRegister.setOnClickListener(this);

        }


    }

    private void notifyToPendingBookings() {
        if (getActivity() == null)
            return;
        if (UtilsPreference.getInstance(getActivity()).getPreference(Constants.IS_BOOKINGS_PENDING, false)) {
            binding.imgNotify.setVisibility(View.VISIBLE);
            binding.pulsator.setColor(getActivity().getResources().getColor(R.color.colorAccent));
            binding.pulsator.start();
            binding.pulsator.setVisibility(View.VISIBLE);
        } else {
            binding.imgNotify.setVisibility(View.GONE);
            binding.pulsator.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.llPass:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_CHANGE_PASSWORD);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new ChangePasswordFragment())
                        .addToBackStack("ChangePasswordFragment")
                        .commit();
                break;

            case R.id.LayoutFrequent:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_FREQUENT_TRAVELLERS);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new FrequentTravellersFragment())
                        .addToBackStack("AddFreqTravelerFragment")
                        .commit();
                break;
            case R.id.llTrips:
                if (getActivity() == null)
                    return;
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_TRIPS);
                getActivity().startActivity(new Intent(getActivity(), TripsActivity.class));
                /*
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new MyTripsPagerFragment())
                        .addToBackStack("MyTripsFragment")
                        .commit();

                 */
                break;
            case R.id.linearNotifications:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_NOTIFICATION_SETTINGS);
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new NotificationSettingsFragment())
                            .addToBackStack("NotificationSettingsFragment")
                            .commit();
                break;
            case R.id.llFav:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_FAVORITE);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new FavoritesFragment())
                        .addToBackStack("ProfileFragment")
                        .commit();
                break;
            case R.id.llAccountInfo:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_ACCOUNT_INFO);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new AccountInfoFragment())
                        .addToBackStack("AccountInfoFragment")
                        .commit();
                break;

            case R.id.llPoints:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_POINTS);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new PointsFragment())
                        .addToBackStack("AccountInfoFragment")
                        .commit();
                break;

            case R.id.btnLogout:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_LOG_OUT);
                Logout = new Dialog(Objects.requireNonNull(getActivity()), android.R.style.Theme_DeviceDefault_Dialog);
                Objects.requireNonNull(Logout.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                LogoutDialogBinding DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.logout_dialog, null, false);
                Logout.setContentView(DialogBinding.getRoot());
                DialogBinding.btnCancel.setOnClickListener(this);
                DialogBinding.btnLogoutDialog.setOnClickListener(this);
                Logout.show();
                break;
            case R.id.btnLogin:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_LOGIN);
                startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                break;
            case R.id.btnLogoutDialog:
                logout();
                Logout.dismiss();
                break;
            case R.id.btnCancel:
                Logout.dismiss();
                break;
            case R.id.llRegister:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_REGISTER);
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                intent.putExtra(Constants.TRANSACTION, Constants.REGISTRATION);
                startActivity(intent);
                break;

        }
    }

    private void logout() {

        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty())
            return;

        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelStatic> call = mApiService.logout("Bearer " + user.getAccess_token(), Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {

                if (getActivity() == null)
                    return;
                if (ProfileFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();

                if (response.code() == 401) {

                    if (getActivity() == null)
                        return;
                    UtilsPreference.getInstance(getActivity()).logOut();
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_LOG_OUT_SUCCESS);
                    PaginationProvider.USER_TOKEN = null;
                    UtilsPreference.getInstance(getActivity()).savePreference(Constants.IS_BOOKINGS_PENDING, false);
                    SearchHistoryRepository historyRepository = new SearchHistoryRepository(getActivity());
                    historyRepository.deleteAll();
                    //NotificationsRepository notificationsRepository = new NotificationsRepository(getActivity());
                    // notificationsRepository.deleteAllNotification();
                    updateShortCut();

                    notifyToPendingBookings();
                    bindDate();
                    binding.llGuest.setAlpha(0);
                    binding.llGuest.setVisibility(View.VISIBLE);
                    binding.llGuest.animate().alpha(1.0f);
                    binding.llRegisterUser.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            binding.llGuest.setVisibility(View.VISIBLE);
                            binding.llRegisterUser.setVisibility(View.GONE);
                            binding.btnLogin.setOnClickListener(ProfileFragment.this);
                            binding.llRegister.setOnClickListener(ProfileFragment.this);
                        }
                    });

                }
                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(ProfileFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(ProfileFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200) {

                    if (getActivity() == null)
                        return;
                    UtilsPreference.getInstance(getActivity()).logOut();
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_LOG_OUT_SUCCESS);
                    PaginationProvider.USER_TOKEN = null;
                    UtilsPreference.getInstance(getActivity()).savePreference(Constants.IS_BOOKINGS_PENDING, false);
                    SearchHistoryRepository historyRepository = new SearchHistoryRepository(getActivity());
                    historyRepository.deleteAll();
                    NotificationCount.setVisibility(View.GONE);
                    //NotificationsRepository notificationsRepository = new NotificationsRepository(getActivity());
                    // notificationsRepository.deleteAllNotification();
                    updateShortCut();

                    notifyToPendingBookings();
                    bindDate();
                    binding.llGuest.setAlpha(0);
                    binding.llGuest.setVisibility(View.VISIBLE);
                    binding.llGuest.animate().alpha(1.0f);
                    binding.llRegisterUser.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            binding.llGuest.setVisibility(View.VISIBLE);
                            binding.llRegisterUser.setVisibility(View.GONE);
                            binding.btnLogin.setOnClickListener(ProfileFragment.this);
                            binding.llRegister.setOnClickListener(ProfileFragment.this);
                        }
                    });

                } else {
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_LOG_OUT_FAIL);
                    GlobalInfoDialog.getInstance(ProfileFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(body.getMessage()).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (ProfileFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                planeDialog.Dismiss();
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_LOG_OUT_FAIL);
                // GlobalInfoDialog.getInstance(ProfileFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(ProfileFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));


            }
        });
    }

    private void updateShortCut() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {

            ShortcutManager shortcutManager = ProfileFragment.this.requireContext().getSystemService(ShortcutManager.class);
            if (shortcutManager != null)
                shortcutManager.removeAllDynamicShortcuts();

            Intent intentSearch = new Intent(Constants.SEARCH_ACTION);
            intentSearch.setClassName(ProfileFragment.this.requireContext().getPackageName(), Constants.QUICK_ACCESS_CLASS_NAME);

            ShortcutInfo shortcutSearch = new ShortcutInfo.Builder(ProfileFragment.this.requireContext(), "SEARCH")
                    .setShortLabel(ProfileFragment.this.requireContext().getResources().getString(R.string.search))
                    .setLongLabel(ProfileFragment.this.requireContext().getResources().getString(R.string.search))
                    .setIcon(Icon.createWithResource(ProfileFragment.this.requireContext(), R.drawable.ic_search))
                    .setIntent(intentSearch)
                    .build();

            if (shortcutManager != null)
                shortcutManager.setDynamicShortcuts(Collections.singletonList(shortcutSearch));
        }
    }

    @Override
    public void onResume() {
        bindDate();
        super.onResume();
    }

    @Override
    public void onFragmentUpdate() {
        user = UtilsPreference.getInstance(getActivity()).getUser();
        if (user != null && user.getAccess_token() != null && !user.getAccess_token().isEmpty())
            binding.setProfileData(user);

        notifyToPendingBookings();
    }
}
