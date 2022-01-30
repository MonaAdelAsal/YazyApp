package com.asc.yazy.cash.room;


import static com.asc.yazy.utils.Constants.DATA_BASE_NAME;
import static com.asc.yazy.utils.Constants.DATA_BASE_VERSION;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.asc.yazy.cash.room.dao.FullSearchHistoryDao;
import com.asc.yazy.cash.room.dao.NotificationRoomDao;
import com.asc.yazy.cash.room.dao.OfferRoomDao;
import com.asc.yazy.cash.room.dao.SearchHistoryDao;
import com.asc.yazy.cash.room.model.FullSearchHistoryModel;
import com.asc.yazy.cash.room.model.NotificationRoomModel;
import com.asc.yazy.cash.room.model.OfferRoomModel;
import com.asc.yazy.cash.room.model.SearchHistoryModel;

@Database(entities = {OfferRoomModel.class, NotificationRoomModel.class, SearchHistoryModel.class, FullSearchHistoryModel.class}, version = DATA_BASE_VERSION, exportSchema = false)
public abstract class TravelXRoomDateBase extends RoomDatabase {


    private static TravelXRoomDateBase instance;

    public static TravelXRoomDateBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TravelXRoomDateBase.class, DATA_BASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract OfferRoomDao offerRoomDao();

    public abstract NotificationRoomDao NotificationRoomDao();

    public abstract SearchHistoryDao searchHistoryDao();

    public abstract FullSearchHistoryDao FullSearchHistoryDao();


}
