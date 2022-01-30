package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class MYModelPointsAPI {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Point data;

    public Point getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
