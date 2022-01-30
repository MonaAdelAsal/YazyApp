package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelCountry {


    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("currency")
    private String currency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
