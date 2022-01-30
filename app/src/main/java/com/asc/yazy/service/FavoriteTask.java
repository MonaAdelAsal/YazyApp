package com.asc.yazy.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelFav;
import com.asc.yazy.api.model.ModelFavorite;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.api.pagination.onLoadData;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "FavoriteTask";
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final int liked;
    private final String id;
    private onLoadData update;

    @SuppressWarnings("deprecation")
    public FavoriteTask(Context context, ModelOffer offer, int liked, onLoadData update) {
        this.context = context;
        this.liked = liked;
        this.id = offer.getId();
        this.update = update;
    }

    @SuppressWarnings("deprecation")
    public FavoriteTask(Context context, ModelFav offer, int liked) {
        this.context = context;
        this.liked = liked;
        this.id = offer.getId();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        ModelUser user = UtilsPreference.getInstance(context).getUser();
        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty())
            return null;
        if (id == null || id.isEmpty())
            return null;
        ApiInterface mApiService = ApiClient.getInterfaceService();
        int flag;
        if (liked == 1)
            flag = 0;
        else
            flag = 1;
        Call<ModelFavorite> call = mApiService.favorite("Bearer " + user.getAccess_token(), Constants.getLANGUAGE(), id, flag);
        call.enqueue(new Callback<ModelFavorite>() {
            @Override
            public void onResponse(@NonNull Call<ModelFavorite> call, @NonNull Response<ModelFavorite> response) {

                if (response.code() != 200) {
                    if (update != null)
                        update.onDataLoaded(NetworkState.NO_NET);
                }

                if (response.code() == 401) {
                    Intent intent = new Intent(context, AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    context.startActivity(intent);
                    return;
                }

                Log.d(TAG, "onResponse: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<ModelFavorite> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
            }
        });

        return null;
    }


}