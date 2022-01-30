package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelFavorite {


    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


}
