package com.asc.yazy.core;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelPoint;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.interfaces.OnTaskListener;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemDialog extends Dialog implements View.OnClickListener {

    public static final int GRAY = 0x4F000000;
    private final Dialog Filters;
    private final Context context;
    private final ModelPoint point;
    private final OnTaskListener listener;


    public RedeemDialog(@NonNull Context context, ModelPoint point, OnTaskListener listener) {
        super(context);
        this.context = context;
        this.point = point;
        this.listener = listener;
        Filters = new Dialog(Objects.requireNonNull(context), R.style.DialogStyle);
        setCancelable(true);
        Objects.requireNonNull(Filters.getWindow()).setBackgroundDrawable(new ColorDrawable(GRAY));
        com.asc.yazy.databinding.DialogRedeemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_redeem, null, false);
        Filters.setContentView(binding.getRoot());
        Window window = Filters.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        binding.btnGetIT.setOnClickListener(this);
        binding.btnNo.setOnClickListener(this);
        binding.tvSave.setText((point.getDiscount() + "%"));


    }


    public void show() {
        Filters.show();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        if (v == null || context == null)
            return;
        switch (v.getId()) {
            case R.id.btnGetIT:
                redeemPoints();
                break;
            case R.id.btnNo:
                Filters.dismiss();
                break;

        }
    }


    private void redeemPoints() {

        String token = UtilsPreference.getInstance(context).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(context).startActivity(new Intent(context, AuthenticationActivity.class));
            return;
        }
        PlaneDialog planeDialog = new PlaneDialog(Objects.requireNonNull(context));
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelStatic> call = mApiService.redeemPoint("Bearer " + token, point.getDiscountId(), Constants.getLANGUAGE());
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {

                planeDialog.Dismiss();

                if (response.code() == 401) {
                    Intent intent = new Intent(context, AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    context.startActivity(intent);
                    return;
                }

                if (response.code() != 200) {
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getString(R.string.error_in_server)).show();
                    }

                    return;
                }
                ModelStatic body = response.body();

                if (body == null) {
                    GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }
                if (body.getStatus() == 200) {
                    Toast.makeText(context, body.getMessage(), Toast.LENGTH_SHORT).show();
                    if (listener != null)
                        listener.onSuccess();
                    dismiss();
                    Filters.dismiss();

                } else {
                    Toast.makeText(context, body.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                planeDialog.Dismiss();
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.REDEEM_POINTS);
                //  GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getResources().getString(R.string.cant_connect)).show();
                context.startActivity(new Intent(context, NoNetActivity.class));
            }
        });
    }


}