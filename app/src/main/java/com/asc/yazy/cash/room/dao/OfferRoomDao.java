package com.asc.yazy.cash.room.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.asc.yazy.cash.room.model.OfferRoomModel;

import java.util.List;

@Dao
public interface OfferRoomDao {

    @Insert(onConflict = REPLACE)
    void insert(OfferRoomModel model);

    @Query("SELECT * FROM OfferRoomModel where localID =:id AND `like`=1")
    OfferRoomModel getOffer(String id);

    @Query("DELETE FROM OfferRoomModel")
    void delete();

    @Delete
    void deleteOffer(OfferRoomModel model);

    @Query("SELECT * FROM OfferRoomModel")
    LiveData<List<OfferRoomModel>> getAllOffers();

    @Query("SELECT * FROM OfferRoomModel where `like`=1")
    LiveData<List<OfferRoomModel>> getAllLikedOffers();

    @Query("SELECT * FROM OfferRoomModel where localID =:id")
    LiveData<OfferRoomModel> getOfferById(String id);

    @Query("UPDATE offerroommodel set currency =:currency where localID =:localID")
    void updateOfferByID(String localID, String currency);


}
