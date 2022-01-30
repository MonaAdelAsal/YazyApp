package com.asc.yazy.api.pagination.favorite;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.asc.yazy.api.model.ModelFav;
import com.asc.yazy.api.pagination.onLoadData;


public class FavoriteDataFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, ModelFav>> itemLiveDataSource = new MutableLiveData<>();
    private FavoriteDataSource itemDataSource;
    private onLoadData update;

    @NonNull
    @Override
    public DataSource<Integer, ModelFav> create() {

        itemDataSource = new FavoriteDataSource(new MutableLiveData());


        itemLiveDataSource.postValue(itemDataSource);
        itemDataSource.setListener(update);

        return itemDataSource;
    }


    MutableLiveData<PageKeyedDataSource<Integer, ModelFav>> getItemLiveDataSource() {
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

