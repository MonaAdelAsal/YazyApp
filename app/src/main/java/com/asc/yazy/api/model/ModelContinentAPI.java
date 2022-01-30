package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelContinentAPI {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Continent data;

    @SerializedName("promotedOffers")
    private List<ModelOffer> promotedOffers;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Continent getData() {
        return data;
    }

    public List<ModelOffer> getPromotedOffers() {
        return promotedOffers;
    }
}
