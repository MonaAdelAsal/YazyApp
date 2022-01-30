package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingModelDataList {


    @SerializedName("completed")
    private List<ModelBooking> completed;

    @SerializedName("pendding")
    private List<ModelBooking> pendding;

    @SerializedName("canceled")
    private List<ModelBooking> canceled;

    public List<ModelBooking> getCompleted() {
        return completed;
    }


    public List<ModelBooking> getPendding() {
        return pendding;
    }


    public List<ModelBooking> getCanceled() {
        return canceled;
    }
}
