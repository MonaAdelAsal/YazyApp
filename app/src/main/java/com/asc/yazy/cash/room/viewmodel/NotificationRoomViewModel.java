package com.asc.yazy.cash.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.asc.yazy.cash.room.NotificationsRepository;
import com.asc.yazy.cash.room.model.NotificationRoomModel;

import java.util.List;

public class NotificationRoomViewModel extends AndroidViewModel {

    private LiveData<List<NotificationRoomModel>> liveData;
    private LiveData<Integer> notOpenedNotificationsCount;


    public NotificationRoomViewModel(@NonNull Application application) {
        super(application);
        NotificationsRepository repository = new NotificationsRepository(application);
        liveData = repository.getAllNotifications();
        notOpenedNotificationsCount = repository.getAllNotOpenedNotifications();

    }

    public LiveData<List<NotificationRoomModel>> getAllNotifications() {
        return liveData;
    }

    public LiveData<Integer> getNotOpenedNotificationsCount() {
        return notOpenedNotificationsCount;
    }
}
