package com.asc.yazy.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavoriteModelDataList {

    @SerializedName("meta")
    private metaModel meta;

    @SerializedName("data")
    private List<ModelFav> data_list;

    public List<ModelFav> getData_list() {
        return data_list;
    }
}
