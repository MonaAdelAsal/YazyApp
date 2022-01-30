package com.asc.yazy.core;

import com.asc.yazy.api.model.ModelDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PaymentModel implements Serializable {

    private double amount;

    private int adults;

    private int child;

    private int gift;

    private String currency;

    private String offerId;

    private String chargeID;

    private String statusCode;

    private String statusMessage;

    private String travelAgencyId;

    private String paymentGateWay;

    private String destination;

    private String adultList;

    private String childList;
    private String startDate;


    private String countryCode;
    private String phoneNumber;
    private String email;

    private String promoCodeID;


    private List<AdultsCountObserver> originalAdults = new ArrayList<>();
    private List<AdultsCountObserver> originalChild = new ArrayList<>();
    private ModelDetails offer;
    private String title;

    public List<AdultsCountObserver> getOriginalAdults() {
        return originalAdults;
    }

    public void setOriginalAdults(List<AdultsCountObserver> originalAdults) {
        this.originalAdults = originalAdults;
    }

    public List<AdultsCountObserver> getOriginalChild() {
        return originalChild;
    }

    public void setOriginalChild(List<AdultsCountObserver> originalChild) {
        this.originalChild = originalChild;
    }


    public int getGift() {
        return gift;
    }

    public void setGift(int gift) {
        this.gift = gift;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getAmountInt() {
        return (int) amount;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getChargeID() {
        return chargeID;
    }

    public void setChargeID(String chargeID) {
        this.chargeID = chargeID;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getTravelAgencyId() {
        return travelAgencyId;
    }

    public void setTravelAgencyId(String travelAgencyId) {
        this.travelAgencyId = travelAgencyId;
    }

    public String getPaymentGateWay() {
        return paymentGateWay;
    }

    public void setPaymentGateWay(String paymentGateWay) {
        this.paymentGateWay = paymentGateWay;
    }

    public String getAdultList() {
        return adultList;
    }

    public void setAdultList(String adultList) {
        this.adultList = adultList;
    }

    public String getChildList() {
        return childList;
    }

    public void setChildList(String childList) {
        this.childList = childList;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public ModelDetails getOffer() {
        return offer;
    }

    public void setOffer(ModelDetails offer) {
        this.offer = offer;
    }

    public String getPromoCodeID() {
        return promoCodeID;
    }

    public void setPromoCodeID(String promoCodeID) {
        this.promoCodeID = promoCodeID;
    }
}
