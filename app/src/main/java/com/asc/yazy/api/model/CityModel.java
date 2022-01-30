package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CityModel implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("mainCity")
    private String main_city;

    @SerializedName("cityId")
    private String city_id;

    @SerializedName("cityName")
    private String city_name;

    @SerializedName("sourceCityName")
    private String country_name;

    @SerializedName("hotel")
    private String hotel;

    @SerializedName("hotelRoomType")
    private String hotel_room_type;

    @SerializedName("flightClass")
    private String flight_class;

    @SerializedName("dateFrom")
    private String date_from;

    @SerializedName("date_to")
    private String date_to;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String image;

    @SerializedName("airlineId")
    private String airlines;

    @SerializedName("duration")
    private String duration;

    @SerializedName("accommodationServices")
    private List<String> accommodation_services;

    public List<String> getAccommodation_services() {
        return accommodation_services;
    }

    public void setAccommodation_services(List<String> accommodation_services) {
        this.accommodation_services = accommodation_services;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAirlines() {
        return airlines;
    }

    public void setAirlines(String airlines) {
        this.airlines = airlines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain_city() {
        return main_city;
    }

    public void setMain_city(String main_city) {
        this.main_city = main_city;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }



    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
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

    public String getFlight_class() {
        return flight_class;
    }

    public void setFlight_class(String flight_class) {
        this.flight_class = flight_class;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
