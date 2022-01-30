package com.asc.yazy.utils;

import com.asc.yazy.cash.room.model.NotificationRoomModel;

import java.util.List;

public class StringListProvider {

    private List<NotificationRoomModel> list;

    public StringListProvider(List<NotificationRoomModel> list) {
        this.list = list;
    }

    List<NotificationRoomModel> getStringList() {
        return list;
    }
}