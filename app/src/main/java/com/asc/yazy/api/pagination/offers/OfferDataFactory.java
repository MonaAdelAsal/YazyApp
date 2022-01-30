package com.asc.yazy.api.pagination.offers;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.pagination.onLoadData;


public class OfferDataFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, ModelOffer>> itemLiveDataSource = new MutableLiveData<>();
    private OffersDataSource itemDataSource;
    private onLoadData update;

    @NonNull
    @Override
    public DataSource<Integer, ModelOffer> create() {

        itemDataSource = new OffersDataSource();


        itemLiveDataSource.postValue(itemDataSource);
        itemDataSource.setListener(update);

        return itemDataSource;
    }


    MutableLiveData<PageKeyedDataSource<Integer, ModelOffer>> getItemLiveDataSource() {
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

