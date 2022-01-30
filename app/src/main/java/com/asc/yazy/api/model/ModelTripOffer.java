package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelTripOffer implements Serializable {

    @SerializedName("date_from")
    private String date_from;

    @SerializedName("date_to")
    private String date_to;

    @SerializedName("country_name")
    private String country_name;

    @SerializedName("city_name")
    private String city_name;

    @SerializedName("hotel")
    private String hotel;

    @SerializedName("hotel_room_type")
    private String hotel_room_type;

    @SerializedName("airlines")
    private String airlines;

    @SerializedName("flight_class")
    private String flight_class;

    @SerializedName("travelAgencyName")
    private String travel_agency_name;

    @SerializedName("open_package")
    private int open_package;

    @SerializedName("duration_digits")
    private int duration_digits;

    public int getDuration_digits() {
        return duration_digits;
    }


    public int getOpen_package() {
        return open_package;
    }

    public void setOpen_package(int open_package) {
        this.open_package = open_package;
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

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getHotel_room_type() {
        return hotel_room_type;
    }

    public void setHotel_room_type(String hotel_room_type) {
        this.hotel_room_type = hotel_room_type;
    }

    public String getAirlines() {
        return airlines;
    }

    public void setAirlines(String airlines) {
        this.airlines = airlines;
    }

    public String getFlight_class() {
        return flight_class;
    }

    public void setFlight_class(String flight_class) {
        this.flight_class = flight_class;
    }

    public String getTravel_agency_name() {
        return travel_agency_name;
    }

    public void setTravel_agency_name(String travel_agency_name) {
        this.travel_agency_name = travel_agency_name;
    }
}
