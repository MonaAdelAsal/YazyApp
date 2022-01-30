package com.asc.yazy.cash.room.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.asc.yazy.cash.room.model.FullSearchHistoryModel;

import java.util.List;

@Dao
public interface FullSearchHistoryDao {


    @Insert(onConflict = REPLACE)
    void insert(FullSearchHistoryModel model);

    @Update()
    void update(FullSearchHistoryModel model);

    @Delete
    void delete(FullSearchHistoryModel model);

    @Query("DELETE FROM FullSearchHistoryModel")
    void deleteAll();

    @Query("SELECT * FROM FullSearchHistoryModel ORDER BY time desc limit 5")
    LiveData<List<FullSearchHistoryModel>> getAll();


}
