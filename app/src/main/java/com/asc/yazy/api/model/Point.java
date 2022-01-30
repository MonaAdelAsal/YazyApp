package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class Point {

    @SerializedName("points")
    private String points;

    public String getPoints() {
        return points;
    }
}
