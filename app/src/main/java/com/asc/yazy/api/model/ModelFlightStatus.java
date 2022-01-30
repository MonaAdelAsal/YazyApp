package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelFlightStatus {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ModelFlight data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ModelFlight getData() {
        return data;
    }
}
