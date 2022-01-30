package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class metaModel implements Serializable {
    @SerializedName("current_page")
    private String current_page;

    @SerializedName("total")
    private String total_count;

    @SerializedName("per_page")
    private String page_size;

    public String getCurrent_page() {
        return current_page;
    }

    public String getTotal_count() {
        return total_count;
    }

    public String getPage_size() {
        return page_size;
    }
}
