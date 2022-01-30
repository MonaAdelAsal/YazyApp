package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelOfferAPI {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<ModelOffer> data;

    @SerializedName("promotedOffers")
    private List<ModelOffer> promotedOffers;

    public List<ModelOffer> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ModelOffer> getPromotedOffers() {
        return promotedOffers;
    }
}
