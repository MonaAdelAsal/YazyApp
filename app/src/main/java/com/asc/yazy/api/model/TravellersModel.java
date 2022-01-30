package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TravellersModel {

    @SerializedName("adults")
    private List<ModelTravellerDetails> adults;

    @SerializedName("children")
    private List<ModelTravellerDetails> children;


    public List<ModelTravellerDetails> getAdults() {
        return adults;
    }

    public List<ModelTravellerDetails> getChildren() {
        return children;
    }
}
