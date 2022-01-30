package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelContinents {


    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
