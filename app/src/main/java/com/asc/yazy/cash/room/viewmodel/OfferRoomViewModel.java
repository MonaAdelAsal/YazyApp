package com.asc.yazy.cash.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.asc.yazy.cash.room.OffersRepository;
import com.asc.yazy.cash.room.model.OfferRoomModel;

import java.util.List;

public class OfferRoomViewModel extends AndroidViewModel {

    private LiveData<List<OfferRoomModel>> liveData;
    private LiveData<List<OfferRoomModel>> likedOffers;

    public OfferRoomViewModel(@NonNull Application application) {
        super(application);
        OffersRepository repository = new OffersRepository(application);
        liveData = repository.getAllOffers();
        likedOffers = repository.getAllLikedOffers();
    }


    public LiveData<List<OfferRoomModel>> getAllOffers() {
        return liveData;
    }

    public LiveData<List<OfferRoomModel>> getLikedOffers() {
        return likedOffers;
    }
}
