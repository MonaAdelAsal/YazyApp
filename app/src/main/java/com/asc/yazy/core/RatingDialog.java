package com.asc.yazy.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.airbnb.lottie.LottieDrawable;
import com.asc.yazy.R;
import com.asc.yazy.activity.AuthenticationActivity;
import com.asc.yazy.activity.NoNetActivity;
import com.asc.yazy.api.ApiClient;
import com.asc.yazy.api.ApiInterface;
import com.asc.yazy.api.model.ModelStatic;
import com.asc.yazy.api.pagination.offers.NetworkState;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.databinding.RatingDialogBinding;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingDialog extends Dialog {

    private static boolean isAlive = false;
    private final Context context;
    private int notificationID;
    private RatingDialogBinding binding;


    private RatingDialog(@NonNull Context context) {
        super(context, R.style.DialogSlideAnim);
        this.context = context;
        if (isAlive) {
            dismiss();
        }
    }

    @NonNull
    public static RatingDialog getInstance(Context context) {
        return new RatingDialog(context);
    }

    public RatingDialog setNotificationID(int notificationID) {
        this.notificationID = notificationID;
        return this;
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.rating_dialog, null, false);
        setContentView(binding.getRoot());
        binding.TvCancel.setOnClickListener(v -> dismiss());
        binding.TvSubmit.setOnClickListener(v -> {
            waiting();
            InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            assert keyboard != null;
            keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
            AnalyticsUtility.logAction(AnalyticsUtility.Actions.RATE);
            rate(notificationID, binding.rating.getNumStars() + "", binding.etReview.getText().toString());
        });
    }


    private void updateUiSuccess() {
        setCancelable(false);
        binding.layoutBackGround.setVisibility(View.VISIBLE);
        binding.loadingAnimation.setMinAndMaxFrame(220, 400);
        binding.loadingAnimation.setRepeatCount(0);
        binding.loadingAnimation.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                dismiss();
            }
        });
    }

    private void waiting() {
        setCancelable(false);
        binding.layoutBackGround.setVisibility(View.VISIBLE);
        binding.loadingAnimation.setMinAndMaxFrame(0, 120);
        binding.loadingAnimation.playAnimation();
        binding.loadingAnimation.setRepeatMode(LottieDrawable.RESTART);
        binding.loadingAnimation.setRepeatCount(1000);
    }

    private void rate(int NotificationID, String Stars, String Comment) {
        String token = UtilsPreference.getInstance(context).getUser().getAccess_token();
        if (token == null || token.isEmpty()) {
            Objects.requireNonNull(context).startActivity(new Intent(context, AuthenticationActivity.class));
            return;
        }
        ApiInterface mApiService = ApiClient.getInterfaceService();
        Call<ModelStatic> call = mApiService.SendReview("Bearer " + token, Constants.getLANGUAGE(), NotificationID, Stars, Comment);
        call.enqueue(new Callback<ModelStatic>() {
            @Override
            public void onResponse(@NonNull Call<ModelStatic> call, @NonNull Response<ModelStatic> response) {

                if (context == null)
                    return;

                if (response.code() == 401) {
                    Intent intent = new Intent(context, AuthenticationActivity.class);
                    intent.putExtra(Constants.TRANSACTION, NetworkState.UN_AUTHORISE);
                    context.startActivity(intent);
                    binding.layoutBackGround.setVisibility(View.GONE);
                    return;
                }
                if (response.code() != 200) {
                    binding.layoutBackGround.setVisibility(View.GONE);
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

                ModelStatic body = response.body();

                if (body == null) {
                    binding.layoutBackGround.setVisibility(View.GONE);
                    GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getString(R.string.please_check_your_internet_connection)).show();
                    return;
                }

                if (body.getStatus() == 200) {
                    AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.RATE);
                    //Toast.makeText(getContext(), body.getMessage(), Toast.LENGTH_SHORT).show();
                    updateUiSuccess();
                } else {
                    binding.layoutBackGround.setVisibility(View.GONE);
                    GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(body.getMessage()).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ModelStatic> call, @NonNull Throwable t) {

                if (context == null)
                    return;

                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.RATE);
                // GlobalInfoDialog.getInstance(context).setTitle(context.getString(R.string.error)).setMessage(context.getResources().getString(R.string.cant_connect)).show();

                binding.layoutBackGround.setVisibility(View.GONE);
                context.startActivity(new Intent(context, NoNetActivity.class));
            }
        });
    }

}