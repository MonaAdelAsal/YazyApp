package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class City implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("offer_id")
    private int offer_id;

    @SerializedName("mainCity")
    private int main_city;

    @SerializedName("cityId")
    private int city_id;

    @SerializedName("cityName")
    private String city_name;

    @SerializedName("sourceCityId")
    private String source_city_id;

    @SerializedName("sourceCityName")
    private String source_city_name;

    @SerializedName("hotel")
    private String hotel;

    @SerializedName("hotelRoomType")
    private String hotel_room_type;

    @SerializedName("flightClass")
    private String flight_class;

    @SerializedName("dateFrom")
    private String date_from;

    @SerializedName("dateTo")
    private String date_to;

    @SerializedName("duration")
    private String duration;

    @SerializedName("durationDigits")
    private int duration_digits;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String image;

    @SerializedName("airlineId")
    private String airlines;

    public int getId() {
        return id;
    }

    public int getOffer_id() {
        return offer_id;
    }

    public int getMain_city() {
        return main_city;
    }

    public int getCity_id() {
        return city_id;
    }

    public String getCity_name() {
        return city_name;
    }


    public String getSource_city_id() {
        return source_city_id;
    }

    public String getSource_city_name() {
        return source_city_name;
    }

    public String getHotel() {
        return hotel;
    }

    public String getHotel_room_type() {
        return hotel_room_type;
    }

    public String getFlight_class() {
        return flight_class;
    }

    public String getDate_from() {
        return date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public String getDuration() {
        return duration;
    }

    public int getDuration_digits() {
        return duration_digits;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getAirlines() {
        return airlines;
    }
}
