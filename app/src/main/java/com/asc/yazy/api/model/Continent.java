package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Continent {

    @SerializedName("offersAfrica")
    private List<ModelOffer> offersAfrica;

    @SerializedName("offersAntartica")
    private List<ModelOffer> offersAntartica;

    @SerializedName("offersAsia")
    private List<ModelOffer> offersAsia;

    @SerializedName("offersAustralia")
    private List<ModelOffer> offersAustralia;

    @SerializedName("offersEurope")
    private List<ModelOffer> offersEurope;

    @SerializedName("offersNAmerica")
    private List<ModelOffer> offersNAmerica;

    @SerializedName("offersSAmerica")
    private List<ModelOffer> offersSAmerica;

    public List<ModelOffer> getOffersAfrica() {
        return offersAfrica;
    }

    public List<ModelOffer> getOffersAntartica() {
        return offersAntartica;
    }

    public List<ModelOffer> getOffersAsia() {
        return offersAsia;
    }

    public List<ModelOffer> getOffersAustralia() {
        return offersAustralia;
    }

    public List<ModelOffer> getOffersEurope() {
        return offersEurope;
    }

    public List<ModelOffer> getOffersNAmerica() {
        return offersNAmerica;
    }

    public List<ModelOffer> getOffersSAmerica() {
        return offersSAmerica;
    }
}
