package com.asc.yazy.api.pagination.favorite;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelFav;
import com.asc.yazy.api.model.ModelFavoriteAPI;
import com.asc.yazy.api.pagination.PaginationProvider;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.api.pagination.onLoadData;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteDataSource extends PageKeyedDataSource<Integer, ModelFav> {

    private final MutableLiveData<Object> networkState;
    private onLoadData update;

    FavoriteDataSource(MutableLiveData networkState) {
        this.networkState = new MutableLiveData<>();
    }

    public void setListener(onLoadData update) {
        this.update = update;
    }

    public MutableLiveData<Object> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ModelFav> callback) {

        if (update != null)
            update.onDataLoaded(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        /*
        if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.UN_AUTHORISE);
            return;
        }
        */
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelFavoriteAPI> call = mApiService.getFavorite("Bearer " + PaginationProvider.USER_TOKEN, Constants.getLANGUAGE(), 1);
        call.enqueue(new Callback<ModelFavoriteAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelFavoriteAPI> call, @NonNull Response<ModelFavoriteAPI> response) {

                System.out.println("DATAMODEL on initial ");

                if (response.code() == 401) {
                    AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.FAVORITE);
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }

                if (response.body() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.FAVORITE);
                    if (update != null)
                        update.onDataLoaded(response.body().getData().getData_list().size());
                    callback.onResult(response.body().getData().getData_list(), null, 2);
                    networkState.postValue(NetworkState.LOADED);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelFavoriteAPI> call, @NonNull Throwable t) {

                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.FAVORITE);
            }
        });
    }


    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ModelFav> callback) {
      /*
        if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.NO_NET);
            return;
        }
        */

        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelFavoriteAPI> call = mApiService.getFavorite("Bearer " + PaginationProvider.USER_TOKEN, Constants.getLANGUAGE(), params.key);
        call.enqueue(new Callback<ModelFavoriteAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelFavoriteAPI> call, @NonNull Response<ModelFavoriteAPI> response) {

                System.out.println("DATAMODEL on before ");

                if (response.code() == 401) {
                    AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.FAVORITE);
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }

                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (response.body() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.FAVORITE);
                    callback.onResult(response.body().getData().getData_list(), adjacentKey);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelFavoriteAPI> call, @NonNull Throwable t) {

                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.FAVORITE);
            }
        });

    }


    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ModelFav> callback) {
       /*
        if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.NO_NET);
            return;
        }
        */
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelFavoriteAPI> call = mApiService.getFavorite("Bearer " + PaginationProvider.USER_TOKEN, Constants.getLANGUAGE(), params.key);
        call.enqueue(new Callback<ModelFavoriteAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelFavoriteAPI> call, @NonNull Response<ModelFavoriteAPI> response) {
                System.out.println("DATAMODEL on after ");

                if (response.code() == 401) {
                    AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.FAVORITE);
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }

                if (response.body() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.FAVORITE);
                    callback.onResult(response.body().getData().getData_list(), params.key + 1);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelFavoriteAPI> call, @NonNull Throwable t) {
                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.FAVORITE);
            }
        });
    }


}
