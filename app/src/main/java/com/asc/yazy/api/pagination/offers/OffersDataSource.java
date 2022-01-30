package com.asc.yazy.api.pagination.offers;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.model.ModelOfferAPI;
import com.asc.yazy.api.pagination.PaginationProvider;
import com.asc.yazy.api.pagination.onLoadData;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersDataSource extends PageKeyedDataSource<Integer, ModelOffer> {


    private onLoadData update;

    public void setListener(onLoadData update) {
        this.update = update;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ModelOffer> callback) {

        if (update != null)
            update.onDataLoaded(NetworkState.LOADING);

      /*  if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.UN_AUTHORISE);
            return;
        }*/
        ApiInterface mApiService = ApiClient.getInterfaceService();
        String token = null;
        if (PaginationProvider.USER_TOKEN != null && !PaginationProvider.USER_TOKEN.isEmpty())
            token = "Bearer " + PaginationProvider.USER_TOKEN;
        Call<ModelOfferAPI> call = mApiService.getOffers(token, Constants.getLANGUAGE(), 1);
        call.enqueue(new Callback<ModelOfferAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelOfferAPI> call, @NonNull Response<ModelOfferAPI> response) {
                System.out.println("DATAMODEL on ini ");
/*
                if (response.code() == 401) {
                    AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.HOME);
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }
*/
                if (response.body() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.HOME);
                    if (update != null)
                        update.onDataLoaded(response.body().getData().size());
                    callback.onResult(response.body().getData(), null, 2);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelOfferAPI> call, @NonNull Throwable t) {

                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.HOME);

            }
        });
    }



    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ModelOffer> callback) {

      /*  if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.UN_AUTHORISE);
            return;
        }*/

        ApiInterface mApiService = ApiClient.getInterfaceService();
        String token = null;
        if (PaginationProvider.USER_TOKEN != null && !PaginationProvider.USER_TOKEN.isEmpty())
            token = "Bearer " + PaginationProvider.USER_TOKEN;
        Call<ModelOfferAPI> call = mApiService.getOffers(token, Constants.getLANGUAGE(), params.key);
        call.enqueue(new Callback<ModelOfferAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelOfferAPI> call, @NonNull Response<ModelOfferAPI> response) {

                System.out.println("DATAMODEL on before ");
/*
                if (response.code() == 401) {
                    AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.HOME);
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }
*/

                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (response.body() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.HOME);
                    callback.onResult(response.body().getData(), adjacentKey);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelOfferAPI> call, @NonNull Throwable t) {

                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.HOME);

            }
        });

    }


    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ModelOffer> callback) {

       /* if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.UN_AUTHORISE);
            return;
        }
*/
        ApiInterface mApiService = ApiClient.getInterfaceService();
        String token = null;
        if (PaginationProvider.USER_TOKEN != null && !PaginationProvider.USER_TOKEN.isEmpty())
            token = "Bearer " + PaginationProvider.USER_TOKEN;
        Call<ModelOfferAPI> call = mApiService.getOffers(token, Constants.getLANGUAGE(), params.key);
        call.enqueue(new Callback<ModelOfferAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelOfferAPI> call, @NonNull Response<ModelOfferAPI> response) {
                System.out.println("DATAMODEL on after ");
/*
                if (response.code() == 401) {
                    AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.HOME);
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }
*/
                if (response.body() != null) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.HOME);
                    callback.onResult(response.body().getData(), params.key + 1);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelOfferAPI> call, @NonNull Throwable t) {
                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.HOME);
            }
        });
    }


}
