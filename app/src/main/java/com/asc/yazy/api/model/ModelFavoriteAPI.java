package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;


public class ModelFavoriteAPI {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private FavoriteModelDataList data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public FavoriteModelDataList getData() {
        return data;
    }
}
