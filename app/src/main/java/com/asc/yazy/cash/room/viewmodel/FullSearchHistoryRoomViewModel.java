package com.asc.yazy.cash.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.asc.yazy.cash.room.FullSearchHistoryRepository;
import com.asc.yazy.cash.room.model.FullSearchHistoryModel;

import java.util.List;

public class FullSearchHistoryRoomViewModel extends AndroidViewModel {

    private LiveData<List<FullSearchHistoryModel>> liveData;
    private LiveData<Integer> notOpenedNotificationsCount;


    public FullSearchHistoryRoomViewModel(@NonNull Application application) {
        super(application);
        FullSearchHistoryRepository repository = new FullSearchHistoryRepository(application);
        liveData = repository.getAll();


    }

    public LiveData<List<FullSearchHistoryModel>> getAll() {
        return liveData;
    }

}
