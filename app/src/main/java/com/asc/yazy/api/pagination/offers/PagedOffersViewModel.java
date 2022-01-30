package com.asc.yazy.api.pagination.offers;

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


public class PagedOffersViewModel extends ViewModel implements onLoadData {

    private final LiveData<PageKeyedDataSource<Integer, ModelOffer>> liveDataSource;
    private final OfferDataFactory itemDataSourceFactory;
    public LiveData<PagedList<ModelOffer>> itemPagedList;
    private onLoadData update;

    //constructor
    public PagedOffersViewModel() {

        MainThreadExecutor executor = new MainThreadExecutor();

        itemDataSourceFactory = new OfferDataFactory();

        //getting the live data source from data source factory
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

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

    public LiveData<PageKeyedDataSource<Integer, ModelOffer>> getLiveDataSource() {
        return liveDataSource;
    }


    public static class MainThreadExecutor implements Executor {

        private final Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }

    }
}
