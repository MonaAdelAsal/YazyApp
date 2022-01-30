package com.asc.yazy.core;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.asc.yazy.R;
import com.asc.yazy.databinding.FragmentSelectDateExpireBinding;
import com.asc.yazy.interfaces.OnExpiryDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DatePickerDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "SelectDateDialog";
    private final Calendar cal = Calendar.getInstance();
    private final FragmentSelectDateExpireBinding binding;
    private final Dialog selectDate;
    private final OnExpiryDateSelectedListener listener;
    private String selectedDate = "0000-00-00";
    private String formattedString;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    public DatePickerDialog(@NonNull Context context, OnExpiryDateSelectedListener listener) {
        super(context);
        this.listener = listener;
        selectDate = new Dialog(Objects.requireNonNull(context), R.style.DialogSlideAnim);
        selectDate.setCancelable(true);
        Objects.requireNonNull(selectDate.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_select_date_expire, null, false);
        selectDate.setContentView(binding.getRoot());
        CheckLanguage checkLanguage = new CheckLanguage(context);
        checkLanguage.UpdateLanguage();
        binding.btnProcess.setOnClickListener(this);

        binding.imgClose.setOnClickListener(this);

        binding.imgNextMonth.setOnClickListener(this);
        binding.imgPreviousMonth.setOnClickListener(this);

        binding.imgNextYear.setOnClickListener(this);
        binding.imgPreviousYear.setOnClickListener(this);

        binding.calender.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Log.d(TAG, "SelectDateDialog: ");
            month++;
            selectedDate = dayOfMonth + "/" + month + "/" + year;
            formattedString = year + "-" + month + "-" + dayOfMonth;
        });
        selectDate.show();
        prepareUI();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.imgClose:
                selectDate.dismiss();
                //listener.OnDateSelectedListener("");
                break;
            case R.id.btnProcess:
                if (!selectedDate.equals("0000-00-00")) {
                    if (listener != null)
                        listener.onDateSelectedListener(selectedDate, formattedString);
                    selectDate.dismiss();
                }
                break;
            case R.id.imgNextMonth:
                cal.add(Calendar.MONTH, +1); // Add a month
                binding.calender.setDate(cal.getTimeInMillis());
                @SuppressLint("SimpleDateFormat") String monthPlus = new SimpleDateFormat("MMM").format(new Date(cal.getTimeInMillis()));
                binding.month.setText(monthPlus);
                break;
            case R.id.imgPreviousMonth:
                cal.add(Calendar.MONTH, -1); // subtract a month
                binding.calender.setDate(cal.getTimeInMillis());
                @SuppressLint("SimpleDateFormat") String monthMinus = new SimpleDateFormat("MMM").format(new Date(cal.getTimeInMillis()));
                binding.month.setText(monthMinus);
                break;

            case R.id.imgNextYear:
                cal.add(Calendar.YEAR, +1); // Add a month
                binding.calender.setDate(cal.getTimeInMillis());
                @SuppressLint("SimpleDateFormat") String yearPlus = new SimpleDateFormat("yyyy").format(new Date(cal.getTimeInMillis()));
                binding.year.setText(yearPlus);
                break;
            case R.id.imgPreviousYear:
                cal.add(Calendar.YEAR, -1); // subtract a month
                binding.calender.setDate(cal.getTimeInMillis());
                @SuppressLint("SimpleDateFormat") String YearMinus = new SimpleDateFormat("yyyy").format(new Date(cal.getTimeInMillis()));
                binding.year.setText(YearMinus);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void prepareUI() {
        @SuppressLint("SimpleDateFormat") String monthPlus = new SimpleDateFormat("MMM").format(new Date(cal.getTimeInMillis()));
        binding.month.setText(monthPlus);
        @SuppressLint("SimpleDateFormat") String YearMinus = new SimpleDateFormat("yyyy").format(new Date(cal.getTimeInMillis()));
        binding.year.setText(YearMinus);
    }


}