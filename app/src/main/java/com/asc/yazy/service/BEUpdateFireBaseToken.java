package com.asc.yazy.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BEUpdateFireBaseToken extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "FIRE_BASE_BACKEND";
    private final String token;
    @SuppressLint("StaticFieldLeak")
    private final Context context;

    @SuppressWarnings("deprecation")
    private BEUpdateFireBaseToken(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    static void updateServerToken(Context context, String token) {
        new BEUpdateFireBaseToken(context, token).execute();
    }

    private void updatedStatus(final String FireBaseToken) {


        try {


            ModelUser user = UtilsPreference.getInstance(context).getUser();
            if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty())
                return;

            if (FireBaseToken == null || FireBaseToken.isEmpty() || FireBaseToken.length() < 30)
                return;

            Log.d(TAG, "updatedStatus: fire base token" + FireBaseToken);

            ApiInterface mApiService = ApiClient.getInterfaceService();


            Call<ModelStatic> call = mApiService.updateFireBaseToken("Bearer " + user.getAccess_token(), Constants.getLANGUAGE(), FireBaseToken, "0");
            call.enqueue(new Callback<ModelStatic>() {
                @Override
                public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {
/*
                    if (response.code() == 401) {
                        Intent intent = new Intent(context, AuthenticationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                        context.startActivity(intent);
                        return;
                    }
*/

                    try {
                        Log.d(TAG, "onResponse: " + response.code() + " " + FireBaseToken);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {
                    try {
                        Log.d(TAG, "onFailure: " + t.getMessage());
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
        updatedStatus(token);
        return null;
    }

}

