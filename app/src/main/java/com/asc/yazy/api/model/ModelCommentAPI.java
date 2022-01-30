package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelCommentAPI {


    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private TravelAgencyRate data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public TravelAgencyRate getData() {
        return data;
    }
}
