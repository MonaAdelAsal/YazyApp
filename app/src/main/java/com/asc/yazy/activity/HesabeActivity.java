package com.asc.yazy.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.airbnb.lottie.LottieDrawable;
import com.asc.yazy.R;
import com.asc.yazy.api.model.TDetailsModel;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.CongratulationDialog;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.PaymentModel;
import com.asc.yazy.databinding.ActivityTapBinding;
import com.asc.yazy.hesabe.api.ApiInterface;
import com.asc.yazy.hesabe.api.Constants;
import com.asc.yazy.hesabe.api.RetrofitClientInstance;
import com.asc.yazy.hesabe.crypto.HesabeCrypt;
import com.asc.yazy.interfaces.OnBackThreadListener;
import com.asc.yazy.service.BEBookingTask;
import com.asc.yazy.service.BEUpdateBookingTask;
import com.asc.yazy.utils.AnalyticsUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;


public class HesabeActivity extends AppCompatActivity implements View.OnClickListener {

    private final boolean flag = false;
    private ActivityTapBinding binding;
    private HesabeCrypt hesabeCrypt;
    private PaymentModel paymentModel;
    private TDetailsModel offer;
    private GuideView caseView;
    private String paymentId;
    private final OnBackThreadListener threadListener = new OnBackThreadListener() {

        @Override
        public void onSuccess(String shareLink, String bookid, int gift, String points) {
            AnalyticsUtility.logEvent(AnalyticsUtility.Events.PAYMENT_SUCCESS, AnalyticsUtility.Events.PAYMENT_SUCCESS);
            updateUiSuccess();
            if (points.equals("0"))
                GlobalInfoDialog.getInstance(HesabeActivity.this).setTitle(getString(R.string.win_points)).setMessage(getString(R.string.win_next)).show();
            else
                CongratulationDialog.getInstance(HesabeActivity.this).setTitle(getString(R.string.Congratulations)).setMessage(points).show();

        }

        @Override
        public void onFailure(String error) {
            AnalyticsUtility.logEvent(AnalyticsUtility.Events.PAYMENT_FAIL, AnalyticsUtility.Events.PAYMENT_SUCCESS);
            updateUiFail(error);
        }
    };
    private final OnBackThreadListener updateListener = new OnBackThreadListener() {

        @Override
        public void onSuccess(String shareLink, String bookid, int gift, String points) {
            AnalyticsUtility.logEvent(AnalyticsUtility.Events.PAYMENT_SUCCESS, AnalyticsUtility.Events.PAYMENT_SUCCESS);
            updateUiSuccess();
            if (points.equals("0"))
                GlobalInfoDialog.getInstance(HesabeActivity.this).setTitle(getString(R.string.win_points)).setMessage(getString(R.string.win_next)).show();
            else
                CongratulationDialog.getInstance(HesabeActivity.this).setTitle(getString(R.string.Congratulations)).setMessage(points).show();

        }

        @Override
        public void onFailure(String error) {
            AnalyticsUtility.logEvent(AnalyticsUtility.Events.PAYMENT_FAIL, AnalyticsUtility.Events.PAYMENT_SUCCESS);
            updateUiFail(error);
        }
    };

    private void introViewBehaviour() {


        boolean firstRun = UtilsPreference.getInstance(this).getPreference(com.asc.yazy.utils.Constants.IS_CONFIRM_BOOK_FIRST_RUN, true);

        if (!firstRun)
            return;


        caseView = new GuideView.Builder(this)
                .setContentText(Objects.requireNonNull(this).getResources().getString(R.string.book_success))
                .setTargetView(binding.layoutBook)
                .setGravity(Gravity.center)//optional
                .setDismissType(DismissType.anywhere)
                .build();

        caseView.dismiss();
        caseView.show();

        UtilsPreference.getInstance(this).savePreference(com.asc.yazy.utils.Constants.IS_CONFIRM_BOOK_FIRST_RUN, false);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (caseView != null && caseView.isShowing()) {
            caseView.dismiss();
            return false;
        }
        finish();
        return false;
    }


    private void updateUiSuccess() {

        binding.webView.setVisibility(View.GONE);
        com.asc.yazy.utils.Constants.changeStatusBarColor(HesabeActivity.this, R.color.blueOld);
        binding.layoutBackGround.setBackgroundColor(getResources().getColor(R.color.blueOld));

        binding.tvConfrim.setVisibility(View.VISIBLE);
        binding.tvConfrim.setText(getResources().getString(R.string.payment_confirmed));
        binding.tvMessage.setVisibility(View.VISIBLE);
        binding.tvMessage.setText(getResources().getString(R.string.your_payment_has_been_confirmed_a_confirmation_email_has_been_sent_to));

        if (paymentModel != null && paymentModel.getEmail() != null) {
            binding.tvEmail.setText(paymentModel.getEmail());
            binding.tvEmail.setVisibility(View.VISIBLE);
        } else if (offer != null && offer.getEmail() != null) {
            binding.tvEmail.setText(offer.getEmail());
            binding.tvEmail.setVisibility(View.VISIBLE);
        }

        binding.tvTransactionId.setVisibility(View.VISIBLE);
        binding.tvTransactionIdValue.setVisibility(View.VISIBLE);

        binding.tvTransactionIdValue.setText(paymentId);

        binding.loadingAnimation.setMinAndMaxFrame(220, 400);
        binding.loadingAnimation.setRepeatCount(0);

        binding.btnHome.setOnClickListener(this);
        binding.btnHome.setVisibility(View.VISIBLE);

        binding.btnTryAgain.setVisibility(View.INVISIBLE);
        introViewBehaviour();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tap);
        CheckLanguage checkLanguage = new CheckLanguage(HesabeActivity.this);
        AnalyticsUtility.setScreen(this);
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.PAYMENT);
        checkLanguage.UpdateLanguage();
        Intent intent = getIntent();
        if (intent.hasExtra(com.asc.yazy.utils.Constants.TRANSACTION) || intent.hasExtra(com.asc.yazy.utils.Constants.TRANSACTION_OFFER)) {
            paymentModel = (PaymentModel) intent.getSerializableExtra(com.asc.yazy.utils.Constants.TRANSACTION);
            offer = (TDetailsModel) intent.getSerializableExtra(com.asc.yazy.utils.Constants.TRANSACTION_OFFER);
            waiting();
            startSDK();
/*

          if (paymentModel != null) {
                paymentModel.setPaymentGateWay(getResources().getString(R.string.hesabe));
                paymentModel.setChargeID("123");
                paymentModel.setStatusCode("200");
                paymentModel.setStatusMessage("message");
                BEBookingTask.book(this, paymentModel, threadListener, 0);
            }

            if (offer != null) {
                paymentId = "10000";
                BEUpdateBookingTask.update(this, String.valueOf(offer.getId()), updateListener, 1, paymentId);
            }

*/
        } else {
            Toast.makeText(this, getResources().getString(R.string.some_thing_went_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void waiting() {
        binding.webView.setVisibility(View.GONE);
        com.asc.yazy.utils.Constants.changeStatusBarColor(HesabeActivity.this, R.color.blueOld);
        binding.layoutBackGround.setBackgroundColor(getResources().getColor(R.color.blueOld));
        binding.tvConfrim.setVisibility(View.VISIBLE);
        binding.tvConfrim.setText(getResources().getString(R.string.processing));
        binding.tvMessage.setVisibility(View.INVISIBLE);
        binding.tvTransactionId.setVisibility(View.INVISIBLE);
        binding.tvTransactionIdValue.setVisibility(View.INVISIBLE);
        binding.btnTryAgain.setVisibility(View.INVISIBLE);
        binding.loadingAnimation.setMinAndMaxFrame(0, 120);
        binding.loadingAnimation.playAnimation();
        binding.loadingAnimation.setRepeatMode(LottieDrawable.RESTART);
        binding.loadingAnimation.setRepeatCount(1000);
    }

    private void startSDK() {
        hesabeCrypt = new HesabeCrypt(com.asc.yazy.hesabe.api.Constants.SECRET_KEY, com.asc.yazy.hesabe.api.Constants.IV_KEY);
        checkout();
    }

    private void checkout() {
        AnalyticsUtility.logEvent(AnalyticsUtility.Events.PAYMENT_CHECKOUT, AnalyticsUtility.Events.PAYMENT_CHECKOUT);
        JSONObject obj = getPaymentRequestObject();
        String encryptedData = hesabeCrypt.encrypt(obj.toString());
        checkoutRequest(encryptedData);
    }

    private JSONObject getPaymentRequestObject() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("merchantCode", Constants.MERCHANT_CODE); //Use merchant code
            if (paymentModel != null && offer == null)
                obj.put("amount", paymentModel.getAmount()); //Total amount
            else if (paymentModel == null && offer != null)
                obj.put("amount", offer.getTotal()); //Total amount

            obj.put("paymentType", "0"); //Type of the payment
            obj.put("responseUrl", "http://my.site.com/result/"); //Given URL end point will be used to check the result
            obj.put("failureUrl", "http://my.site.com/result/");
            obj.put("version", "2.0"); //Hesabe Payment Gateway version
            obj.put("variable1", "#OR12345"); //Order ID or any other variable which will get back after payment completes.
            obj.put("variable2", "");
            obj.put("variable3", "");
            obj.put("variable4", "");
            obj.put("variable5", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    private void checkoutRequest(String encryptedData) {
        ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<String> call = service.hesabePay(encryptedData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.body() != null) {
                    try {
                        AnalyticsUtility.logEventAPISuccess(AnalyticsUtility.Events.PAYMENT);
                        processResponse(response.body());

                    } catch (Exception e) {
                        e.printStackTrace();
                        updateUiFail(HesabeActivity.this.getResources().getString(R.string.coud_not_comunicate));
                    }
                } else {
                    updateUiFail(HesabeActivity.this.getResources().getString(R.string.coud_not_comunicate));
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                AnalyticsUtility.logEventAPIFail(AnalyticsUtility.Events.PAYMENT);
                updateUiFail(HesabeActivity.this.getResources().getString(R.string.coud_not_comunicate));
            }
        });
    }

    private void processResponse(String response) {
        try {

            byte[] decryptedResponse = hesabeCrypt.decrypt(response);
            String trimmedData = new String(decryptedResponse).replaceAll("\\s+", " ").trim();
            String responseToken = new JSONObject(trimmedData).getJSONObject("response").getString("data");
            String paymentURL = Constants.BASE_URL.concat("/payment?data=").concat(responseToken);
            redirectToPayment(paymentURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void redirectToPayment(String url) {
        AnalyticsUtility.logEvent(AnalyticsUtility.Events.PAYMENT_REDIRECT_TO_PAYMENT, AnalyticsUtility.Events.PAYMENT_REDIRECT_TO_PAYMENT);
        if (url != null && !url.isEmpty()) {
            binding.webView.setVisibility(View.VISIBLE);
            binding.webView.loadUrl(url);
            WebSettings webSettings = binding.webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            binding.webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                    if (!flag) {
                        view.loadUrl(url);
                        return true;
                    }
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    parseResult(url);
                    /*

                    if (url.contains("http://my.site.com/result/")) {
                        flag = true;

                      parseResult(url);
                    }
                    */
                }
            });
        }
    }

    void updateUiFail(String message) {

        if (caseView != null && caseView.isShowing())
            caseView.dismiss();

        com.asc.yazy.utils.Constants.changeStatusBarColor(HesabeActivity.this, R.color.red);
        binding.layoutBackGround.setBackgroundColor(getResources().getColor(R.color.red));
        binding.tvConfrim.setVisibility(View.VISIBLE);
        binding.tvMessage.setVisibility(View.VISIBLE);
        binding.tvTransactionId.setVisibility(View.INVISIBLE);
        binding.tvTransactionIdValue.setVisibility(View.INVISIBLE);

        binding.tvConfrim.setText(getResources().getString(R.string.error));
        binding.tvMessage.setText(message);
        binding.tvEmail.setVisibility(View.INVISIBLE);

        binding.loadingAnimation.setMinAndMaxFrame(657, 823);
        binding.loadingAnimation.setRepeatCount(0);

        binding.btnTryAgain.setOnClickListener(this);
        binding.btnTryAgain.setVisibility(View.VISIBLE);

        binding.btnHome.setVisibility(View.VISIBLE);
        binding.btnHome.setOnClickListener(this);


    }


    private void parseResult(String url) {
        Uri parse = Uri.parse(url);
        String data = parse.getQueryParameter("data");
        try {
            /* Decrypt the result */
            byte[] decrypt = hesabeCrypt.decrypt(data);
            String decryptedData = new String(decrypt).replaceAll("\\s+", " ").trim();

            /* Get the decrypted data as a JSONObject */
            JSONObject codeParent = new JSONObject(decryptedData);
            JSONObject decryptedObject = new JSONObject(decryptedData).getJSONObject("response");


            int code = codeParent.getInt("code");
            String message = codeParent.getString("message");
            String resultCode = decryptedObject.getString("resultCode");
            paymentId = decryptedObject.getString("paymentId");

            if (code == 1) {
                if (paymentModel != null) {
                    paymentModel.setPaymentGateWay(getResources().getString(R.string.hesabe));
                    paymentModel.setChargeID(paymentId);
                    paymentModel.setStatusCode("200");
                    paymentModel.setStatusMessage(message);
                    Intent intent = getIntent();
                    if (intent.hasExtra("BookingID")) {
                        BEUpdateBookingTask.update(this, Objects.requireNonNull(getIntent().getExtras()).getString("BookingID"), updateListener, code, paymentId);
                    } else {
                        BEBookingTask.book(this, paymentModel, threadListener, 0);
                    }

                } else if (offer != null) {
                    BEUpdateBookingTask.update(this, String.valueOf(offer.getId()), updateListener, code, paymentId);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.btnTryAgain:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.PAYMENT_TRY_AGAIN);
                waiting();
                startSDK();
                break;
            case R.id.btnHome:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_HOME);
                startActivity(new Intent(this, com.asc.yazy.activity.MainActivity.class));
                finish();
                break;
        }

    }


}
