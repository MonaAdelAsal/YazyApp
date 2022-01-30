package com.asc.yazy.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.asc.yazy.R;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelUpdateBookingAPI;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.interfaces.OnBackThreadListener;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BEUpdateBookingTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "BEUpdateBookingTask";
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final OnBackThreadListener threadListener;
    private final int code;
    private final String paymentId;
    private final String bookId;

    @SuppressWarnings("deprecation")
    private BEUpdateBookingTask(Context context, String BookId, OnBackThreadListener threadListener, int code, String paymentId) {
        this.context = context;
        this.bookId = BookId;
        this.threadListener = threadListener;
        this.code = code;
        this.paymentId = paymentId;

    }

    public static void update(Context context, String BookId, OnBackThreadListener threadListener, int code, String paymentId) {
        new BEUpdateBookingTask(context, BookId, threadListener, code, paymentId).execute();
    }


    private void run(final String BookId) {

        try {


            ApiInterface mApiService = ApiClient.getInterfaceService();

            ModelUser user = UtilsPreference.getInstance(context).getUser();
            if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty()) {
                Log.d(TAG, " not authorized");
                return;
            }


            Call<ModelUpdateBookingAPI> call = mApiService.completeBook(
                    "Bearer " + user.getAccess_token(), Constants.getLANGUAGE(),
                    BookId,
                    getTransactionCode(),
                    getTransactionErrorMessage(),
                    paymentId,
                    context.getResources().getString(R.string.hesabe),
                    0
            );
            call.enqueue(new Callback<ModelUpdateBookingAPI>() {
                @Override
                public void onResponse(@NonNull Call<ModelUpdateBookingAPI> call, @NonNull Response<ModelUpdateBookingAPI> response) {
                    try {
                        assert response.body() != null;
                        System.out.println(TAG + response.code() + " ");
                        System.out.println(TAG + response.toString() + " ");

                        if (response.code() == 200 && threadListener != null) {
                            threadListener.onSuccess(null, null, -1, response.body().getData().getPoints());
                            //Toast.makeText(context,response.body().getData().getPoints(),Toast.LENGTH_LONG).show();
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
                public void onFailure(@NonNull Call<ModelUpdateBookingAPI> call, @NonNull Throwable t) {
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

    private String getTransactionErrorMessage() {
        if (code == 1)
            return "Success";
        else
            return "Fail";
    }

    private String getTransactionCode() {
        if (code == 1)
            return "200";
        else
            return "400";
    }

    @Override
    protected Void doInBackground(Void... voids) {
        run(bookId);
        return null;
    }

}

