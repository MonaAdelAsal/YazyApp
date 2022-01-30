package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelAuthenticate {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ModelUser data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ModelUser getData() {
        return data;
    }
}
