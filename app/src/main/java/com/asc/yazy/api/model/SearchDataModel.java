package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchDataModel {

    @SerializedName("destinations")
    private List<ModelDestination> destinations;
    @SerializedName("travel_agencies")
    private List<ModelTravelAgencySearch> travel_agencies;

    @SerializedName("price_limits")
    private ModelPriceLimit price_limits;

    @SerializedName("flight_class")
    private List<ModelFlightClass> flight_class;

    @SerializedName("continents")
    private List<ModelContinents> continents;

    public List<ModelContinents> getContinents() {
        return continents;
    }

    public List<ModelDestination> getDestinations() {
        return destinations;
    }

    public List<ModelTravelAgencySearch> getTravel_agencies() {
        return travel_agencies;
    }

    public ModelPriceLimit getPrice_limits() {
        return price_limits;
    }

    public List<ModelFlightClass> getFlight_class() {
        return flight_class;
    }
}
