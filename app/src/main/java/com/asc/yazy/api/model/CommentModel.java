package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class CommentModel {

    @SerializedName("name")
    private String name;

    @SerializedName("comment")
    private String comment;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("rate")
    private String rate;

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getRate() {
        return rate;
    }
}
