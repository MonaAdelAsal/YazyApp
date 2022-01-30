package com.asc.yazy.core;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.asc.yazy.R;
import com.asc.yazy.databinding.FragmentSelectDateBinding;
import com.asc.yazy.interfaces.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class SelectDateDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "SelectDateDialog";
    private final Calendar cal = Calendar.getInstance();
    private final FragmentSelectDateBinding binding;
    private final Dialog selectDate;
    private final String fromDate;
    private final String toDate;
    private final Context context;
    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat = new SimpleDateFormat("MM");
    OnDateSelectedListener listener;
    private String selectedDate = "0000-00-00";

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    public SelectDateDialog(@NonNull Context context, OnDateSelectedListener listener, String dateFrom, String dateTo) {
        super(context);
        this.fromDate = dateFrom;
        this.toDate = dateTo;
        this.context = context;
        this.listener = listener;

        selectDate = new Dialog(Objects.requireNonNull(context), R.style.DialogSlideAnim);
        selectDate.setCancelable(true);
        Objects.requireNonNull(selectDate.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_select_date, null, false);
        selectDate.setContentView(binding.getRoot());
        CheckLanguage checkLanguage = new CheckLanguage(context);
        checkLanguage.UpdateLanguage();
        binding.btnProcess.setOnClickListener(this);
        binding.btnSelectLater.setOnClickListener(this);
        binding.imgClose.setOnClickListener(this);
        binding.imgNext.setOnClickListener(this);
        binding.imgPrevious.setOnClickListener(this);


        binding.calender.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Log.d(TAG, "SelectDateDialog: ");
            month++;
            selectedDate = year + "-" + month + "-" + dayOfMonth;
            @SuppressLint("SimpleDateFormat") String CurrentTime = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

            // Toast.makeText(context, SelectedDate, Toast.LENGTH_LONG).show();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date From = sdf.parse(fromDate);
                Date To = sdf.parse(toDate);
                Date Selected = sdf.parse(selectedDate);
                Date Current = sdf.parse(CurrentTime);
                if (Objects.requireNonNull(Current).after(Selected)) {
                    Toast.makeText(context, R.string.selected_day_before_current, Toast.LENGTH_LONG).show();
                    selectedDate = "0000-00-00";
                } else {
                    if ((Objects.requireNonNull(Selected).after(From) || Selected.equals(From)) && (Selected.before(To) || Selected.equals(To))) {

                        selectedDate = year + "-" + month + "-" + dayOfMonth;

                    } else {

                        Toast.makeText(context, context.getString(R.string.sorry_offer_not_valid), Toast.LENGTH_LONG).show();
                        selectedDate = "0000-00-00";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        });
        selectDate.show();
        prepareUI(dateFrom, dateTo);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.btnSelectLater:
                listener.onDateSelectedListener("");
                selectDate.dismiss();
                break;
            case R.id.imgClose:
                selectDate.dismiss();
                //listener.OnDateSelectedListener("");
                break;
            case R.id.btnProcess:
                if (!selectedDate.equals("0000-00-00")) {
                    listener.onDateSelectedListener(selectedDate);
                    selectDate.dismiss();
                }
                break;
            case R.id.imgNext:
                cal.add(Calendar.MONTH, +1); // Add a month
                binding.calender.setDate(cal.getTimeInMillis());
                @SuppressLint("SimpleDateFormat") String year = new SimpleDateFormat("yyyy").format(new Date(cal.getTimeInMillis()));
                binding.year.setText(getMonthName(Integer.parseInt(dateFormat.format(binding.calender.getDate())), Integer.parseInt(year)));
                break;
            case R.id.imgPrevious:
                cal.add(Calendar.MONTH, -1); // subtract a month
                binding.calender.setDate(cal.getTimeInMillis());
                @SuppressLint("SimpleDateFormat") String Year = new SimpleDateFormat("yyyy").format(new Date(cal.getTimeInMillis()));
                binding.year.setText(getMonthName(Integer.parseInt(dateFormat.format(binding.calender.getDate())), Integer.parseInt(Year)));
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void prepareUI(String dateFrom, String dateTo) {
        Date Months = new Date();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Integer.parseInt(dateFormat.format(Months));
        binding.year.setText(getMonthName(currentMonth, currentYear));
        binding.tvValidationDate.setText(context.getString(R.string.valid_from) + " " + dateFrom + context.getString(R.string.to) + " " + dateTo);
    }

    private String getMonthName(int currentMonth, int Year) {
        if (currentMonth == 1)
            return context.getResources().getString(R.string.jan) + " " + Year;
        else if (currentMonth == 2)
            return context.getResources().getString(R.string.feb) + " " + Year;
        else if (currentMonth == 3)
            return context.getResources().getString(R.string.mar) + " " + Year;
        else if (currentMonth == 4)
            return context.getResources().getString(R.string.apr) + " " + Year;
        else if (currentMonth == 5)
            return context.getResources().getString(R.string.may) + " " + Year;
        else if (currentMonth == 6)
            return context.getResources().getString(R.string.jun) + " " + Year;
        else if (currentMonth == 7)
            return context.getResources().getString(R.string.jul) + " " + Year;
        else if (currentMonth == 8)
            return context.getResources().getString(R.string.aug) + " " + Year;
        else if (currentMonth == 9)
            return context.getResources().getString(R.string.sep) + " " + Year;
        else if (currentMonth == 10)
            return context.getResources().getString(R.string.oct) + " " + Year;
        else if (currentMonth == 11)
            return context.getResources().getString(R.string.nov) + " " + Year;
        else if (currentMonth == 12)
            return context.getResources().getString(R.string.dec) + " " + Year;
        return "";
    }


}