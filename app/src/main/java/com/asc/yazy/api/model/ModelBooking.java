package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelBooking {

    @SerializedName("offerTitle")
    private String title;

    @SerializedName("dateFrom")
    private String date_from;

    @SerializedName("dateTo")
    private String date_to;

    @SerializedName("image")
    private String image;

    @SerializedName("bookingId")
    private String booking_id;

    @SerializedName("statusId")
    private int status_mobile;

    @SerializedName("startDate")
    private String start_date;

    @SerializedName("endDate")
    private String end_date;

    @SerializedName("openPackage")
    private int open_package;

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public int getOpen_package() {
        return open_package;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public int getStatus_mobile() {
        return status_mobile;
    }

    public void setStatus_mobile(int status_mobile) {
        this.status_mobile = status_mobile;
    }

   
}
