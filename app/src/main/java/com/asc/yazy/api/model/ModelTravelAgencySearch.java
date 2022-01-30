package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelTravelAgencySearch {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public ModelTravelAgencySearch(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
