package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelFlight implements Serializable {


    @SerializedName("days")
    private String days;

    @SerializedName("booking_id")
    private String booking_id;

    @SerializedName("booking_title")
    private String booking_title;

    @SerializedName("color")
    private String color;

    public String getDays() {
        return days;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public String getBooking_title() {
        return booking_title;
    }

    public String getColor() {
        return color;
    }
}
