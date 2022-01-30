package com.asc.yazy.application;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class TravelXApplication extends Application {

    private static FirebaseAnalytics firebaseAnalytics;


    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        FirebaseApp.initializeApp(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        new Instabug.Builder(this, "69bdd7d6ea22322d52d6f1af895186dd")
                .setInvocationEvents(
                        InstabugInvocationEvent.SHAKE)
                .build();

        AppCenter.start(this, "94b3e699-f9e9-4b39-9c9d-7265c1cf41f2", Analytics.class, Crashes.class);
    }

    public FirebaseAnalytics getFireballAnalytics() {
        if (firebaseAnalytics == null) {
            FirebaseApp.initializeApp(this);
            firebaseAnalytics = FirebaseAnalytics.getInstance(this);
            firebaseAnalytics.setAnalyticsCollectionEnabled(true);
            return firebaseAnalytics;
        }
        return firebaseAnalytics;
    }

}
