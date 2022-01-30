package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelFav {

    @SerializedName("offerId")
    private String id;

    @SerializedName("offerTitle")
    private String title;

    @SerializedName("offerDateFrom")
    private String date_from;

    @SerializedName("offerDateTo")
    private String date_to;

    @SerializedName("offerImage")
    private String image;

    @SerializedName("offerPrice")
    private String price;

    @SerializedName("offerCurrency")
    private String currency;

    @SerializedName("offerExpired")
    private int expired;

    @SerializedName("offerFavouriteCount")
    private String favourite_count;

    @SerializedName("offerFavourited")
    private int is_favorited;

    @SerializedName("offerDuration")
    private String offerDuration;

    @SerializedName("offerOpenPackage")
    private String offerOpenPackage;

    public String getOfferDuration() {
        return offerDuration;
    }

    public String getOfferOpenPackage() {
        return offerOpenPackage;
    }

    public int getIs_favorited() {
        return is_favorited;
    }

    public void setIs_favorited(int is_favorited) {
        this.is_favorited = is_favorited;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate_from() {
        return date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public int getExpired() {
        return expired;
    }

    public String getFavourite_count() {
        return favourite_count;
    }

}
