package com.asc.yazy.core;

import android.content.Context;
import android.content.res.Configuration;

import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.utils.Constants;

import java.util.Locale;
import java.util.Objects;

public class CheckLanguage {

    private final Context context;

    public CheckLanguage(Context context) {
        this.context = context;

    }

    public void UpdateLanguage() {

        if (UtilsPreference.getInstance(context).getPreference(Constants.LANGUAGE, "ar").equals("en")) {
            Locale locale = new Locale("en");
            Constants.setLanguage("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            Objects.requireNonNull(context).getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        } else {
            Locale locale = new Locale("ar");
            Constants.setLanguage("ar");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            Objects.requireNonNull(context).getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }
}
