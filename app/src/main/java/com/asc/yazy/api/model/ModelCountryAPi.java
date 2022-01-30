package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelCountryAPi {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ModelCountry> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ModelCountry> getData() {
        return data;
    }
}
