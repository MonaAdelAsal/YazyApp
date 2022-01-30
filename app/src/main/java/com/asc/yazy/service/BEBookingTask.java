package com.asc.yazy.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.BEBookingAPI;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.PaymentModel;
import com.asc.yazy.interfaces.OnBackThreadListener;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BEBookingTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "BEBookingTask";
    private final PaymentModel paymentModel;
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final OnBackThreadListener threadListener;
    private final int gift;

    @SuppressWarnings("deprecation")
    private BEBookingTask(Context context, PaymentModel paymentModel, OnBackThreadListener threadListener, int gift) {
        this.context = context;
        this.paymentModel = paymentModel;
        this.threadListener = threadListener;
        this.gift = gift;
        paymentModel.setGift(gift);
    }

    public static void book(Context context, PaymentModel paymentModel, OnBackThreadListener threadListener, int gift) {
        new BEBookingTask(context, paymentModel, threadListener, gift).execute();
    }


    private void run(final PaymentModel paymentModel) {


        try {


            ApiInterface mApiService = ApiClient.getInterfaceService();

            ModelUser user = UtilsPreference.getInstance(context).getUser();
            if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
                Log.d(TAG, " not authorized");
                return;
            }
            if (paymentModel.getStatusCode() == null) {
                paymentModel.setStatusCode("");
                paymentModel.setPaymentGateWay("Hesabe");
                paymentModel.setStatusMessage("not paid");
                paymentModel.setChargeID("0");
            }
            Call<BEBookingAPI> call = mApiService.bookingRequest(
                    "Bearer " + user.getAccess_token(), Constants.getLANGUAGE(),
                    user.getName(),
                    paymentModel.getEmail(),
                    paymentModel.getCountryCode(),
                    paymentModel.getPhoneNumber(),
                    paymentModel.getAdults(),
                    paymentModel.getChild(),
                    (int) paymentModel.getAmount(),
                    paymentModel.getCurrency(),
                    paymentModel.getOfferId(),
                    paymentModel.getStatusCode(),
                    paymentModel.getStatusMessage(),
                    paymentModel.getChargeID(),
                    paymentModel.getTravelAgencyId(),
                    paymentModel.getPaymentGateWay(),
                    paymentModel.getAdultList(),
                    paymentModel.getChildList(),
                    paymentModel.getStartDate(), gift,
                    paymentModel.getPromoCodeID()
            );
            call.enqueue(new Callback<BEBookingAPI>() {
                @Override
                public void onResponse(@NonNull Call<BEBookingAPI> call, @NonNull Response<BEBookingAPI> response) {
                    try {
                        System.out.println(TAG + response.code() + " ");
                        System.out.println(TAG + response.toString() + " ");
                        if (gift == 1 || paymentModel.getStartDate() == null) {
                            UtilsPreference.getInstance(context).savePreference(Constants.IS_BOOKINGS_PENDING, true);
                        }
                        if (response.code() == 200 && threadListener != null) {
                            threadListener.onSuccess(response.body().getData().getPayment_link(), response.body().getData().getBooking_id(), gift, response.body().getData().getPoints());
                        } else {

                            try {
                                assert response.errorBody() != null;
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                String message = jObjError.getString("message");
                                if (threadListener != null)
                                    threadListener.onFailure(message);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    } catch (Exception e) {
                        System.out.println(TAG + e.getMessage());
                    }

                }

                @Override
                public void onFailure(@NonNull Call<BEBookingAPI> call, @NonNull Throwable t) {
                    try {
                        System.out.println(TAG + t.getMessage());
                    } catch (Exception ignored) {

                    }
                }
            });

        } catch (Exception e) {
            System.out.println(TAG + e.getMessage());
        }

    }

    @Override
    protected Void doInBackground(Void... voids) {
        run(paymentModel);
        return null;
    }

}

