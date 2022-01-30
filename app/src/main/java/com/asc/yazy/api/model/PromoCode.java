package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class PromoCode {

    @SerializedName("promocodeId")
    private String promocodeId;

    @SerializedName("promocode")
    private String promocode;

    @SerializedName("promocodeType")
    private String promocodeType;

    @SerializedName("promocodeValue")
    private String promocodeValue;

    @SerializedName("promocodeDescription")
    private String promocodeDescription;

    public String getPromocodeId() {
        return promocodeId;
    }

    public String getPromocode() {
        return promocode;
    }

    public String getPromocodeType() {
        return promocodeType;
    }

    public String getPromocodeValue() {
        return promocodeValue;
    }

    public String getPromocodeDescription() {
        return promocodeDescription;
    }
}
