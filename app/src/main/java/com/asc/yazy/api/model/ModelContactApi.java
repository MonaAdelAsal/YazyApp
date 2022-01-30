package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelContactApi {


    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ModelContactUs data;

    public ModelContactUs getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
