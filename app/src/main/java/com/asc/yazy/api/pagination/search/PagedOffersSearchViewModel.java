package com.asc.yazy.api.pagination.search;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.pagination.onLoadData;

import java.util.concurrent.Executor;


public class PagedOffersSearchViewModel extends ViewModel implements onLoadData {

    private final OfferSearchDataFactory itemDataSourceFactory;
    public LiveData<PagedList<ModelOffer>> itemPagedList;
    private onLoadData update;



    //constructor
    public PagedOffersSearchViewModel(String travelAgencyID, String cityID, String date, String dateTo, String flightClass, String maxPrice, String sortBy, String type, String duration, String tvAccommodations, String continent) {

        MainThreadExecutor executor = new MainThreadExecutor();

        itemDataSourceFactory = new OfferSearchDataFactory(travelAgencyID, cityID, date, dateTo, flightClass, maxPrice, sortBy, type, duration, tvAccommodations, continent);

        LiveData<PageKeyedDataSource<Integer, ModelOffer>> liveDataSource = itemDataSourceFactory.getItemLiveDataSource();


        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(10).build();


        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, pagedListConfig)).setFetchExecutor(executor).build();
        itemDataSourceFactory.setUpdate(this);


    }

    public void change() {
        itemDataSourceFactory.Change();
    }

    public void setUpdate(onLoadData update) {
        this.update = update;
    }

    @Override
    public void onDataLoaded(int count) {
        if (update != null)
            update.onDataLoaded(count);
    }


    private static class MainThreadExecutor implements Executor {

        private final Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }

    }

}
