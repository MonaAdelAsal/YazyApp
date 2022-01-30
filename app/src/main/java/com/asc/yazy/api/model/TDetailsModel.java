package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TDetailsModel implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("mobileUserId")
    private int mobile_user_id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("travelAgencyId")
    private int travel_agency_id;

    @SerializedName("offerId")
    private int offer_id;

    @SerializedName("startDate")
    private String start_date;

    @SerializedName("endDate")
    private String end_date;

    @SerializedName("total")
    private int total;

    @SerializedName("currency")
    private String currency;

    @SerializedName("adults")
    private int adults;

    @SerializedName("children")
    private int children;

    @SerializedName("paymentGateway")
    private String payment_gateway;

    @SerializedName("transactionStatusCode")
    private int transaction_status_code;

    @SerializedName("transactionErrorMessage")
    private String transaction_error_message;

    @SerializedName("transactionId")
    private String transaction_id;

    @SerializedName("status")
    private String status;

    @SerializedName("statusId")
    private int status_mobile;

    @SerializedName("gift")
    private int gift;

    @SerializedName("createdAt")
    private String created_at;

    @SerializedName("updatedAt")
    private String updated_at;

    @SerializedName("countryCode")
    private String country_code;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("ticketsNumber")
    private int tickets_number;

    @SerializedName("offer")
    private Offer offer;

    @SerializedName("paymentLink")
    private String payment_link;

    @SerializedName("ticket")
    private String ticket;

    public String getTicket() {
        return ticket;
    }

    public String getPayment_link() {
        return payment_link;
    }

    public int getId() {
        return id;
    }

    public int getMobile_user_id() {
        return mobile_user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getTravel_agency_id() {
        return travel_agency_id;
    }

    public int getOffer_id() {
        return offer_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public int getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public String getPayment_gateway() {
        return payment_gateway;
    }

    public int getTransaction_status_code() {
        return transaction_status_code;
    }

    public String getTransaction_error_message() {
        return transaction_error_message;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getStatus() {
        return status;
    }

    public int getStatus_mobile() {
        return status_mobile;
    }

    public int getGift() {
        return gift;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getMobile() {
        return mobile;
    }

    public int getTickets_number() {
        return tickets_number;
    }

    public Offer getOffer() {
        return offer;
    }
}
