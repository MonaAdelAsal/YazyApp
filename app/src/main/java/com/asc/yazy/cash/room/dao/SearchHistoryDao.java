package com.asc.yazy.cash.room.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.asc.yazy.cash.room.model.SearchHistoryModel;

import java.util.List;

@Dao
public interface SearchHistoryDao {


    @Insert(onConflict = REPLACE)
    void insert(SearchHistoryModel model);

    @Update()
    void update(SearchHistoryModel model);

    @Delete
    void delete(SearchHistoryModel model);

    @Query("DELETE FROM SearchHistoryModel")
    void deleteAll();

    @Query("SELECT * FROM SearchHistoryModel ORDER BY date desc")
    LiveData<List<SearchHistoryModel>> getAll();


}
