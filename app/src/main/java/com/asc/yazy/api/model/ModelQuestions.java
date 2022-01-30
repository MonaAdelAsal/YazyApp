package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelQuestions {


    @SerializedName("id")
    private String id;

    @SerializedName("question")
    private String question;

    @SerializedName("answer")
    private String answer;

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
