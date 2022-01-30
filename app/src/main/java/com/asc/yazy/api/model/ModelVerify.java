package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelVerify {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private String data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}
