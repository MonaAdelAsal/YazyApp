package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

public class ModelUser {

    @SerializedName("id")
    private String id;

    @SerializedName("country_code")
    private String country_code;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("passport_no")
    private String passport_no;

    @SerializedName("passport_expiry")
    private String passport_expiry;

    @SerializedName("nationality")
    private String nationality;

    @SerializedName("civil_id")
    private String civil_id;

    @SerializedName("enable_notification")
    private int enable_notification;

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("firebase_token")
    private String firebase_token;

    @SerializedName("points")
    private String points;
    private boolean rememberMe;
    private String password;

    public String getPoints() {
        return points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassport_no() {
        return passport_no;
    }

    public void setPassport_no(String passport_no) {
        this.passport_no = passport_no;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCivil_id() {
        return civil_id;
    }

    public void setCivil_id(String civil_id) {
        this.civil_id = civil_id;
    }

    public int getEnable_notification() {
        return enable_notification;
    }

    public void setEnable_notification(int enable_notification) {
        this.enable_notification = enable_notification;
    }

    public String getPassport_expiry() {
        return passport_expiry;
    }

    public void setPassport_expiry(String passport_expiry) {
        this.passport_expiry = passport_expiry;
    }

    public String getFirebase_token() {
        return firebase_token;
    }

    public void setFirebase_token(String firebase_token) {
        this.firebase_token = firebase_token;
    }
}
