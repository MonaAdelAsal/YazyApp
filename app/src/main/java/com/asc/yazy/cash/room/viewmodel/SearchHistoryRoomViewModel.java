package com.asc.yazy.cash.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.asc.yazy.cash.room.SearchHistoryRepository;
import com.asc.yazy.cash.room.model.SearchHistoryModel;

import java.util.List;

public class SearchHistoryRoomViewModel extends AndroidViewModel {

    private LiveData<List<SearchHistoryModel>> liveData;
    private LiveData<Integer> notOpenedNotificationsCount;


    public SearchHistoryRoomViewModel(@NonNull Application application) {
        super(application);
        SearchHistoryRepository repository = new SearchHistoryRepository(application);
        liveData = repository.getAll();


    }

    public LiveData<List<SearchHistoryModel>> getAllNotifications() {
        return liveData;
    }

}
