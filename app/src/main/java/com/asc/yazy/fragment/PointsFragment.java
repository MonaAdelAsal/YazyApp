package com.asc.yazy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.PointsAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.MYModelPointsAPI;
import com.asc.yazy.api.model.ModelPoint;
import com.asc.yazy.api.model.ModelPointsAPI;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PlaneDialog;
import com.asc.yazy.databinding.FragmentPointsBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.IUpdatableFragment;
import com.asc.yazy.interfaces.OnPointListener;
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


public class PointsFragment extends Fragment implements View.OnClickListener, IUpdatableFragment, IFragment, SwipeRefreshLayout.OnRefreshListener, OnPointListener {

    private long mLastClickTime = System.currentTimeMillis();
    private FragmentPointsBinding binding;
    private int totalPoints = 0;


    public PointsFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_points, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.NOTIFICATIONS_SETTINGS);
        AnalyticsUtility.setScreen(this);
        binding.imgBack.setOnClickListener(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        mytPoints();
        getPoints();
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

    private void mytPoints() {
        binding.loader.startShimmerAnimation();
        binding.label.setVisibility(View.GONE);
        binding.tvPoints.setVisibility(View.GONE);
        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            return;
        }
        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<MYModelPointsAPI> call = mApiService.myPoint("Bearer " + token);
        call.enqueue(new Callback<MYModelPointsAPI>() {
            @Override
            public void onResponse(@NonNull Call<MYModelPointsAPI> call, @NonNull Response<MYModelPointsAPI> response) {

                if (getActivity() == null)
                    return;
                if (PointsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                binding.loader.stopShimmerAnimation();
                planeDialog.Dismiss();


                if (response.code() == 401) {
                    Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    getActivity().startActivity(intent);
                    return;
                }

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(PointsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    return;
                }
                MYModelPointsAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(PointsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    binding.tvPoints.setText(body.getData().getPoints());
                    float progress = Float.parseFloat(body.getData().getPoints());
                    if (progress >= 60000)
                        binding.progress.setProgress(60000);
                    else
                        binding.progress.setProgress(progress);
                    totalPoints = Integer.parseInt(body.getData().getPoints());
                    binding.label.setVisibility(View.VISIBLE);
                    binding.tvPoints.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onFailure(@NonNull Call<MYModelPointsAPI> call, @NonNull Throwable t) {

                if (PointsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                planeDialog.Dismiss();
                binding.loader.stopShimmerAnimation();
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.NOTIFICATIONS_SETTINGS);
                //  GlobalInfoDialog.getInstance(PointsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(PointsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }

    private void getPoints() {

        List<ModelPoint> pointList = new ArrayList<>();
        pointList.add(new ModelPoint());
        pointList.add(new ModelPoint());
        pointList.add(new ModelPoint());
        pointList.add(new ModelPoint());
        pointList.add(new ModelPoint());
        pointList.add(new ModelPoint());

        PointsAdapter bookingAdapter = new PointsAdapter(PointsFragment.this.getContext(), pointList, true, PointsFragment.this);
        binding.rvPoints.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvPoints.setAdapter(bookingAdapter);

        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            return;
        }
        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(getActivity()));
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelPointsAPI> call = mApiService.getPoints("Bearer " + token);
        call.enqueue(new Callback<ModelPointsAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelPointsAPI> call, @NonNull Response<ModelPointsAPI> response) {

                if (getActivity() == null)
                    return;
                if (PointsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                planeDialog.Dismiss();

                if (response.code() == 401) {
                    Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    getActivity().startActivity(intent);
                    return;
                }

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(PointsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    return;
                }
                ModelPointsAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(PointsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {

                    PointsAdapter bookingAdapter = new PointsAdapter(PointsFragment.this.getContext(), body.getData(), false, PointsFragment.this);
                    binding.rvPoints.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    binding.rvPoints.setAdapter(bookingAdapter);

                }


            }

            @Override
            public void onFailure(@NonNull Call<ModelPointsAPI> call, @NonNull Throwable t) {

                if (PointsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;

                planeDialog.Dismiss();
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.NOTIFICATIONS_SETTINGS);
                //GlobalInfoDialog.getInstance(PointsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(PointsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onRefresh() {
        //binding.swipeRefreshLayout.setRefreshing(false);
        mytPoints();
        getPoints();
    }

    @Override
    public void onPointClicked(ModelPoint point) {

        long now = System.currentTimeMillis();
        if (now - mLastClickTime < Constants.CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;

        if (point == null)
            return;
        if (Integer.parseInt(point.getDiscountPoints()) > totalPoints) {
            String message = getString(R.string.u_cant_redem) + " " + getString(R.string.you_point_less) + " " + point.getDiscountPoints() + " " + getString(R.string.pts);
            GlobalInfoDialog.getInstance(PointsFragment.this.requireContext()).setTitle(getString(R.string.sorry)).setMessage(message).show();
        } else {
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.REDEEM_POINTS);
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                    .add(R.id.fullContent, new RedeemPointsFragment(point))
                    .addToBackStack("RedeemPointsFragment")
                    .commit();

        }
    }

    @Override
    public void onFragmentUpdate() {
        mytPoints2();

    }

    private void mytPoints2() {

        String token = UtilsPreference.getInstance(getActivity()).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            return;
        }
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<MYModelPointsAPI> call = mApiService.myPoint("Bearer " + token);
        call.enqueue(new Callback<MYModelPointsAPI>() {
            @Override
            public void onResponse(@NonNull Call<MYModelPointsAPI> call, @NonNull Response<MYModelPointsAPI> response) {

                if (getActivity() == null)
                    return;
                if (PointsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;


                if (response.code() == 401) {
                    Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    getActivity().startActivity(intent);
                    return;
                }

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(PointsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    return;
                }
                MYModelPointsAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(PointsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    binding.tvPoints.setText(body.getData().getPoints());
                    float progress = Float.parseFloat(body.getData().getPoints());
                    if (progress >= 60000)
                        binding.progress.setProgress(60000);
                    else
                        binding.progress.setProgress(progress);
                    totalPoints = Integer.parseInt(body.getData().getPoints());
                    binding.label.setVisibility(View.VISIBLE);
                    binding.tvPoints.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onFailure(@NonNull Call<MYModelPointsAPI> call, @NonNull Throwable t) {

                if (PointsFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.NOTIFICATIONS_SETTINGS);
                //GlobalInfoDialog.getInstance(PointsFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(PointsFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));

            }
        });
    }

}
