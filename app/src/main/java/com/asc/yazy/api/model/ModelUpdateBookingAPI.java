package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelUpdateBookingAPI {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private UpdateBookingModel data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public UpdateBookingModel getData() {
        return data;
    }
}
