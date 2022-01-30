package com.asc.yazy.utils;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.asc.yazy.cash.room.model.NotificationRoomModel;

import java.util.List;

public class StringDataSource extends PageKeyedDataSource<Integer, NotificationRoomModel> {

    public static final int PAGE_SIZE = 20;
    private StringListProvider provider;

    public StringDataSource(StringListProvider provider) {
        this.provider = provider;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, NotificationRoomModel> callback) {
        List<NotificationRoomModel> result = provider.getStringList();
        callback.onResult(result, 1, 1);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, NotificationRoomModel> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, NotificationRoomModel> callback) {

    }
}