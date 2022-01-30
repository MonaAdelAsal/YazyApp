package com.asc.yazy.api.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelOffer implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("price")
    private String price;

    @SerializedName("priceChild")
    private String price_child;

    @SerializedName("currency")
    private String currency;

    @SerializedName("title")
    private String title;

    @SerializedName("destination")
    private String destination;

    @SerializedName("dateFrom")
    private String date_from;

    @SerializedName("dateTo")
    private String date_to;

    @SerializedName("image")
    private String image;

    @SerializedName("favouriteCount")
    private String favourite_count;

    @SerializedName("isFavorited")
    private int is_favorited;

    @SerializedName("acceptCivilId")
    private int accept_civil_id;

    @SerializedName("multiCities")
    private int multi_cities;

    @SerializedName("duration")
    private String duration;

    @SerializedName("openPackage")
    private int open_package;

    @SerializedName("policy")
    private String policy;

    @SerializedName("expirationDate")
    private String expirationDate;
    private MutableLiveData<Integer> mutFav = new MutableLiveData<>();

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public int getOpen_package() {
        return open_package;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getMulti_cities() {
        return multi_cities;
    }

    public void setMulti_cities(int multi_cities) {
        this.multi_cities = multi_cities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getFavorites_count() {
        return favourite_count;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFavourite_count() {
        return favourite_count;
    }

    public void setFavourite_count(String favourite_count) {
        this.favourite_count = favourite_count;
    }

    public int getIs_favorited() {
        if (is_favorited == 1)
            mutFav.postValue(1);
        else
            mutFav.postValue(0);
        return is_favorited;
    }

    public void setIs_favorited(int is_favorited) {
        if (this.is_favorited == 0 && is_favorited == 1)
            mutFav.postValue(1);
        else if (this.is_favorited == 1 && is_favorited == 0)
            mutFav.postValue(0);
        this.is_favorited = is_favorited;
    }

    public LiveData<Integer> getLiveFav() {
        return mutFav;

    }

    public int getAccept_civil_id() {
        return accept_civil_id;
    }

    public void setAccept_civil_id(int accept_civil_id) {
        this.accept_civil_id = accept_civil_id;
    }
}
