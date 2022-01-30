package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelPoint {

    @SerializedName("discountId")
    private String discountId;

    @SerializedName("discount")
    private String discount;

    @SerializedName("discountPoints")
    private String discountPoints;

    @SerializedName("discountImage")
    private String discountImage;

    @SerializedName("discountDetailImage")
    private String discountDetailImage;

    public String getDiscountId() {
        return discountId;
    }

    public String getDiscount() {
        return discount;
    }

    public String getDiscountPoints() {
        return discountPoints;
    }

    public String getDiscountImage() {
        return discountImage;
    }

    public String getDiscountDetailImage() {
        return discountDetailImage;
    }
}
