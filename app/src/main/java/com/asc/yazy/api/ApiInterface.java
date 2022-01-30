package com.asc.yazy.api;

import com.asc.yazy.api.model.BEBookingAPI;
import com.asc.yazy.api.model.MYModelPointsAPI;
import com.asc.yazy.api.model.ModelAuthenticate;
import com.asc.yazy.api.model.ModelBookingAPI;
import com.asc.yazy.api.model.ModelCancel;
import com.asc.yazy.api.model.ModelCommentAPI;
import com.asc.yazy.api.model.ModelContactApi;
import com.asc.yazy.api.model.ModelContinentAPI;
import com.asc.yazy.api.model.ModelCountryAPi;
import com.asc.yazy.api.model.ModelDetailsAPi;
import com.asc.yazy.api.model.ModelFAQsApi;
import com.asc.yazy.api.model.ModelFavorite;
import com.asc.yazy.api.model.ModelFavoriteAPI;
import com.asc.yazy.api.model.ModelFlightStatus;
import com.asc.yazy.api.model.ModelNotificationAPI;
import com.asc.yazy.api.model.ModelOfferAPI;
import com.asc.yazy.api.model.ModelPointsAPI;
import com.asc.yazy.api.model.ModelPromoCodeAPI;
import com.asc.yazy.api.model.ModelPromotedOfferAPI;
import com.asc.yazy.api.model.ModelSearchDataAPI;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.model.ModelTravellerAPI;
import com.asc.yazy.api.model.ModelTripDetailAPI;
import com.asc.yazy.api.model.ModelUpdateBookingAPI;
import com.asc.yazy.api.model.ModelVerify;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("get-offers")
    Call<ModelOfferAPI> getOffers(@Header("Authorization") String Authorization, @Header("Content-Language") String Language, @Query("page") int pageNum);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("get-offers-with-promoted")
    Call<ModelOfferAPI> getOffersNoPagination(@Header("Authorization") String Authorization, @Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("get-continent-offers")
    Call<ModelContinentAPI> getOffersContinent(@Header("Authorization") String Authorization, @Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("get-offers")
    Call<ModelOfferAPI> getSearchResult(
            @Header("Authorization") String Authorization,
            @Header("Content-Language") String Language,
            @Query("page") int page,
            @Query("travel_agency_id") String travel_agency_id,
            @Query("destination") String city_id,
            @Query("date") String date,
            @Query("date_to") String dateTo,
            @Query("flight_class") String flight_class,
            @Query("max_price") String max_price,
            @Query("sort") String sort,
            @Query("type") String type,
            @Query("duration") String duration,
            @Query("accommodation_service") String accommodation_service,
            @Query("continent_id") String continent_id);


    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("offer-details/{id}")
    Call<ModelDetailsAPi> getDetails(@Header("Authorization") String Authorization, @Header("Content-Language") String Language, @Path(value = "id", encoded = true) String id);

    //----------------------------------------------------------------------------------------------------
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("search-info")
    Call<ModelSearchDataAPI> getSearchData(@Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("favorite")
    Call<ModelFavorite> favorite(@Header("Authorization") String Authorization, @Header("Content-Language") String Language, @Query("offer_id") String offer_id, @Query("flag") int flag);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("static/about-us")
    Call<ModelStatic> aboutUs(@Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("static/contact-us")
    Call<ModelContactApi> contactUs(@Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("static/privacy-policy")
    Call<ModelStatic> privacyPolicy(@Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("static/terms-conditions")
    Call<ModelStatic> termsAndConditions(@Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("add-booking-request")
    Call<BEBookingAPI> bookingRequest(
            @Header("Authorization") String Authorization,
            @Header("Content-Language") String Language,
            @Query("name") String name,
            @Query("email") String email,
            @Query("country_code") String country_code,
            @Query("mobile") String mobile,
            @Query("adults") int adults,
            @Query("children") int children,
            @Query("total") int amount,
            @Query("currency") String currency,
            @Query("offer_id") String offer_id,
            @Query("transaction_status_code") String transaction_status_code,
            @Query("transaction_error_message") String transaction_error_message,
            @Query("transaction_id") String transaction_id,
            @Query("travel_agency_id") String travel_agency_id,
            @Query("payment_gateway") String payment_gateway,
            @Query("adult_passenger") String adult_passenger,
            @Query("child_passenger") String child_passenger,
            @Query("start_date") String start_date,
            @Query("gift") int gift,
            @Query("promocode_id") String promocode_id
    );

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("get-country")
    Call<ModelCountryAPi> getCountries(@Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("mobile/login")
    Call<ModelAuthenticate> login(@Header("Content-Language") String Language, @Query("country_code") String countryCode, @Query("mobile") String mobile, @Query("password") String password);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("mobile/register")
    Call<ModelAuthenticate> register(@Header("Content-Language") String Language, @Query("name") String name,
                                     @Query("country_code") String countryCode,
                                     @Query("mobile") String mobile,
                                     @Query("password") String password,
                                     @Query("email") String email);
    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("mobile/verify_mobile_number")
    Call<ModelVerify> verify(@Header("Content-Language") String Language, @Query("country_code") String countryCode, @Query("mobile") String mobile);


    //----------------------------------------------------------------------------------------------------
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("mobile/verify_mobile_forget_password")
    Call<ModelVerify> verifyForgetPassword(@Header("Content-Language") String Language, @Query("country_code") String countryCode, @Query("mobile") String mobile);


    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("list-favourite")
    Call<ModelFavoriteAPI> getFavorite(@Header("Authorization") String Authorization, @Header("Content-Language") String Language, @Query("page") int page);


    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("list-booking")
    Call<ModelBookingAPI> getBooking(@Header("Authorization") String Authorization, @Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("forgot-password")
    Call<ModelStatic> change(@Header("Content-Language") String Language, @Query("country_code") String countryCode,
                             @Query("mobile") String mobile,
                             @Query("new_password") String password);

    //----------------------------------------------------------------------------------------------------


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("change-password")
    Call<ModelStatic> changePassword(@Header("Authorization") String Authorization, @Header("Content-Language") String Language,
                                     @Query("old_password") String old_password,
                                     @Query("new_password") String new_password);

    //----------------------------------------------------------------------------------------------------
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("update-profile")
    Call<ModelAuthenticate> updateProfile(@Header("Authorization") String Authorization, @Header("Content-Language") String Language,
                                          @Query("name") String name,
                                          @Query("email") String email,
                                          @Query("nationality") String nationality,
                                          @Query("passport_no") String passport_no,
                                          @Query("civil_id") String civil_id,
                                          @Query("passport_expiry") String passport_expiry);

    //----------------------------------------------------------------------------------------------------


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("booking-details")
    Call<ModelTripDetailAPI> tripDetails(@Header("Authorization") String Authorization, @Header("Content-Language") String Language, @Query("booking_id") String bookingID);

    //----------------------------------------------------------------------------------------------------


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("logout")
    Call<ModelStatic> logout(@Header("Authorization") String Authorization, @Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("notification-clear")
    Call<ModelStatic> clearNotification(@Header("Authorization") String Authorization, @Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("notification-list")
    Call<ModelNotificationAPI> getNotificationsUser(@Header("Authorization") String Authorization, @Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("notification-list")
    Call<ModelNotificationAPI> getNotificationsGuest(@Query("install_date") String installationDate, @Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("notification-setting")
    Call<ModelAuthenticate> NotificationSettings(@Header("Authorization") String Authorization, @Header("Content-Language") String Language,
                                                 @Query("enable_notification") String enable_notification);

    //----------------------------------------------------------------------------------------------------


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("update-firebase")
    Call<ModelStatic> updateFireBaseToken(@Header("Authorization") String Authorization, @Header("Content-Language") String Language, @Query("firebase_token") String firebase_token, @Query("device_type") String Device);

    //----------------------------------------------------------------------------------------------------
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("add-frequent-traveller")
    Call<ModelStatic> addTraveller(@Header("Authorization") String Authorization, @Header("Content-Language") String Language,
                                   @Query("type") int type,
                                   @Query("name") String name,
                                   @Query("nationality") String nationality,
                                   @Query("passport_no") String passport_no,
                                   @Query("civil_id") String civil_id,
                                   @Query("passport_expiry") String passport_expiry,
                                   @Query("birth_date") String birth_date);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("update-frequent-traveller/{id}")
    Call<ModelStatic> updateTraveller(@Header("Authorization") String Authorization, @Header("Content-Language") String Language,
                                      @Path("id") String id,
                                      @Query("type") int type,
                                      @Query("name") String name,
                                      @Query("nationality") String nationality,
                                      @Query("passport_no") String passport_no,
                                      @Query("civil_id") String civil_id,
                                      @Query("passport_expiry") String passport_expiry,
                                      @Query("birth_date") String birth_date);

    //----------------------------------------------------------------------------------------------------


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("list-frequent-travellers")
    Call<ModelTravellerAPI> getTravellers(@Header("Authorization") String Authorization, @Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("promoted-offers")
    Call<ModelPromotedOfferAPI> getPromotedOffers(@Header("Authorization") String Authorization, @Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("delete-frequent-travellers")
    Call<ModelStatic> deleteTraveller(@Header("Authorization") String Authorization, @Header("Content-Language") String Language, @Query("id") String id);

    //----------------------------------------------------------------------------------------------------


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("list-rate/{id}")
    Call<ModelCommentAPI> getComments(@Header("Authorization") String Authorization, @Header("Content-Language") String Language, @Path("id") String id);
    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("add-rate")
    Call<ModelStatic> SendReview(@Header("Authorization") String Authorization, @Header("Content-Language") String Language,
                                 @Query("booking_id") int notification_id,
                                 @Query("rate") String rate,
                                 @Query("comment") String comment);
    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("update-booking")
    Call<ModelUpdateBookingAPI> SendDateFrom(@Header("Authorization") String Authorization, @Header("Content-Language") String Language,
                                             @Query("booking_id") String booking_id,
                                             @Query("start_date") String start_date,
                                             @Query("reschedule") int reschedule);


    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("payment-booking")
    Call<ModelUpdateBookingAPI> completeBook(@Header("Authorization") String Authorization, @Header("Content-Language") String Language,
                                             @Query("booking_id") String booking_id,
                                             @Query("transaction_status_code") String transaction_status_code,
                                             @Query("transaction_error_message") String transaction_error_message,
                                             @Query("transaction_id") String transaction_id,
                                             @Query("payment_gateway") String payment_gateway,
                                             @Query("gift") int gift);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("cancel-booking")
    Call<ModelCancel> cancelBooking(@Header("Authorization") String Authorization, @Header("Content-Language") String Language, @Query("booking_id") String booking_id);

    //----------------------------------------------------------------------------------------------------


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("static/cancellation-policy")
    Call<ModelStatic> cancellationPolicy(@Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("/api/flight-status")
    Call<ModelFlightStatus> GetFlightStatus(@Header("Authorization") String Authorization, @Header("Content-Language") String Language);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("notification-read")
    Call<ModelStatic> SetNotificationSeen(@Header("Authorization") String Authorization, @Header("Content-Language") String Language, @Query("notificationID") String id);

    //----------------------------------------------------------------------------------------------------
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("my-questions")
    Call<ModelStatic> SendQuestion(@Header("Authorization") String Authorization, @Header("Content-Language") String Language,
                                   @Query("offer_id") String offer_id,
                                   @Query("email") String email,
                                   @Query("question") String question);
    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("FAQ-list")
    Call<ModelFAQsApi> GetFAQs(@Header("Content-Language") String Language);
    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("check-promocode")
    Call<ModelPromoCodeAPI> checkPromoCode(@Header("Authorization") String Authorization, @Header("Content-Language") String Language, @Query("promocode") String promocode);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("all-points")
    Call<ModelPointsAPI> getPoints(@Header("Authorization") String Authorization);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("my-points")
    Call<MYModelPointsAPI> myPoint(@Header("Authorization") String Authorization);

    //----------------------------------------------------------------------------------------------------

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("redeem-points")
    Call<ModelStatic> redeemPoint(@Header("Authorization") String Authorization, @Query("point_id") String point_id, @Header("Content-Language") String Language);
    //----------------------------------------------------------------------------------------------------


}
