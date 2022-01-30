package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelSearchDataAPI {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private SearchDataModel data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public SearchDataModel getData() {
        return data;
    }

}

