package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelFlightClass {

    @SerializedName("id")
    private String id;

    @SerializedName("value")
    private String value;

    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toggle() {
        this.checked = !checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
