package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModelTravelAgency implements Serializable {

    @SerializedName("name")
    private String travel_agency_name;

    @SerializedName("id")
    private String travel_agency_id;

    @SerializedName("phones")
    private List<String> phones;

    @SerializedName("email")
    private String email;

    @SerializedName("address")
    private String address;

    @SerializedName("description")
    private String description;

    @SerializedName("total_rate")
    private float total_rate;

    @SerializedName("total_reviews")
    private int total_reviews;


    @SerializedName("working_hours")
    private String working_hours;

    @SerializedName("image")
    private String image;

    @SerializedName("details")
    private String details;

    public int getTotal_reviews() {
        return total_reviews;
    }

    public float getTotal_rate() {
        return total_rate;
    }

    public String getTravel_agency_name() {
        return travel_agency_name;
    }

    public String getTravel_agency_id() {
        return travel_agency_id;
    }

    public List<String> getPhones() {
        return phones;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getWorking_hours() {
           return working_hours;
       }

    public String getImage() {
        return image;
    }

    public String getDetails() {
        return details;
    }

    public String getDescription() {
        return description;
    }
}
