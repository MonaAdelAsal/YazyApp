package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Offer implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("travelAgencyId")
    private int travel_agency_id;

    @SerializedName("price")
    private int price;

    @SerializedName("priceChild")
    private int price_child;

    @SerializedName("currency")
    private String currency;

    @SerializedName("description")
    private String description;

    @SerializedName("policy")
    private String policy;

    @SerializedName("image")
    private String image;

    @SerializedName("favourite_count")
    private int favourite_count;

    @SerializedName("dateFrom")
    private String date_from;

    @SerializedName("dateTo")
    private String date_to;

    @SerializedName("openPackage")
    private int open_package;

    @SerializedName("multiCities")
    private int multi_cities;

    @SerializedName("duration")
    private String duration;

    @SerializedName("durationDigits")
    private int duration_digits;

    @SerializedName("expired")
    private int expired;

    @SerializedName("acceptCivilId")
    private int accept_civil_id;


    @SerializedName("bookingsCount")
    private int bookings_count;

    @SerializedName("cities")
    private List<City> cities;

    @SerializedName("travelAgencyName")
    private String travel_agency_name;

    @SerializedName("travelAgencyRate")
    private double travel_agency_rate;

    @SerializedName("bookingCount")
    private int booking_count;

    @SerializedName("expirationDate")
    private String expiration_date;

    @SerializedName("countryCode")
    private int countryCode;

    @SerializedName("createdAt")
    private String created_at;
    @SerializedName("transactionId")
    private String transactionId;

    public int getCountryCode() {
        return countryCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getTravel_agency_id() {
        return travel_agency_id;
    }


    public int getPrice() {
        return price;
    }

    public int getPrice_child() {
        return price_child;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getPolicy() {
        return policy;
    }

    public String getImage() {
        return image;
    }

    public int getFavourite_count() {
        return favourite_count;
    }

    public String getDate_from() {
        return date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public int getOpen_package() {
        return open_package;
    }

    public String getDuration() {
        return duration;
    }

    public int getDuration_digits() {
        return duration_digits;
    }

    public int getExpired() {
        return expired;
    }

    public int getAccept_civil_id() {
        return accept_civil_id;
    }


    public int getBookings_count() {
        return bookings_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public List<City> getCities() {
        return cities;
    }

    public String getTravel_agency_name() {
        return travel_agency_name;
    }

    public double getTravel_agency_rate() {
        return travel_agency_rate;
    }

    public int getBooking_count() {
        return booking_count;
    }

    public int getMulti_cities() {
        return multi_cities;
    }
}
