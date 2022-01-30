package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelContactUs {


    @SerializedName("introduction")
    private String introduction;

    @SerializedName("emailAddress")
    private String email;

    @SerializedName("localAddress")
    private String address;

    @SerializedName("mobileNumber")
    private String mobile;

    public String getIntroduction() {
        return introduction;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }
}
