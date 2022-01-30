package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ShareLink {

    @SerializedName("payment_link")
    private String payment_link;

    @SerializedName("booking_id")
    private String booking_id;

    @SerializedName("points")
    private String points;

    public String getPoints() {
        return points;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public String getPayment_link() {
        return payment_link;
    }
}
