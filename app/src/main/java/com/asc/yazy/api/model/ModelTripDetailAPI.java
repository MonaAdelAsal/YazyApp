package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelTripDetailAPI {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private TDetailsModel data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public TDetailsModel getData() {
        return data;
    }
}
