package com.asc.yazy.utils;

import com.google.firebase.messaging.FirebaseMessaging;

public class UtilsFireBase {


    private static final String TAG = "FIRE_BASE_BACKEND ";

    public static boolean subscribe(String topic){
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            System.out.println(TAG + "DONE");
            return true;
        }catch (Exception ex){
            System.out.println(TAG + ex.getMessage());
            return false;
        }
    }

    public static boolean unSubscribe(String topic){
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}