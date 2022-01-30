package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class BEBookingAPI {


    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ShareLink data;


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ShareLink getData() {
        return data;
    }


}
