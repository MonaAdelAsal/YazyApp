package com.asc.yazy.api.pagination.booking;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.api.model.ModelBookingAPI;
import com.asc.yazy.api.pagination.PaginationProvider;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.api.pagination.onLoadData;
import com.asc.yazy.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDataSource extends PageKeyedDataSource<Integer, ModelBooking> {

    private onLoadData update;

    public void setListener(onLoadData update) {
        this.update = update;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ModelBooking> callback) {

        if (update != null)
            update.onDataLoaded(NetworkState.LOADING);

       /* if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.UN_AUTHORISE);
            return;
        }*/
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelBookingAPI> call = mApiService.getBooking("Bearer " + PaginationProvider.USER_TOKEN, Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelBookingAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelBookingAPI> call, @NonNull Response<ModelBookingAPI> response) {

                if (response.code() == 401) {
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }

                System.out.println("DATAMODEL on initial ");
                if (response.body() != null) {
                    //      if (update != null)
                    //    update.onDataLoaded(response.body().getData().getData_list().size());
                    // callback.onResult(response.body().getData().getData_list(), null, 2);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelBookingAPI> call, @NonNull Throwable t) {

                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
            }
        });
    }


    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ModelBooking> callback) {
      /*  if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.UN_AUTHORISE);
            return;
        }*/
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelBookingAPI> call = mApiService.getBooking("Bearer " + PaginationProvider.USER_TOKEN, Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelBookingAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelBookingAPI> call, @NonNull Response<ModelBookingAPI> response) {

                if (response.code() == 401) {
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }

                System.out.println("DATAMODEL on before ");

                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (response.body() != null) {
                    //      callback.onResult(response.body().getData().getData_list(), adjacentKey);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelBookingAPI> call, @NonNull Throwable t) {

                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
            }
        });

    }


    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ModelBooking> callback) {
       /* if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.UN_AUTHORISE);
            return;
        }*/
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelBookingAPI> call = mApiService.getBooking("Bearer " + PaginationProvider.USER_TOKEN, Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelBookingAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelBookingAPI> call, @NonNull Response<ModelBookingAPI> response) {
                System.out.println("DATAMODEL on after ");

                if (response.code() == 401) {
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }

                if (response.body() != null) {
                    //           callback.onResult(response.body().getData().getData_list(), params.key + 1);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelBookingAPI> call, @NonNull Throwable t) {

                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
            }
        });
    }


}
