package com.asc.yazy.api.pagination.search;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.model.ModelOfferAPI;
import com.asc.yazy.api.pagination.PaginationProvider;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.api.pagination.onLoadData;
import com.asc.yazy.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersSearchDataSource extends PageKeyedDataSource<Integer, ModelOffer> {


    private final String travelAgencyID;
    private final String cityID;
    private final String date;
    private final String flightClass;
    private final String maxPrice;
    private final String sortBy;
    private final String dateTo;
    private final String type;
    private final String duration;
    private final String tvAccommodations;
    private final String continent;
    private onLoadData update;

    OffersSearchDataSource(String travelAgencyID, String cityID, String date, String dateTo, String flightClass, String maxPrice, String sortBy, String type, String duration, String tvAccommodations, String continent) {
        this.travelAgencyID = travelAgencyID;
        this.cityID = cityID;
        this.date = date;
        this.dateTo = dateTo;
        this.flightClass = flightClass;
        this.maxPrice = maxPrice;
        this.sortBy = sortBy;
        this.type = type;
        this.duration = duration;
        this.tvAccommodations = tvAccommodations;
        this.continent = continent;
    }

    public void setListener(onLoadData update) {
        this.update = update;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ModelOffer> callback) {

        if (update != null)
            update.onDataLoaded(NetworkState.LOADING);

        /*if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.UN_AUTHORISE);
            return;
        }*/
        ApiInterface mApiService = ApiClient.getInterfaceService();
        String token = null;
        if (PaginationProvider.USER_TOKEN != null && !PaginationProvider.USER_TOKEN.isEmpty())
            token = "Bearer " + PaginationProvider.USER_TOKEN;
        Call<ModelOfferAPI> call = mApiService.getSearchResult(token, Constants.getLANGUAGE(), 1, travelAgencyID, cityID, date, dateTo, flightClass, maxPrice, sortBy, type, duration, tvAccommodations, continent);
        call.enqueue(new Callback<ModelOfferAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelOfferAPI> call, @NonNull Response<ModelOfferAPI> response) {

                System.out.println("DATAMODEL on initial " + "travelAgencyID " + travelAgencyID + " cityID " + cityID + " date " + date);

                if (response.code() == 401) {
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }


                if (response.body() != null) {
                    if (update != null)
                        update.onDataLoaded(response.body().getData().size());
                    callback.onResult(response.body().getData(), null, 2);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelOfferAPI> call, @NonNull Throwable t) {

                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
            }
        });
    }


    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ModelOffer> callback) {
       /* if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.UN_AUTHORISE);
            return;
        }*/
        ApiInterface mApiService = ApiClient.getInterfaceService();
        String token = null;
        if (PaginationProvider.USER_TOKEN != null && !PaginationProvider.USER_TOKEN.isEmpty())
            token = "Bearer " + PaginationProvider.USER_TOKEN;
        Call<ModelOfferAPI> call = mApiService.getSearchResult(token, Constants.getLANGUAGE(), params.key, travelAgencyID, cityID, date, dateTo, flightClass, maxPrice, sortBy, type, duration, tvAccommodations, continent);
        call.enqueue(new Callback<ModelOfferAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelOfferAPI> call, @NonNull Response<ModelOfferAPI> response) {

                System.out.println("DATAMODEL on before ");

                if (response.code() == 401) {
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }

                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (response.body() != null) {
                    callback.onResult(response.body().getData(), adjacentKey);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelOfferAPI> call, @NonNull Throwable t) {

                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
            }
        });

    }


    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ModelOffer> callback) {

      /*  if (PaginationProvider.USER_TOKEN == null || PaginationProvider.USER_TOKEN.isEmpty()) {
            if (update != null)
                update.onDataLoaded(NetworkState.UN_AUTHORISE);
            return;
        }*/
        ApiInterface mApiService = ApiClient.getInterfaceService();
        String token = null;
        if (PaginationProvider.USER_TOKEN != null && !PaginationProvider.USER_TOKEN.isEmpty())
            token = "Bearer " + PaginationProvider.USER_TOKEN;
        Call<ModelOfferAPI> call = mApiService.getSearchResult(token, Constants.getLANGUAGE(), params.key, travelAgencyID, cityID, date, dateTo, flightClass, maxPrice, sortBy, type, duration, tvAccommodations, continent);
        call.enqueue(new Callback<ModelOfferAPI>() {
            @Override
            public void onResponse(@NonNull Call<ModelOfferAPI> call, @NonNull Response<ModelOfferAPI> response) {
                System.out.println("DATAMODEL on after ");

                if (response.code() == 401) {
                    if (update != null)
                        update.onDataLoaded(NetworkState.UN_AUTHORISE);
                    return;
                }

                if (response.body() != null) {
                    callback.onResult(response.body().getData(), params.key + 1);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelOfferAPI> call, @NonNull Throwable t) {

                if (update != null)
                    update.onDataLoaded(NetworkState.NO_NET);
            }
        });
    }


}
