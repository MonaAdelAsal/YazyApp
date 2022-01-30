package com.asc.yazy.api.pagination.booking;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.api.pagination.onLoadData;


public class BookingDataFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, ModelBooking>> itemLiveDataSource = new MutableLiveData<>();
    private BookingDataSource itemDataSource;
    private onLoadData update;

    @NonNull
    @Override
    public DataSource<Integer, ModelBooking> create() {

        itemDataSource = new BookingDataSource();


        itemLiveDataSource.postValue(itemDataSource);
        itemDataSource.setListener(update);

        return itemDataSource;
    }


    MutableLiveData<PageKeyedDataSource<Integer, ModelBooking>> getItemLiveDataSource() {
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

