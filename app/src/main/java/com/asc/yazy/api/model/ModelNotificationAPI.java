package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ModelNotificationAPI {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ModelNotification> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    public List<ModelNotification> getData() {
        return data;
    }
}
