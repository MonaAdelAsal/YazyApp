package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TravelAgencyRate {

    @SerializedName("id")
    private String id;

    @SerializedName("travelAgencyName")
    private String travel_agency_name;

    @SerializedName("total_rate")
    private float total_rate;

    @SerializedName("total_reviews")
    private int total_reviews;

    @SerializedName("rates")
    private List<CommentModel> rates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTravel_agency_name() {
        return travel_agency_name;
    }

    public void setTravel_agency_name(String travel_agency_name) {
        this.travel_agency_name = travel_agency_name;
    }

    public float getTotal_rate() {
        return total_rate;
    }

    public void setTotal_rate(float total_rate) {
        this.total_rate = total_rate;
    }

    public int getTotal_reviews() {
        return total_reviews;
    }

    public void setTotal_reviews(int total_reviews) {
        this.total_reviews = total_reviews;
    }

    public List<CommentModel> getRates() {
        return rates;
    }

    public void setRates(List<CommentModel> rates) {
        this.rates = rates;
    }
}
