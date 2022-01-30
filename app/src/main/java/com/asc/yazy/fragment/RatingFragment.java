package com.asc.yazy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.asc.yazy.R;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.adapter.CommentsAdapter;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.CommentModel;
import com.asc.yazy.api.model.ModelCommentAPI;
import com.asc.yazy.api.model.ModelTravelAgency;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.databinding.FragmentRatingBinding;
import com.asc.yazy.interfaces.IFragment;
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


public class RatingFragment extends Fragment implements View.OnClickListener, IFragment, SwipeRefreshLayout.OnRefreshListener {


    private FragmentRatingBinding binding;
    private ModelTravelAgency travelAgency;


    public RatingFragment() {
    }

    public RatingFragment(ModelTravelAgency travelAgency) {
        this.travelAgency = travelAgency;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_rating, null, false);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.REVIEWS);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        binding.rate.setRating(Math.round(travelAgency.getTotal_rate()));
        binding.imgBack.setOnClickListener(this);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDate();
        loadData();
    }

    private void loadingDate() {

        binding.loaderName.startShimmerAnimation();
        binding.loaderRate.startShimmerAnimation();
        List<CommentModel> offersList = new ArrayList<>();
        CommentModel offer = new CommentModel();
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        offersList.add(offer);
        CommentsAdapter offersAdapter = new CommentsAdapter(offersList, true);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvComments.setItemAnimator(new DefaultItemAnimator());
        binding.rvComments.setHasFixedSize(true);
        binding.rvComments.setAdapter(offersAdapter);

    }

    private void loadData() {

        ModelUser user = UtilsPreference.getInstance(RatingFragment.this.getContext()).getUser();
        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty())
            return;

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelCommentAPI> call = mApiService.getComments("Bearer " + user.getAccess_token(), Constants.getLANGUAGE(), travelAgency.getTravel_agency_id());
        call.enqueue(new Callback<ModelCommentAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelCommentAPI> call, @NonNull Response<ModelCommentAPI> response) {
                if (getActivity() == null)
                    return;
                if (RatingFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(RatingFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                ModelCommentAPI body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(RatingFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200 && body.getData() != null && body.getData() != null && body.getData().getRates() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.REVIEWS);
                    binding.loaderRate.stopShimmerAnimation();
                    binding.loaderName.stopShimmerAnimation();
                    binding.tvRate.setBackground(null);
                    binding.tvTAName.setBackground(null);
                    binding.setTravelAgency(body.getData());
                    CommentsAdapter adult = new CommentsAdapter(body.getData().getRates(), false);
                    binding.rvComments.setLayoutManager(new LinearLayoutManager(RatingFragment.this.getContext()));
                    binding.rvComments.setHasFixedSize(true);
                    binding.rvComments.setAdapter(adult);


                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelCommentAPI> call, @NonNull Throwable t) {

                if (RatingFragment.this.getContext() == null)
                    return;
                if (!isAdded())
                    return;
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.REVIEWS);
                // GlobalInfoDialog.getInstance(RatingFragment.this.getContext()).setTitle(getString(R.string.error)).setMessage(RatingFragment.this.getContext().getResources().getString(R.string.cant_connect)).show();
                requireActivity().startActivity(new Intent(getActivity(), NoNetActivity.class));


            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == null)
            return;
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
    public void onRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
        binding.tvTAName.setBackgroundColor(Objects.requireNonNull(RatingFragment.this.getContext()).getResources().getColor(R.color.gray));
        binding.tvRate.setBackgroundColor(Objects.requireNonNull(RatingFragment.this.getContext()).getResources().getColor(R.color.gray));
        binding.loaderName.startShimmerAnimation();
        binding.loaderRate.startShimmerAnimation();
        loadingDate();
        loadData();
    }
}
