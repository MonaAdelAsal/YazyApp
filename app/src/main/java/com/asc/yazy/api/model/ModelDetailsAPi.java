package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelDetailsAPi {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ModelDetails data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ModelDetails getData() {
        return data;
    }
}
