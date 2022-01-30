package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PagesModel implements Serializable {
    @SerializedName("PageName")
    private String PageName;

    @SerializedName("pageContent")
    private String pageContent;

    public String getPageName() {
        return PageName;
    }

    public String getPageContent() {
        return pageContent;
    }
}
