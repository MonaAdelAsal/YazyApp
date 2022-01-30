package com.asc.yazy.interfaces;

public interface OnBackThreadListener {

    void onSuccess(String shareLink, String booking_id, int gift, String points);

    void onFailure(String error);
}
