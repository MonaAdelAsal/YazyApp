package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelPriceLimit {

    @SerializedName("MIN_PRICE")
    private String MIN_PRICE;

    @SerializedName("MAX_PRICE")
    private String MAX_PRICE;

    public String getMIN_PRICE() {
        return MIN_PRICE;
    }

    public String getMAX_PRICE() {
        return MAX_PRICE;
    }
}
