package com.asc.yazy.api.pagination.notification;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.asc.yazy.api.model.ModelNotification;
import com.asc.yazy.api.pagination.onLoadData;


public class NotificationDataFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, ModelNotification>> itemLiveDataSource = new MutableLiveData<>();
    private NotificationDataSource itemDataSource;
    private onLoadData update;

    @NonNull
    @Override
    public DataSource<Integer, ModelNotification> create() {

        itemDataSource = new NotificationDataSource();


        itemLiveDataSource.postValue(itemDataSource);
        itemDataSource.setListener(update);

        return itemDataSource;
    }


    MutableLiveData<PageKeyedDataSource<Integer, ModelNotification>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }

    void Change() {
        if (itemDataSource != null)
            itemDataSource.invalidate();
    }

    public void setUpdate(onLoadData update) {
        this.update = update;

    }
}

