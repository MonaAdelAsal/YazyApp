package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelDestination {


    @SerializedName("name")
    private String name;

    public ModelDestination(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
