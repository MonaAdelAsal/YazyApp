package com.asc.yazy.core;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelCancel;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.databinding.CancelDialogBinding;
import com.asc.yazy.fragment.MyTripsPagerFragment;
import com.asc.yazy.interfaces.OnCancelCompleted;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelDialog extends Dialog {

    private static boolean isAlive = false;
    private final Context context;
    private final String bookingID;
    private final OnCancelCompleted onCancelCompleted;


    private CancelDialog(@NonNull Context context, String bookingID, OnCancelCompleted onCancelCompleted) {
        super(context, R.style.DialogSlideAnim);
        this.context = context;
        this.bookingID = bookingID;
        this.onCancelCompleted = onCancelCompleted;
        if (isAlive) {
            dismiss();
        }
    }

    @NonNull
    public static CancelDialog getInstance(Context context, String bookingID, OnCancelCompleted onCancelCompleted) {
        return new CancelDialog(context, bookingID, onCancelCompleted);
    }


    @Override
    public void show() {
        super.show();
        if (isAlive) {
            dismiss();
        } else {
            isAlive = true;
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
        isAlive = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckLanguage checkLanguage = new CheckLanguage(context);
        checkLanguage.UpdateLanguage();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        CancelDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cancel_dialog, null, false);
        setContentView(binding.getRoot());
        binding.tvCancel.setOnClickListener(v -> dismiss());
        binding.tvConfirm.setOnClickListener(v -> cancel(bookingID));
    }

    private void cancel(String bookingID) {
        String token = UtilsPreference.getInstance(context).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(context).startActivity(new Intent(context, AuthenticationActivity.class));
            return;
        }
        PlaneDialog dialog = new PlaneDialog(context);
        dialog.show();
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelCancel> call = mApiService.cancelBooking("Bearer " + token, Constants.getLANGUAGE(), bookingID);
        call.enqueue(new Callback<ModelCancel>() {
            @Override
            public void onResponse(@NonNull Call<ModelCancel> call, @NonNull Response<ModelCancel> response) {

                dialog.Dismiss();

                if (response.code() == 401) {
                    Intent intent = new Intent(context, AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    context.startActivity(intent);
                    // binding.layoutBackGround.setVisibility(View.GONE);
                    return;
                }
                if (response.code() != 200) {
                    //  binding.layoutBackGround.setVisibility(View.GONE);
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(message).show();
                        return;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                ModelCancel body = response.body();

                if (body == null) {
                    //   binding.layoutBackGround.setVisibility(View.GONE);
                    GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200) {

                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.RATE);
                    Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                    dismiss();
                    if (MyTripsPagerFragment.basketViewPagerAdapter != null)
                        MyTripsPagerFragment.basketViewPagerAdapter.notifyDataSetChanged();
                    if (onCancelCompleted != null)
                        onCancelCompleted.nnCancelCompleted();
                    //       updateUiSuccess();
                } else {
                    //      binding.layoutBackGround.setVisibility(View.GONE);
                    GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(body.getMessage()).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ModelCancel> call, @NonNull Throwable t) {

                dialog.Dismiss();

                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.RATE);
                // GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getResources().getString(R.string.cant_connect)).show();
                context.startActivity(new Intent(context, NoNetActivity.class));
                //      binding.layoutBackGround.setVisibility(View.GONE);

            }
        });
    }

}