package com.asc.yazy.api.pagination.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.pagination.onLoadData;


public class OfferSearchDataFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, ModelOffer>> itemLiveDataSource = new MutableLiveData<>();
    private final String travelAgencyID;
    private final String cityID;
    private final String date;
    private final String dateTo;
    private final String flightClass;
    private final String maxPrice;
    private final String sortBy;
    private final String type;
    private final String duration;
    private final String tvAccommodations;
    private final String continent;
    private OffersSearchDataSource itemDataSource;
    private onLoadData update;

    OfferSearchDataFactory(String travelAgencyID, String cityID, String date, String dateTo, String flightClass, String maxPrice, String sortBy, String type, String duration, String tvAccommodations, String continent) {
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


    @NonNull
    @Override
    public DataSource<Integer, ModelOffer> create() {

        itemDataSource = new OffersSearchDataSource(travelAgencyID, cityID, date, dateTo, flightClass, maxPrice, sortBy, type, duration, tvAccommodations, continent);
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

