package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelFAQsApi {


    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ModelFAQs> data;

    public List<ModelFAQs> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
