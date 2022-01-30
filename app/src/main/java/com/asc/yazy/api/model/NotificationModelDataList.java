package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModelDataList {

    @SerializedName("current_page")
    private String current_page;

    @SerializedName("total_count")
    private String total_count;

    @SerializedName("page_size")
    private String page_size;

    @SerializedName("data_list")
    private List<ModelNotification> data_list;

    public String getCurrent_page() {
        return current_page;
    }

    public String getTotal_count() {
        return total_count;
    }

    public String getPage_size() {
        return page_size;
    }

    public List<ModelNotification> getData_list() {
        return data_list;
    }
}
