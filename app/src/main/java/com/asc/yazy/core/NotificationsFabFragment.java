package com.asc.yazy.core;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelUser;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.interfaces.OnNotificationsSettingsListener;


public class NotificationsFabFragment extends AAH_FabulousFragment implements View.OnClickListener {

    private final OnNotificationsSettingsListener listener;


    public NotificationsFabFragment(OnNotificationsSettingsListener listener) {
        this.listener = listener;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        com.asc.yazy.databinding.LayoutNotifcationsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_notifcations, null, false);
        binding.tvCancel.setOnClickListener(v -> closeFilter(null));
        //params to set
        setAnimationDuration(400); //optional; default 500ms
        setPeekHeight(300); // optional; default 400dp
        setViewMain(binding.layoutMain); //necessary; main bottomsheet view
        setMainContentView(binding.getRoot()); // necessary; call at end before super
        binding.tvClearAll.setOnClickListener(this);
        binding.tvMarkAllAsRead.setOnClickListener(this);

        ModelUser user = UtilsPreference.getInstance(getContext()).getUser();
        if (user == null || user.getAccess_token() == null || user.getAccess_token().isEmpty())
            binding.tvClearAll.setVisibility(View.GONE);
        else
            binding.tvClearAll.setVisibility(View.VISIBLE);

        super.setupDialog(dialog, style); //call super at last

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.tvMarkAllAsRead:
                closeFilter(null);
                if (listener != null)
                    listener.onMarkAllAsRead();
                break;
            case R.id.tvClearAll:
                closeFilter(null);
                if (listener != null)
                    listener.onClearAll();
                break;

        }

    }

}
