package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromotesOfferedModel {

    @SerializedName("data")
    private List<ModelOffer> data;

    public List<ModelOffer> getData() {
        return data;
    }
}
