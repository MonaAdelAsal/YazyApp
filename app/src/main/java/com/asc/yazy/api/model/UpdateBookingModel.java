package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateBookingModel implements Serializable {
    @SerializedName("points")
    private String points;

    public String getPoints() {
        return points;
    }
}
