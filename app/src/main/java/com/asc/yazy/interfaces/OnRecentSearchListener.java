package com.asc.yazy.interfaces;

import com.asc.yazy.cash.room.model.SearchHistoryModel;

public interface OnRecentSearchListener {

    void onRecentSearchItemClicked(SearchHistoryModel historyModel);
}
