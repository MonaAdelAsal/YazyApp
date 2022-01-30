package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelFAQs {


    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    private int clicked = 0;

    @SerializedName("questions")
    private List<ModelQuestions> questions;

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ModelQuestions> getQuestions() {
        return questions;
    }
}
