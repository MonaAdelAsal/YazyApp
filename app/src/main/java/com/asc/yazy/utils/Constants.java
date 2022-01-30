package com.asc.yazy.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.asc.yazy.api.model.CityModel;
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.api.model.ModelDetails;
import com.asc.yazy.api.model.ModelFav;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.model.Offer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {

    public static final String DATA_BASE_NAME = "travel.dp";
    public static final int DATA_BASE_VERSION = 26;
    public static final long CLICK_TIME_INTERVAL = 1000;
    public static final String TRANSACTION = "TRANSACTION";
    public static final String COUNTRY_NAME = "COUNTRY_NAME";
    public static final String COUNTRY_ID = "COUNTRY_ID";
    public static final String COUNTRY_CURRENCY = "COUNTRY_CURRENCY";
    public static final String IS_FIRST_RUN = "IS_FIRST_RUN";


    public static final String ANDROID_LIVE = "ANDROID";
    public static final String ANDROID_TESTING = "ANDROID_TESTING";
    public static final String ANDROID_DEVELOPMENT = "ANDROID_DEV";


    public static final String USER_LOGIN = "USER_LOGIN";
    public static final String USER_REMEMBER = "USER_REMEMBER";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_MOBILE = "USER_MOBILE";
    public static final String USER_COUNTRY_CODE = "USER_COUNTRY_CODE";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_ID = "USER_ID";
    public static final String USER_TOKEN = "USER_TOKEN";
    public static final String USER_REMEMBER_ME = "USER_REMEMBER_ME";
    public static final String USER_PASSWORD = "USER_PASSWORD";

    public static final String USER_NATIONALITY = "USER_NATIONALITY";
    public static final String USER_CIVIL_ID = "USER_CIVIL_ID";
    public static final String USER_ENABLE_NOTIFICATION = "USER_ENABLE_NOTIFICATION";
    public static final String USER_PASSPORT_NUM = "USER_PASSPORT_NUM";
    public static final String USER_PASSPORT_EX_Date = "USER_PASSPORT_EX_Date";

    public static final String USER_FIRE_BASE_TOKEN = "USER_FIRE_BASE_TOKEN";

    public static final String LANGUAGE = "LANGUAGE";
    public static final String OFFER = "OFFER";
    public static final String TRAVEL_AGENCY = "TRAVEL_AGENCY";
    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";


    public static final String LAST_CLEAR_DATE = "LAST_CLEAR_DATE";

    //public static final int USER_NOTIFICATION = 2;
    // public static final int GENERAL_NOTIFICATION = 1;
    public static final boolean REGISTRATION = true;
    public static final String ENVIRONMENT = "ENVIRONMENT";
    public static final String DEVELOPMENT = "DEVELOPMENT";
    public static final String TESTING = "TESTING";
    public static final String LIVE = "LIVE";

    public static final String IS_HOME_FIRST_RUN = "IS_HOME_FIRST_RUN";
    public static final String IS_SEARCH_FIRST_RUN = "IS_SEARCH_FIRST_RUN";
    public static final String IS_TRIP_ADAPTER_FIRST_RUN = "IS_TRIP_ADAPTER_FIRST_RUN";
    public static final String IS_TRIP_FIRST_RUN = "IS_TRIP_FIRST_RUN";

    public static final String IS_BOOK_COUNT_FIRST_RUN = "IS_BOOK_COUNT_FIRST_RUN";
    public static final String IS_BOOK_COUNT_ADULTS_FIRST_RUN = "IS_BOOK_COUNT_ADULTS_FIRST_RUN";
    public static final String IS_BOOK_COUNT_CHILD_FIRST_RUN = "IS_BOOK_COUNT_CHILD_FIRST_RUN";
    public static final String IS_BOOK_INFO_FIRST_RUN = "IS_BOOK_INFO_FIRST_RUN";
    public static final String IS_CONFIRM_BOOK_FIRST_RUN = "IS_CONFIRM_BOOK_FIRST_RUN";
    public static final String IDENTITY_CARD = "Civil id";
    public static final String TRANSACTION_TYPE = "TRANSACTION_TYPE";
    public static final String TRANSACTION_TARGET_ID = "TRANSACTION_OFFER_ID";
    public static final String TRANSACTION_NOTIFICATION_ID = "TRANSACTION_NOTIFICATION_ID";
    public static final String NOTIFICATION_TITLE = "TRANSACTION_NOTIFICATION_ID";
    public static final String NOTIFICATION_BODY = "TRANSACTION_NOTIFICATION_ID";
    public static final String IS_BOOKINGS_PENDING = "IS_BOOKINGS_PENDING";
    public static final String SELECTED_DATE = "SELECTED_DATE";
    public static final String TRANSACTION_OFFER = "TRANSACTION_OFFER";

    public static final String SEARCH_ACTION = "com.asc.yazy.SEARCH";
    public static final String TRIPS_ACTION = "com.asc.yazy.MYTRIPS";
    public static final String FAV_ACTION = "com.asc.yazy.MYFAVORITE";
    public static final String QUICK_ACCESS_CLASS_NAME = "com.asc.yazy.activity.QuickKAccessActivity";
    public static final String FIXED = "fixed";
    public static final String PERCENTAGE = "percent";
    public static final String REGISTRATION_POINTS = "REGISTRATION_POINTS";
    public static final String NOTIFICATION_COUNT = "NOTIFICATION_COUNT";


    public static final String AFRICA = "1";
    public static final String ANTERCATICA = "2";
    public static final String ASIA = "3";
    public static final String AUSURLIA = "4";
    public static final String EUROPE = "5";
    public static final String N_AMERICA = "6";
    public static final String S_AMERICA = "7";


    public static int newNotification = 0;

    public static String id = "";
    public static String language = "ar";

    public static String getLANGUAGE() {
        return language;
    }

    public static void setLanguage(String language) {
        Constants.language = language;
    }

    public static String getNormalizedPhoneNumber(String fullNumber, String countryCode) {
        return fullNumber.replaceFirst(countryCode, "");
    }


    public static void changeStatusBarColor(Activity activity, int color) {
        if (activity != null && activity.getWindow() != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null)
            return false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


    public static boolean isEmailValid(String email) {
        String regExpr =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(regExpr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return !matcher.matches();
    }

    public static String getFormattedDate(CityModel offer) {
        try {
            String DayFrom = offer.getDate_from().split("-")[2];
            String DayTo = offer.getDate_to().split("-")[2];
            String MonthFrom = offer.getDate_from().split("-")[1];
            String YearFrom = offer.getDate_from().split("-")[0];
            String MonthTo = offer.getDate_to().split("-")[1];
            String YearTo = offer.getDate_to().split("-")[0];
            return DayFrom + " " + getMonth(Integer.parseInt(MonthFrom)) + " " + YearFrom + " - " + DayTo + " " + getMonth(Integer.parseInt(MonthTo)) + " " + YearTo;
        } catch (Exception e) {
            return offer.getDate_from();
        }

    }


    public static String getFormattedDate(ModelBooking offer, int openOffer) {
        try {
            String DayFrom, DayTo, MonthFrom, YearFrom, MonthTo, YearTo;
            if (openOffer == 0) {
                DayFrom = offer.getDate_from().split("-")[2];
                DayTo = offer.getDate_to().split("-")[2];
                MonthFrom = offer.getDate_from().split("-")[1];
                YearFrom = offer.getDate_from().split("-")[0];
                MonthTo = offer.getDate_to().split("-")[1];
                YearTo = offer.getDate_to().split("-")[0];
            } else {
                DayFrom = offer.getStart_date().split("-")[2];
                DayTo = offer.getEnd_date().split("-")[2];
                MonthFrom = offer.getStart_date().split("-")[1];
                YearFrom = offer.getStart_date().split("-")[0];
                MonthTo = offer.getEnd_date().split("-")[1];
                YearTo = offer.getEnd_date().split("-")[0];
            }
            return DayFrom + " " + getMonth(Integer.parseInt(MonthFrom)) + " " + YearFrom + " - " + DayTo + " " + getMonth(Integer.parseInt(MonthTo)) + " " + YearTo;
        } catch (Exception e) {
            return offer.getDate_from();
        }

    }

    public static String getFormattedDate(ModelOffer offer) {
        try {
            String DayFrom = offer.getDate_from().split("-")[2];
            String DayTo = offer.getDate_to().split("-")[2];
            String MonthFrom = offer.getDate_from().split("-")[1];
            String YearFrom = offer.getDate_from().split("-")[0];
            String MonthTo = offer.getDate_to().split("-")[1];
            String YearTo = offer.getDate_to().split("-")[0];
            return DayFrom + " " + getMonth(Integer.parseInt(MonthFrom)) + " " + YearFrom + " - " + DayTo + " " + getMonth(Integer.parseInt(MonthTo)) + " " + YearTo;
        } catch (Exception e) {
            // return duaration of trip instead of start and end date
            return offer.getDuration();
        }
    }

    private static String getMonth(int month) {
        switch (month) {
            case 1:
                return "jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sept";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
        return "";
        // return new DateFormatSymbols().getMonths()[month - 1];
    }

    public static String getFormattedDate(ModelFav offer) {
        try {
            String DayFrom = offer.getDate_from().split("-")[2];
            String DayTo = offer.getDate_to().split("-")[2];
            String MonthFrom = offer.getDate_from().split("-")[1];
            String YearFrom = offer.getDate_from().split("-")[0];
            String MonthTo = offer.getDate_to().split("-")[1];
            String YearTo = offer.getDate_to().split("-")[0];
            return DayFrom + " " + getMonth(Integer.parseInt(MonthFrom)) + " " + YearFrom + " - " + DayTo + " " + getMonth(Integer.parseInt(MonthTo)).substring(0, 3) + " " + YearTo;
        } catch (Exception e) {
            return offer.getDate_from();
        }

    }

    public static String getFormattedTripDate(String date) {
        try {
            String Date = date.split(" ")[0];
            String Day = Date.split("-")[2];
            String Month = Date.split("-")[1];
            String Year = Date.split("-")[0];
            return Day + " " + getMonth(Integer.parseInt(Month)) + " " + Year;
        } catch (Exception e) {
            return date;
        }

    }

    public static String getFormattedDate(ModelDetails offer) {

        try {

            String DayFrom = offer.getDate_from().split("-")[2];
            String DayTo = offer.getDate_to().split("-")[2];
            String MonthFrom = offer.getDate_from().split("-")[1];
            String YearFrom = offer.getDate_from().split("-")[0];
            String MonthTo = offer.getDate_to().split("-")[1];
            String YearTo = offer.getDate_to().split("-")[0];
            return DayFrom + " " + getMonth(Integer.parseInt(MonthFrom)) + " " + YearFrom + " - " + DayTo + " " + getMonth(Integer.parseInt(MonthTo)) + " " + YearTo;
        } catch (Exception e) {
            return offer.getDate_from();
        }

    }

    public static String getFormattedDate(Offer offer) {

        try {

            String DayFrom = offer.getDate_from().split("-")[2];
            String DayTo = offer.getDate_to().split("-")[2];
            String MonthFrom = offer.getDate_from().split("-")[1];
            String YearFrom = offer.getDate_from().split("-")[0];
            String MonthTo = offer.getDate_to().split("-")[1];
            String YearTo = offer.getDate_to().split("-")[0];
            return DayFrom + " " + getMonth(Integer.parseInt(MonthFrom)) + " " + YearFrom + " - " + DayTo + " " + getMonth(Integer.parseInt(MonthTo)) + " " + YearTo;
        } catch (Exception e) {
            return offer.getDate_from();
        }

    }

    public static String getFormattedDate(String FromDate, String ToDate) {

        try {

            String DayFrom = FromDate.split("-")[2];
            String DayTo = ToDate.split("-")[2];
            String MonthFrom = FromDate.split("-")[1];
            String YearFrom = FromDate.split("-")[0];
            String MonthTo = ToDate.split("-")[1];
            String YearTo = ToDate.split("-")[0];
            return DayFrom + " " + getMonth(Integer.parseInt(MonthFrom)) + " " + YearFrom + " - " + DayTo + " " + getMonth(Integer.parseInt(MonthTo)) + " " + YearTo;
        } catch (Exception e) {
            return FromDate;
        }

    }


}
