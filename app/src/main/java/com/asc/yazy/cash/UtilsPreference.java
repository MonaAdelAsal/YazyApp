package com.asc.yazy.cash;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.asc.yazy.api.model.ModelCountry;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.utils.Constants;

public class UtilsPreference {

    private SharedPreferences sharedPreferences;

    private UtilsPreference(Context context) {
        if (context != null)
            sharedPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), MODE_PRIVATE);
    }

    @NonNull
    public static UtilsPreference getInstance(Context context) {
        return new UtilsPreference(context);
    }

    public void savePreference(String key, String value) {
        if (sharedPreferences != null)
            sharedPreferences.edit().putString(key, value).apply();
    }

    public void savePreference(String key, int value) {
        if (sharedPreferences != null)
            sharedPreferences.edit().putInt(key, value).apply();
    }

    public void savePreference(String key, boolean value) {
        if (sharedPreferences != null)
            sharedPreferences.edit().putBoolean(key, value).apply();
    }

    // get Data ..
    public String getPreference(String key, String defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, defaultValue);
        } else
            return defaultValue;
    }

    public int getPreference(String key, int defaultValue) {
        if (sharedPreferences != null)
            return sharedPreferences.getInt(key, defaultValue);
        return defaultValue;
    }


    public boolean getPreference(String key, boolean defaultValue) {
        if (sharedPreferences != null)
            return sharedPreferences.getBoolean(key, defaultValue);
        return defaultValue;
    }


    public void saveCountry(ModelCountry country) {
        savePreference(Constants.COUNTRY_NAME, country.getName());
        savePreference(Constants.COUNTRY_ID, country.getId());
        savePreference(Constants.COUNTRY_CURRENCY, country.getCurrency());
    }


    public void UpdateStatue(String status) {
        savePreference(Constants.USER_LOGIN, status);
    }


    public void saveUser(ModelUser user) {

        savePreference(Constants.USER_NAME, user.getName());
        savePreference(Constants.USER_TOKEN, user.getAccess_token());
        savePreference(Constants.USER_MOBILE, user.getMobile());
        savePreference(Constants.USER_COUNTRY_CODE, user.getCountry_code());
        savePreference(Constants.USER_EMAIL, user.getEmail());
        savePreference(Constants.USER_ID, user.getId());
        savePreference(Constants.USER_REMEMBER_ME, user.isRememberMe());
        savePreference(Constants.USER_PASSWORD, user.getPassword());
        savePreference(Constants.USER_PASSPORT_NUM, user.getPassport_no());
        savePreference(Constants.USER_PASSPORT_EX_Date, user.getPassport_expiry());
        savePreference(Constants.USER_NATIONALITY, user.getNationality());
        savePreference(Constants.USER_CIVIL_ID, user.getCivil_id());
        savePreference(Constants.USER_ENABLE_NOTIFICATION, user.getEnable_notification());

    }

    public ModelUser getUser() {
        ModelUser user = new ModelUser();
        user.setName(getPreference(Constants.USER_NAME, null));
        user.setAccess_token(getPreference(Constants.USER_TOKEN, null));
        user.setMobile(getPreference(Constants.USER_MOBILE, null));
        user.setCountry_code(getPreference(Constants.USER_COUNTRY_CODE, null));
        user.setEmail(getPreference(Constants.USER_EMAIL, ""));
        user.setId(getPreference(Constants.USER_ID, null));
        user.setRememberMe(getPreference(Constants.USER_REMEMBER_ME, false));
        user.setPassword(getPreference(Constants.USER_PASSWORD, null));
        user.setPassport_no(getPreference(Constants.USER_PASSPORT_NUM, ""));
        user.setPassport_expiry(getPreference(Constants.USER_PASSPORT_EX_Date, ""));
        user.setNationality(getPreference(Constants.USER_NATIONALITY, ""));
        user.setCivil_id(getPreference(Constants.USER_CIVIL_ID, ""));
        user.setEnable_notification(getPreference(Constants.USER_ENABLE_NOTIFICATION, 1));
        return user;
    }

    public void logOut() {
        savePreference(Constants.USER_NAME, null);
        savePreference(Constants.USER_TOKEN, null);
        // savePreference(Constants.USER_MOBILE, null);
        // savePreference(Constants.USER_COUNTRY_CODE, null);
        // savePreference(Constants.USER_EMAIL, null);
        savePreference(Constants.USER_ID, null);
        savePreference(Constants.USER_PASSPORT_NUM, null);
        savePreference(Constants.USER_NATIONALITY, null);
        savePreference(Constants.USER_CIVIL_ID, null);
    }

}
