package com.asc.yazy.cash.room.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.asc.yazy.cash.room.model.NotificationRoomModel;

import java.util.List;

@Dao
public interface NotificationRoomDao {

    @Insert(onConflict = REPLACE)
    void insert(NotificationRoomModel model);

    @Update()
    void update(NotificationRoomModel model);

    @Delete
    void deleteNotification(NotificationRoomModel model);

    @Query("DELETE FROM NotificationRoomModel")
    void deleteAllNotification();

    @Query("DELETE FROM NotificationRoomModel where type =:type")
    void deleteAllNotificationByType(int type);

    @Query("SELECT * FROM NotificationRoomModel ORDER BY created_at desc")
    LiveData<List<NotificationRoomModel>> getAllNotifications();


    @Query("SELECT COUNT(NotificationID) FROM NotificationRoomModel WHERE isOpened = 0")
    LiveData<Integer> getAllNotOpenNotifications();

    @Query("SELECT * FROM NotificationRoomModel")
    Cursor getItemsWithCursor();

    @Query("SELECT * FROM NotificationRoomModel where NotificationID =:notificationID")
    NotificationRoomModel getNotification(int notificationID);

    @Query("UPDATE NotificationRoomModel SET isOpened = 1 ")
    void markAllRead();
}
