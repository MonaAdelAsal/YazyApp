package com.asc.yazy.utils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.asc.yazy.application.TravelXApplication;
import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsUtility {

    private static final String TAG = "AnalyticsUtility";

    public static void logEvent(String name, String type) {

        try {

            TravelXApplication application = new TravelXApplication();
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
            application.getFireballAnalytics().logEvent(type, bundle);

/*
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            application.getFireballAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
*/


        } catch (Exception e) {
            Log.d(TAG, "logEvent: " + e.getMessage());
        }

    }

    public static void logEventOpen(String parentName) {
        try {
            TravelXApplication application = new TravelXApplication();
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, parentName);
            application.getFireballAnalytics().logEvent(AnalyticsUtility.Events.EVENT_OPEN, bundle);
        } catch (Exception e) {
            Log.d(TAG, "logEvent: " + e.getMessage());
        }

    }

    public static void logEventLoadDate(String parentName) {
        try {
            TravelXApplication application = new TravelXApplication();
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, parentName);
            application.getFireballAnalytics().logEvent(AnalyticsUtility.Events.EVENT_LOAD_DATA, bundle);
        } catch (Exception e) {
            Log.d(TAG, "logEvent: " + e.getMessage());
        }

    }

    public static void logEventAPISuccess(String parentName) {

        try {


            TravelXApplication application = new TravelXApplication();
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, parentName);
            application.getFireballAnalytics().logEvent(AnalyticsUtility.Events.EVENT_API_SUCCESS, bundle);


        } catch (Exception e) {
            Log.d(TAG, "logEvent: " + e.getMessage());
        }

    }


    public static void logEventAPIFail(String parentName) {

        try {

            TravelXApplication application = new TravelXApplication();
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, parentName);
            application.getFireballAnalytics().logEvent(AnalyticsUtility.Events.EVENT_API_FAIL, bundle);

        } catch (Exception e) {
            Log.d(TAG, "logEvent: " + e.getMessage());
        }

    }

    public static void setScreen(Activity activity) {
        try {
            TravelXApplication application = new TravelXApplication();
            application.getFireballAnalytics().setCurrentScreen(activity, activity.getClass().getSimpleName(), null);

        } catch (Exception e) {
            Log.d(TAG, "logEvent: " + e.getMessage());
        }
    }

    public static void setScreen(Fragment activity) {
        try {
            TravelXApplication application = new TravelXApplication();
            if (activity != null && activity.getActivity() != null)
                application.getFireballAnalytics().setCurrentScreen(activity.getActivity(), activity.getClass().getSimpleName(), null);
        } catch (Exception e) {
            Log.d(TAG, "logEvent: " + e.getMessage());
        }
    }


    public static void logAction(String parentName) {

        try {

            try {
                TravelXApplication application = new TravelXApplication();
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, parentName);
                application.getFireballAnalytics().logEvent(parentName, bundle);
            } catch (Exception e) {
                Log.d(TAG, "logAction: " + e.getMessage());
            }

        } catch (Exception e) {
            Log.d(TAG, "logAction: " + e.getMessage());
        }

    }

    public static class Actions {

        //Contact Us Actions
        public static final String DIAL_TRAVEL_AGENCY = "DIAL_TRAVEL_AGENCY";
        public static final String EMAIL_TRAVEL_AGENCY = "EMAIL_TRAVEL_AGENCY";

        //Change language
        public static final String CHANGE_LANGUAGE_ARABIC = "CHANGE_LANGUAGE_ARABIC";
        public static final String CHANGE_LANGUAGE_ENGLISH = "CHANGE_LANGUAGE_ENGLISH";
        public static final String CHANGE_LANGUAGE_CONFIRM = "CHANGE_LANGUAGE_CONFIRM";

        //Notifications
        public static final String NOTIFICATION_OPEN_OFFER = "NOTIFICATION_OPEN_OFFER";
        public static final String NOTIFICATION_OPEN_RATE = "NOTIFICATION_OPEN_RATE";
        public static final String NOTIFICATIONS_CLEAR_ALL = "NOTIFICATIONS_CLEAR_ALL";
        public static final String NOTIFICATIONS_MARK_ALL_AS_READ = "NOTIFICATIONS_MARK_ALL_AS_READ";

        //profile
        public static final String PROFILE_CHANGE_PASSWORD = "PROFILE_CHANGE_PASSWORD";
        public static final String PROFILE_FREQUENT_TRAVELLERS = "PROFILE_FREQUENT_TRAVELLERS";
        public static final String PROFILE_TRIPS = "PROFILE_TRIPS";
        public static final String PROFILE_NOTIFICATION_SETTINGS = "PROFILE_NOTIFICATION_SETTINGS";
        public static final String PROFILE_FAVORITE = "PROFILE_FAVORITE";
        public static final String PROFILE_ACCOUNT_INFO = "PROFILE_ACCOUNT_INFO";
        public static final String PROFILE_LOG_OUT = "PROFILE_LOG_OUT";
        public static final String PROFILE_LOGIN = "PROFILE_LOGIN";
        public static final String PROFILE_LOG_OUT_FAIL = "PROFILE_LOG_OUT_FAIL";
        public static final String PROFILE_LOG_OUT_SUCCESS = "PROFILE_LOG_OUT_SUCCESS";


        //Search, search history , search results
        public static final String SEARCH = "SEARCH";
        public static final String SEARCH_HISTORY = "SEARCH_HISTORY";
        public static final String SEARCH_FILTER_PRICE_HIGH_TO_LOW = "SEARCH_FILTER_PRICE_HIGH_TO_LOW";
        public static final String SEARCH_FILTER_PRICE_LOW_TO_HIGH = "SEARCH_FILTER_PRICE_LOW_TO_HIGH";
        public static final String SEARCH_FILTER_PRICE_MOST_LIKE = "SEARCH_FILTER_PRICE_MOST_LIKE";
        public static final String SEARCH_FILTER_PRICE_LESS_LIKE = "SEARCH_FILTER_PRICE_LESS_LIKE";
        public static final String SEARCH_RECENT_SEARCH_SELECTED = "SEARCH_RECENT_SEARCH_SELECTED";
        public static final String SEARCH_TRAVEL_AGENCY = "SEARCH_TRAVEL_AGENCY";
        public static final String SEARCH_DESTINATION = "SEARCH_DESTINATION";
        public static final String SEARCH_DATE_TO = "SEARCH_DATE_TO";
        public static final String SEARCH_DATE_FROM = "SEARCH_DATE_FROM";
        public static final String SEARCH_FILTER = "SEARCH_FILTER";
        public static final String SEARCH_GRID_VIEW = "SEARCH_GRID_VIEW";
        public static final String SEARCH_LINEAR_VIEW = "SEARCH_LINEAR_VIEW";

        //general
        public static final String OPEN_OFFER_DETAILS = "OPEN_OFFER_DETAILS";
        public static final String OPEN_MULTI_CITIES_OFFER_DETAILS = "OPEN_MULTI_CITIES_OFFER_DETAILS";
        public static final String OPEN_REGISTER = "PROFILE_REGISTER";
        public static final String LIKE = "LIKE";
        public static final String SHARE = "SHARE";

        //home
        public static final String HOME_GRID_VIEW = "HOME_GRID_VIEW";
        public static final String HOME_LINEAR_VIEW = "HOME_LINEAR_VIEW";

        public static final String OPEN_HOME = "OPEN_HOME";
        public static final String OPEN_SEARCH = "OPEN_SEARCH";
        public static final String OPEN_PROFILE = "OPEN_PROFILE";
        public static final String OPEN_NOTIFICATIONS = "OPEN_NOTIFICATIONS";
        public static final String OPEN_MENU = "OPEN_MENU";

        //offer details
        public static final String OPEN_BOOK = "OPEN_BOOK";
        public static final String OPEN_SELECT_DATE = "OPEN_SELECT_DATE";
        public static final String OPEN_TRAVEL_AGENCY = "OPEN_TRAVEL_AGENCY";
        public static final String OPEN_CITY = "OPEN_CITY";
        public static final String OPEN_OFFER_POLICY = "OPEN_OFFER_POLICY";


        //travel agency
        public static final String OPEN_RATE = "OPEN_RATE";
        public static final String OPEN_TRAVEL_AGENCY_EMAIL = "OPEN_TRAVEL_AGENCY_EMAIL";
        public static final String OPEN_TRAVEL_AGENCY_LOCATION = "OPEN_TRAVEL_AGENCY_LOCATION";

        //profile
        public static final String OPEN_EDIT_PROFILE = "OPEN_EDIT_PROFILE";
        public static final String OPEN_BOOK_DETAILS = "OPEN_BOOK_DETAILS";
        public static final String OPEN_ADD_FREQUENT_TRAVELLERS = "OPEN_ADD_FREQUENT_TRAVELLERS";
        public static final String ADD_FREQUENT_TRAVELLER_PASSPORT = "ADD_FREQUENT_TRAVELLER_PASSPORT";
        public static final String ADD_FREQUENT_TRAVELLER_CIVIL_ID = "ADD_FREQUENT_TRAVELLER_CIVIL_ID";
        public static final String ADD_CHILD_FREQUENT_TRAVELLER = "ADD_CHILD_FREQUENT_TRAVELLER";
        public static final String ADD_ADULT_FREQUENT_TRAVELLER = "ADD_ADULT_FREQUENT_TRAVELLER";
        public static final String ADD_FREQUENT_TRAVELLER = "ADD_FREQUENT_TRAVELLER";
        public static final String NOTIFICATIONS_SETTINGS_ENABLED = "NOTIFICATIONS_SETTINGS_ENABLED";
        public static final String NOTIFICATIONS_SETTINGS_DISABLED = "NOTIFICATIONS_SETTINGS_DISABLED";
        public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";

        //booking
        public static final String BOOKING_FREQUENT_TRAVELLERS_COUNT = "BOOKING_FREQUENT_TRAVELLERS_COUNT";
        public static final String BOOKING_ADD_ADULTS_INFO = "BOOKING_ADD_ADULTS_INFO";
        public static final String BOOKING_ADD_CHILD_INFO = "BOOKING_ADD_CHILD_INFO";
        public static final String OPEN_PAYMENT = "OPEN_PAYMENT";
        public static final String PAY_FOR_ME = "PAY_FOR_ME";

        //payment
        public static final String PAYMENT_TRY_AGAIN = "PAYMENT_TRY_AGAIN";

        //auth
        public static final String GET_PEN_CODE = "GET_PEN_CODE";
        public static final String RESEND_PEN_CODE = "RESEND_PEN_CODE";
        public static final String OPEN_RESET_PASSWORD = "OPEN_RESET_PASSWORD";
        public static final String LOGIN = "LOGIN";
        public static final String SKIP = "SKIP";
        public static final String OPEN_FORGET_PASSWORD = "OPEN_FORGET_PASSWORD";

        //rate
        public static final String RATE = "RATE";

        //booking details
        public static final String RE_SHARE_PAYMENT_LINK = "RE_SHARE_PAYMENT_LINK";
        public static final String NOTIFICATION_OPEN_BOOK_DETAILS = "NOTIFICATION_OPEN_BOOK_DETAILS";
        //cancel
        public static final String CANCELLATION_POLICY = "CANCELLATION_POLICY";
        //search
        public static final String SEARCH_MULTI_CITY = "SEARCH_MULTI_CITY";
        public static final String SEARCH_SINGLE_CITY = "SEARCH_SINGLE_CITY";
        public static final String SEARCH_OPEN = "SEARCH_OPEN";
        public static final String SEARCH_FIXED = "SEARCH_FIXED";
        //check out
        public static final String OPEN_CHECK_OUT = "OPEN_CHECK_OUT";
        public static final String CHECK_PROMO_CODE = "CHECK_PROMO_CODE";

        //check out
        public static final String OPEN_FAQS = "OPEN_FAQS";
        public static final String OPEN_Question_Answer = "OPEN_Question_Answer";


        public static final String PROFILE_POINTS = "PROFILE_POINTS";
        public static final String REDEEM_POINTS = "REDEEM_POINTS";
    }

    public static class Events {


        //VIEWS
        public static final String SPLASH = "SPLASH";
        public static final String HOME = "HOME";
        public static final String OFFER_DETAILS = "OFFER_DETAILS";
        public static final String SEARCH = "SEARCH";
        public static final String SEARCH_RESULT = "SEARCH_RESULT";
        public static final String FAVORITE = "FAVORITE";
        public static final String PAYMENT_CONFIRMATION = "PAYMENT_CONFIRMATION";
        public static final String MENU = "MENU";
        public static final String ABOUT = "ABOUT";
        public static final String SHARE_DONE = "SHARE_DONE";
        public static final String FAQs = "FAQs";
        public static final String CONTACT_US = "CONTACT_US";
        public static final String TRAVEL_AGENCY_PROFILE = "TRAVEL_AGENCY_PROFILE";
        public static final String COUNTRY = "COUNTRY";
        public static final String NOTIFICATIONS = "NOTIFICATIONS";

        public static final String PRIVACY = "PRIVACY";
        public static final String TERMS_AND_CONDITIONS = "TERMS_AND_CONDITIONS";
        public static final String LOGIN = "LOGIN";
        public static final String VERIFICATION = "VERIFICATION";
        public static final String CONFIRM_CODE = "CONFIRM_CODE";

        public static final String REGISTER = "REGISTER";
        public static final String FORGET_PASSWORD = "FORGET_PASSWORD";
        public static final String NEW_PASSWORD = "NEW_PASSWORD";
        public static final String MY_TRIPS = "MY_TRIPS";
        public static final String PROFILE = "PROFILE";
        public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";
        public static final String EDIT_PROFILE = "EDIT_PROFILE";
        public static final String SEND_QUESTION = "SEND_QUESTION";
        public static final String ACCOUNT_INFO = "ACCOUNT_INFO";
        public static final String BOOK_DETAILS = "BOOK_DETAILS";
        public static final String CHANGE_LANGUAGE = "CHANGE_LANGUAGE";
        public static final String BOOKING_COUNT = "BOOKING_COUNT";
        public static final String BOOKING_CONTACT_INFO = "BOOKING_CONTACT_INFO";
        public static final String BOOKING_ADULTS_INFO = "BOOKING_ADULTS_INFO";
        public static final String BOOKING_CONTROLLER = "BOOKING_CONTROLLER";

        public static final String NOTIFICATIONS_SETTINGS = "NOTIFICATIONS_SETTINGS";
        public static final String SEARCH_HISTORY = "SEARCH_HISTORY";
        public static final String BOOKING_TRAVELLERS = "BOOKING_TRAVELLERS";
        public static final String CITIES = "CITIES";
        public static final String REVIEWS = "REVIEWS";


        public static final String OFFER_DETAILS_MULTI_CITIES = "OFFER_DETAILS_MULTI_CITIES";
        public static final String CITY_DETAILS = "CITY_DETAILS";
        public static final String PENDING_TRIPS = "PENDING_TRIPS";
        public static final String CONFIRMED_TRIPS = "CONFIRMED_TRIPS";
        public static final String FREQUENT_TRAVELLERS = "FREQUENT_TRAVELLERS";
        public static final String ADD_FREQUENT_TRAVELLER = "ADD_FREQUENT_TRAVELLER";

        public static final String BOOKING_CHILD_INFO = "BOOKING_CHILD_INFO";
        public static final String PAYMENT_SUCCESS = "PAYMENT_SUCCESS";
        public static final String PAYMENT_FAIL = "PAYMENT_FAIL";
        public static final String PAYMENT = "PAYMENT";
        public static final String PAYMENT_CHECKOUT = "PAYMENT_CHECKOUT";
        public static final String PAYMENT_REDIRECT_TO_PAYMENT = "PAYMENT_REDIRECT_TO_PAYMENT";
        public static final String EVENT_VERIFICATION_SUCCESS = "EVENT_VERIFICATION_SUCCESS";
        public static final String EVENT_VERIFICATION_FAIL = "EVENT_VERIFICATION_FAIL";
        public static final String EVENT_VERIFICATION_ON_CODE = "EVENT_VERIFICATION_ON_CODE";
        public static final String RATE = "RATE";


        //EVENTS
        public static final String EVENT_OPEN = "EVENT_OPEN";
        public static final String EVENT_API_SUCCESS = "EVENT_API_SUCCESS";
        public static final String EVENT_API_FAIL = "EVENT_API_FAIL";
        public static final String EVENT_LOAD_DATA = "EVENT_LOAD_DATA";
        public static final String UPDATE_PRICE = "UPDATE_PRICE";


        public static final String CHECK_OUT = "CHECK_OUT";
        public static final String REDEEM_POINTS = "REDEEM_POINTS";
    }
}
