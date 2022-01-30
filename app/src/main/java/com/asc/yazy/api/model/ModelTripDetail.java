package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelTripDetail implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("offer")
    private ModelTripOffer offer;

    @SerializedName("name")
    private String name;

    @SerializedName("country_code")
    private String country_code;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("adults")
    private String adults;

    @SerializedName("tickets_number")
    private String tickets_number;
    @SerializedName("children")
    private String children;

    @SerializedName("transaction_id")
    private String transaction_id;

    @SerializedName("currency")
    private String currency;

    @SerializedName("total")
    private String total;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("start_date")
    private String start_date;

    @SerializedName("end_date")
    private String end_date;

    @SerializedName("status_mobile")
    private int status_mobile;

    @SerializedName("gift")
    private int gift;


    @SerializedName("payment_link")
    private String payment_link;


    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getCreated_at() {
        return created_at.split(" ")[0];
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ModelTripOffer getOffer() {
        return offer;
    }

    public void setOffer(ModelTripOffer offer) {
        this.offer = offer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAdults() {
        return adults;
    }

    public void setAdults(String adults) {
        this.adults = adults;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTickets_number() {
        return tickets_number;
    }

    public void setTickets_number(String tickets_number) {
        this.tickets_number = tickets_number;
    }

    public int getStatus_mobile() {
        return status_mobile;
    }

    public void setStatus_mobile(int status_mobile) {
        this.status_mobile = status_mobile;
    }

    public String getPayment_link() {
        return payment_link;
    }

    public void setPayment_link(String payment_link) {
        this.payment_link = payment_link;
    }

    public int getGift() {
        return gift;
    }

    public void setGift(int gift) {
        this.gift = gift;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
