package com.asc.yazy.activity;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.asc.yazy.R;
import com.asc.yazy.api.ApiConstants;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.asc.yazy.utils.UtilsFireBase;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.changeStatusBarColor(this, R.color.colorAccent);
        initFireBase();
        CheckLanguage checkLanguage = new CheckLanguage(SplashActivity.this);
        checkLanguage.UpdateLanguage();
        AnalyticsUtility.logEventOpen(AnalyticsUtility.Events.SPLASH);
        AnalyticsUtility.setScreen(this);
        Intent intent = getIntent();
        if (intent.getData() != null) {
            Constants.id = intent.getData().toString().split("=")[1];
        }
        addShortcut();
        Intent intentHome = new Intent(SplashActivity.this, MainActivity.class);
        intentHome.setAction(getIntent().getAction());
        startActivity(intentHome);
        finish();
    }

    private void addShortcut() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ModelUser registeredUser = UtilsPreference.getInstance(SplashActivity.this).getUser();
            if (registeredUser != null && registeredUser.getAccess_token() != null && !registeredUser.getAccess_token().isEmpty()) {
                ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
                Intent intentSearch = new Intent(Constants.SEARCH_ACTION);
                intentSearch.setClassName(getPackageName(), Constants.QUICK_ACCESS_CLASS_NAME);
                ShortcutInfo shortcutSearch = new ShortcutInfo.Builder(this, "SEARCH")
                        .setShortLabel(getResources().getString(R.string.search))
                        .setLongLabel((getResources().getString(R.string.search)))
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_search))
                        .setIntent(intentSearch)
                        .build();
                Intent intentTrips = new Intent(Constants.TRIPS_ACTION);
                intentTrips.setClassName(getPackageName(), Constants.QUICK_ACCESS_CLASS_NAME);

                ShortcutInfo shortcutTrip = new ShortcutInfo.Builder(this, "MYTRIPS")
                        .setShortLabel(getResources().getString(R.string.my_trips))
                        .setLongLabel((getResources().getString(R.string.my_trips)))
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_my_trip))
                        .setIntent(intentTrips)
                        .build();

                Intent intentFav = new Intent(Constants.FAV_ACTION);
                intentFav.setClassName(getPackageName(), "com.asc.yazy.activity.SplashActivity");

                ShortcutInfo shortcutFav = new ShortcutInfo.Builder(this, "MYFAV")
                        .setShortLabel(getResources().getString(R.string.my_favorites))
                        .setLongLabel((getResources().getString(R.string.my_favorites)))
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_favorite))
                        .setIntent(intentFav)
                        .build();

                if (shortcutManager != null) {
                    List<ShortcutInfo> shortcutInfoList = new ArrayList<>();
                    shortcutInfoList.add(shortcutTrip);
                    shortcutInfoList.add(shortcutFav);
                    shortcutInfoList.add(shortcutSearch);
                    shortcutManager.setDynamicShortcuts(shortcutInfoList);
                }
            } else {
                ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
                if (shortcutManager != null)
                    shortcutManager.removeAllDynamicShortcuts();
                Intent intentSearch = new Intent(Constants.SEARCH_ACTION);
                intentSearch.setClassName(getPackageName(), Constants.QUICK_ACCESS_CLASS_NAME);
                ShortcutInfo shortcutSearch = new ShortcutInfo.Builder(this, "SEARCH")
                        .setShortLabel(getResources().getString(R.string.search))
                        .setLongLabel((getResources().getString(R.string.search)))
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_search))
                        .setIntent(intentSearch)
                        .build();

                if (shortcutManager != null)
                    shortcutManager.setDynamicShortcuts(Collections.singletonList(shortcutSearch));
            }
        }
    }
    private void initFireBase() {
        FirebaseApp.initializeApp(this);
        UtilsPreference.getInstance(this).savePreference(Constants.ENVIRONMENT, Constants.TESTING);
        UtilsFireBase.unSubscribe(Constants.ANDROID_DEVELOPMENT);
        UtilsFireBase.unSubscribe(Constants.ANDROID_TESTING);
        UtilsFireBase.unSubscribe(Constants.ANDROID_LIVE);
        switch (UtilsPreference.getInstance(this).getPreference(Constants.ENVIRONMENT, Constants.TESTING)) {
            case Constants.DEVELOPMENT:
                UtilsFireBase.subscribe(Constants.ANDROID_DEVELOPMENT);
                ApiConstants.BASE_URL = ApiConstants.DEVELOPMENT_URL;
                break;
            case Constants.TESTING:
                UtilsFireBase.subscribe(Constants.ANDROID_TESTING);
                ApiConstants.BASE_URL = ApiConstants.TESTING_URL;
                break;
            case Constants.LIVE:
                UtilsFireBase.subscribe(Constants.ANDROID_LIVE);
                ApiConstants.BASE_URL = ApiConstants.LIVE_URL;
                break;
        }
    }
}