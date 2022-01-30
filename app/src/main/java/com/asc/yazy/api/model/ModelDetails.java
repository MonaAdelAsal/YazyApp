package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModelDetails implements Serializable {


    @SerializedName("id")
    private String id;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private String price;

    @SerializedName("acceptCivilId")
    private int accept_civil_id;

    @SerializedName("priceChild")
    private String price_child;

    @SerializedName("airlineId")
    private String airlines;

    @SerializedName("hotel")
    private String hotel;

    @SerializedName("travelAgencyName")
    private String travel_agency_name;

    @SerializedName("travel_agency_id")
    private String travel_agency_id;

    @SerializedName("currency")
    private String currency;

    @SerializedName("title")
    private String title;

    @SerializedName("dateFrom")
    private String date_from;

    @SerializedName("dateTo")
    private String date_to;

    @SerializedName("image")
    private String image;


    @SerializedName("flightClass")
    private String fight_class;

    @SerializedName("duration")
    private String duration;

    @SerializedName("hotelRoomType")
    private String hotel_room_type;

    @SerializedName("isFavorited")
    private int is_favorited;

    @SerializedName("images")
    private List<String> images;

    @SerializedName("travelAgency")
    private ModelTravelAgency travel_agency_data;

    @SerializedName("durationDigits")
    private int duration_digits;


    @SerializedName("cities")
    private List<CityModel> cities;

    @SerializedName("openPackage")
    private int open_package;

    @SerializedName("accommodationServices")
    private List<String> accommodation_services;

    @SerializedName("policy")
    private String policy;

    @SerializedName("expirationDate")
    private String expiration_date;

    public String getExpiration_date() {
        return expiration_date;
    }

    public String getPolicy() {
        return policy;
    }

    public int getDuration_digits() {
        return duration_digits;
    }

    public void setDuration_digits(int duration_digits) {
        this.duration_digits = duration_digits;
    }

    public List<String> getAccommodation_services() {
        return accommodation_services;
    }

    public void setAccommodation_services(List<String> accommodation_services) {
        this.accommodation_services = accommodation_services;
    }

    public int getOpen_package() {
        return open_package;
    }

    public void setOpen_package(int open_package) {
        this.open_package = open_package;
    }

    public List<CityModel> getCities() {
        return cities;
    }

    public void setCities(List<CityModel> cities) {
        this.cities = cities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice_child() {
        if (price_child != null) {
            return price_child;
        } else {
            return price;
        }

    }

    public void setPrice_child(String price_child) {
        this.price_child = price_child;
    }

    public String getAirlines() {
        return airlines;
    }

    public void setAirlines(String airlines) {
        this.airlines = airlines;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getTravel_agency_name() {
        return travel_agency_name;
    }

    public void setTravel_agency_name(String travel_agency_name) {
        this.travel_agency_name = travel_agency_name;
    }

    public String getTravel_agency_id() {
        return travel_agency_id;
    }

    public void setTravel_agency_id(String travel_agency_id) {
        this.travel_agency_id = travel_agency_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public ModelTravelAgency getTravel_agency_data() {
        return travel_agency_data;
    }

    public void setTravel_agency_data(ModelTravelAgency travel_agency_data) {
        this.travel_agency_data = travel_agency_data;
    }

    public String getFight_class() {
        return fight_class;
    }

    public void setFight_class(String fight_class) {
        this.fight_class = fight_class;
    }

    public String getDuration() {
        return "( " +  duration + " )";
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getHotel_room_type() {
        return hotel_room_type;
    }

    public void setHotel_room_type(String hotel_room_type) {
        this.hotel_room_type = hotel_room_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIs_favorited() {
        return is_favorited;
    }

    public void setIs_favorited(int is_favorited) {
        this.is_favorited = is_favorited;
    }

    public int getAccept_civil_id() {
        return accept_civil_id;
    }

    public void setAccept_civil_id(int accept_civil_id) {
        this.accept_civil_id = accept_civil_id;
    }
}
