package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelTravellerAPI {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private TravellersModel data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public TravellersModel getData() {
        return data;
    }

}
