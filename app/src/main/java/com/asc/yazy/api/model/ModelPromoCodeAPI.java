package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelPromoCodeAPI {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private PromoCode data;

    public PromoCode getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
