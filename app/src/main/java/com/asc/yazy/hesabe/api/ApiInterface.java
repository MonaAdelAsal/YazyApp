package com.asc.yazy.hesabe.api;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("/checkout")
    @Headers({"accessCode: " + Constants.ACCESS_CODE})
    Call<String> hesabePay(@Query("data") String data);
}
