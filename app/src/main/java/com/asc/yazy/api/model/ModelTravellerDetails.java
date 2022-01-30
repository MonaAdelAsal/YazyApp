package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelTravellerDetails {

    @SerializedName("travellerID")
    private String id;

    @SerializedName("mobile_user_id")
    private String mobile_user_id;

    @SerializedName("travellerName")
    private String name;

    @SerializedName("travellerNationality")
    private String nationality;

    @SerializedName("travellerPassportNumber")
    private String passport_no;

    @SerializedName("travellerPassportExpiry")
    private String passPortEXDate;

    @SerializedName("travellerBirthDate")
    private String travellerBirthDate;

    @SerializedName("travellerCivilID")
    private String civil_id;

    @SerializedName("travellerType")
    private int type;

    private boolean selected = false;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile_user_id() {
        return mobile_user_id;
    }

    public void setMobile_user_id(String mobile_user_id) {
        this.mobile_user_id = mobile_user_id;
    }

    public String getName() {
        if (name == null)
            return "";
        else
            return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        if (nationality == null)
            return "";
        else
            return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassport_no() {
        if (passport_no == null)
            return "";
        else
            return passport_no;
    }

    public void setPassport_no(String passport_no) {
        this.passport_no = passport_no;
    }

    public String getCivil_id() {
        if (civil_id == null)
            return "";
        else
            return civil_id;
    }

    public void setCivil_id(String civil_id) {
        this.civil_id = civil_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggle() {
        this.selected = !selected;
    }

    public String getTravellerPassportExpiry() {
        try {
            String[] list2 = passPortEXDate.split("-");
            return list2[2] + "/" + list2[1] + "/" + list2[0];
        } catch (Exception e) {
            if (passPortEXDate == null)
                return "";
            else
                return passPortEXDate;
        }

    }

    public void setTravellerPassportExpiry(String travellerPassportExpiry) {
        this.passPortEXDate = travellerPassportExpiry;
    }

    public String getTravellerBirthDate() {
        try {
            String[] list = travellerBirthDate.split("-");
            return list[2] + "/" + list[1] + "/" + list[0];
        } catch (Exception e) {
            if (travellerBirthDate == null)
                return "";
            else
                return travellerBirthDate;
        }
    }


    public void setTravellerBirthDate(String travellerBirthDate) {
        this.travellerBirthDate = travellerBirthDate;
    }


}
