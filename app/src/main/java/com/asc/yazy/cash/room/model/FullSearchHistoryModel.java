package com.asc.yazy.cash.room.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FullSearchHistoryModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String destination;

    private String dataFromValue;
    private String dataFrom;

    private String dataToValue;

    private String dataTo;

    private String time;

    @NonNull
    public String getDestination() {
        return destination;
    }

    public void setDestination(@NonNull String destination) {
        this.destination = destination;
    }

    public String getDataFromValue() {
        return dataFromValue;
    }

    public void setDataFromValue(String dataFromValue) {
        this.dataFromValue = dataFromValue;
    }

    public String getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(String dataFrom) {
        this.dataFrom = dataFrom;
    }

    public String getDataToValue() {
        return dataToValue;
    }

    public void setDataToValue(String dataToValue) {
        this.dataToValue = dataToValue;
    }

    public String getDataTo() {
        return dataTo;
    }

    public void setDataTo(String dataTo) {
        this.dataTo = dataTo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
