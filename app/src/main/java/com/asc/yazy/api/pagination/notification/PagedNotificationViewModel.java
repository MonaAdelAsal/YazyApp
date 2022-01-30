package com.asc.yazy.api.pagination.notification;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.asc.yazy.api.model.ModelNotification;
import com.asc.yazy.api.pagination.onLoadData;

import java.util.concurrent.Executor;


public class PagedNotificationViewModel extends ViewModel implements onLoadData {

    public LiveData<PagedList<ModelNotification>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, ModelNotification>> liveDataSource;
    private NotificationDataFactory itemDataSourceFactory;
    private onLoadData update;
    private MainThreadExecutor executor;

    //constructor
    public PagedNotificationViewModel() {

        executor = new MainThreadExecutor();

        itemDataSourceFactory = new NotificationDataFactory();

        //getting the live data source from data source factory
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(5).build();


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

    public LiveData<PageKeyedDataSource<Integer, ModelNotification>> getLiveDataSource() {
        return liveDataSource;
    }


    private static class MainThreadExecutor implements Executor {

        private final Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }

    }
}
