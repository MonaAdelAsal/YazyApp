package com.asc.yazy.core;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.asc.yazy.R;
import com.asc.yazy.animattion.CustomAnimation;
import com.asc.yazy.databinding.MonthPickerBinding;
import com.asc.yazy.interfaces.OnDialogDismissListener;
import com.asc.yazy.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class CalenderEnhancedDialog extends Dialog {


    private final Context context;
    private final Dialog monthPicker;
    private final MonthPickerBinding binding;
    private final OnDialogDismissListener listener;
    private final String dateType;
    private final int monthLimit;
    private final int yearLimit;
    private final int currentYear;
    private final int currentMonth;
    private int year;
    private String month;
    private String date;
    private int selectedMonth;
    private int selectedYear;


    @SuppressLint("SetTextI18n")
    public CalenderEnhancedDialog(@NonNull Context context, int selectedMonth, int selectedYear, int monthLimit, int yearLimit, OnDialogDismissListener listener, String dateType) {
        super(context);
        CheckLanguage checkLanguage = new CheckLanguage(context);
        checkLanguage.UpdateLanguage();

        this.context = context;
        this.listener = listener;
        this.dateType = dateType;
        this.monthLimit = monthLimit;
        this.yearLimit = yearLimit;

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("MM");
        Date Months = new Date();
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        currentMonth = Integer.parseInt(dateFormat.format(Months));

        if (selectedMonth != -1 && selectedYear != -1) {
            this.selectedMonth = selectedMonth;
            this.selectedYear = selectedYear;
            this.year = selectedYear;

        } else {
            this.selectedMonth = currentMonth;
            this.selectedYear = currentYear;
            this.year = currentYear;
        }

        switch (this.selectedMonth) {
            case 1:
                date = year + context.getString(R.string.one);
                month = context.getString(R.string.jan) + " " + year;
                break;
            case 2:
                date = year + context.getString(R.string.two);
                month = context.getString(R.string.feb) + " " + year;
                break;
            case 3:
                date = year + context.getString(R.string.three);
                month = context.getString(R.string.mar) + " " + year;
                break;
            case 4:
                date = year + context.getString(R.string.four);
                month = context.getString(R.string.apr) + " " + year;
                break;
            case 5:
                date = year + context.getString(R.string.five);
                month = context.getString(R.string.May) + " " + year;
                break;
            case 6:
                date = year + context.getString(R.string.six);
                month = context.getString(R.string.jun) + " " + year;
                break;
            case 7:
                date = year + context.getString(R.string.seven);
                month = context.getString(R.string.jul) + " " + year;
                break;
            case 8:
                date = year + context.getString(R.string.eight);
                month = context.getString(R.string.aug) + " " + year;
                break;
            case 9:
                date = year + context.getString(R.string.nine);
                month = context.getString(R.string.sep) + " " + year;
                break;
            case 10:
                date = year + context.getString(R.string.ten);
                month = context.getString(R.string.oct) + " " + year;
                break;
            case 11:
                date = year + context.getString(R.string.eleven);
                month = context.getString(R.string.nov) + " " + year;
                break;
            case 12:
                date = year + context.getString(R.string.twelve);
                month = context.getString(R.string.dec) + " " + year;
                break;
        }

        year = currentYear;
        monthPicker = new Dialog(Objects.requireNonNull(context), android.R.style.Theme_DeviceDefault_Dialog);
        monthPicker.setCancelable(true);
        Objects.requireNonNull(monthPicker.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.month_picker, null, false);
        monthPicker.setContentView(binding.getRoot());


        initListeners();

        updateCalenderWithMonth(context, this.selectedMonth, this.selectedYear);
        binding.year.setText(Calendar.getInstance().get(Calendar.YEAR) + "");
        monthPicker.show();
        entranceAnimation();

    }

    private void initListeners() {

        binding.jan.setOnClickListener(v -> {
            if (currentMonth == 1 || year != currentYear) {
                date = year + context.getString(R.string.one);
                month = context.getString(R.string.January) + "-" + year;
                updateDesign(context);
                binding.jan.setTextColor(context.getResources().getColor(R.color.white));
                binding.jan.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 1;
                this.selectedYear = year;
            }


        });
        binding.feb.setOnClickListener(v -> {
            if (currentMonth <= 2 || year != currentYear) {
                date = year + context.getString(R.string.two);
                month = context.getString(R.string.February) + "-" + year;
                updateDesign(context);
                binding.feb.setTextColor(context.getResources().getColor(R.color.white));
                binding.feb.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 2;
                this.selectedYear = year;
            }
        });
        binding.mar.setOnClickListener(v -> {
            if (currentMonth <= 3 || year != currentYear) {
                date = year + context.getString(R.string.three);
                month = context.getString(R.string.March) + "-" + year;
                updateDesign(context);
                binding.mar.setTextColor(context.getResources().getColor(R.color.white));
                binding.mar.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 3;
                this.selectedYear = year;
            }
        });
        binding.apr.setOnClickListener(v -> {
            if (currentMonth <= 4 || year != currentYear) {
                date = year + context.getString(R.string.four);
                month = context.getString(R.string.April) + "-" + year;
                updateDesign(context);
                binding.apr.setTextColor(context.getResources().getColor(R.color.white));
                binding.apr.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 4;
                this.selectedYear = year;
            }
        });
        binding.may.setOnClickListener(v -> {
            if (currentMonth <= 5 || year != currentYear) {
                date = year + context.getString(R.string.five);
                month = context.getString(R.string.May) + "-" + year;
                updateDesign(context);
                binding.may.setTextColor(context.getResources().getColor(R.color.white));
                binding.may.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 5;
                this.selectedYear = year;
            }
        });
        binding.jun.setOnClickListener(v -> {
            if (currentMonth <= 6 || year != currentYear) {
                date = year + context.getString(R.string.six);
                month = context.getString(R.string.June) + "-" + year;
                updateDesign(context);
                binding.jun.setTextColor(context.getResources().getColor(R.color.white));
                binding.jun.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 6;
                this.selectedYear = year;
            }
        });
        binding.jul.setOnClickListener(v -> {
            if (currentMonth <= 7 || year != currentYear) {
                date = year + context.getString(R.string.seven);
                month = context.getString(R.string.July) + "-" + year;
                updateDesign(context);
                binding.jul.setTextColor(context.getResources().getColor(R.color.white));
                binding.jul.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 7;
                this.selectedYear = year;
            }
        });
        binding.aug.setOnClickListener(v -> {
            if (currentMonth <= 8 || year != currentYear) {
                date = year + context.getString(R.string.eight);
                month = context.getString(R.string.August) + "-" + year;
                updateDesign(context);
                binding.aug.setTextColor(context.getResources().getColor(R.color.white));
                binding.aug.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 8;
                this.selectedYear = year;
            }
        });
        binding.sep.setOnClickListener(v -> {
            if (currentMonth <= 9 || year != currentYear) {
                date = year + context.getString(R.string.nine);
                month = context.getString(R.string.September) + "-" + year;
                updateDesign(context);
                binding.sep.setTextColor(context.getResources().getColor(R.color.white));
                binding.sep.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 9;
                this.selectedYear = year;
            }
        });
        binding.oct.setOnClickListener(v -> {
            if (currentMonth <= 10 || year != currentYear) {
                date = year + context.getString(R.string.ten);
                month = context.getString(R.string.October) + "-" + year;
                updateDesign(context);
                binding.oct.setTextColor(context.getResources().getColor(R.color.white));
                binding.oct.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 10;
                this.selectedYear = year;
            }
        });
        binding.nov.setOnClickListener(v -> {
            if (currentMonth <= 11 || year != currentYear) {
                date = year + context.getString(R.string.eleven);
                month = context.getString(R.string.November) + "-" + year;
                updateDesign(context);
                binding.nov.setTextColor(context.getResources().getColor(R.color.white));
                binding.nov.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 11;
                this.selectedYear = year;
            }
        });
        binding.des.setOnClickListener(v -> {
            if (currentMonth <= 12 || year != currentYear) {
                date = year + context.getString(R.string.twelve);
                month = context.getString(R.string.December) + "-" + year;
                updateDesign(context);
                binding.des.setTextColor(context.getResources().getColor(R.color.white));
                binding.des.setBackgroundResource(R.drawable.sold_blue);
                this.selectedMonth = 12;
                this.selectedYear = year;
            }
        });
        binding.cancel.setOnClickListener(v -> monthPicker.dismiss());
        binding.ok.setOnClickListener(v -> {

            if (this.monthLimit != -1 || this.yearLimit != -1) {
                if (dateType.equals(Constants.START_DATE) && this.selectedYear == yearLimit && this.selectedMonth > monthLimit || dateType.equals(Constants.START_DATE) && this.selectedYear > yearLimit) {
                    Toast.makeText(context, context.getString(R.string.start_date_invalid), Toast.LENGTH_LONG).show();
                    return;
                } else if (dateType.equals(Constants.END_DATE) && this.selectedYear == yearLimit && this.selectedMonth < monthLimit || dateType.equals(Constants.END_DATE) && this.selectedYear < yearLimit) {
                    Toast.makeText(context, R.string.endDate_notvalid, Toast.LENGTH_LONG).show();
                    return;
                }

            }

            listener.OnDialogDismissListener(this.selectedMonth, this.selectedYear, month, date, dateType);
            monthPicker.dismiss();
        });
        binding.imgPrevious.setOnClickListener(v -> {
            if (year != currentYear) {
                year--;
                binding.year.setText((year + ""));
                updateCalenderWithMonth(context, this.selectedMonth, this.selectedYear);
            }
        });
        binding.imgNext.setOnClickListener(v -> {
            year++;
            binding.year.setText((year + ""));
            updateCalenderWithMonth(context, this.selectedMonth, this.selectedYear);

        });
    }

    private void entranceAnimation() {

        int offsetFirstLine = 1;
        int duration = 200;
        int lepOne = 40;


        CustomAnimation.scaleOutWithPulse(binding.jan, offsetFirstLine, duration);
        offsetFirstLine += lepOne;
        CustomAnimation.scaleOutWithPulse(binding.feb, offsetFirstLine, duration);
        offsetFirstLine += lepOne;
        CustomAnimation.scaleOutWithPulse(binding.mar, offsetFirstLine, duration);
        offsetFirstLine += lepOne;
        CustomAnimation.scaleOutWithPulse(binding.apr, offsetFirstLine, duration);
        offsetFirstLine += lepOne;

        CustomAnimation.scaleOutWithPulse(binding.may, offsetFirstLine, duration);
        offsetFirstLine += lepOne;
        CustomAnimation.scaleOutWithPulse(binding.jun, offsetFirstLine, duration);
        offsetFirstLine += lepOne;
        CustomAnimation.scaleOutWithPulse(binding.jul, offsetFirstLine, duration);
        offsetFirstLine += lepOne;
        CustomAnimation.scaleOutWithPulse(binding.aug, offsetFirstLine, duration);
        offsetFirstLine += lepOne;

        CustomAnimation.scaleOutWithPulse(binding.sep, offsetFirstLine, duration);
        offsetFirstLine += lepOne;
        CustomAnimation.scaleOutWithPulse(binding.oct, offsetFirstLine, duration);
        offsetFirstLine += lepOne;
        CustomAnimation.scaleOutWithPulse(binding.nov, offsetFirstLine, duration);
        offsetFirstLine += lepOne;
        CustomAnimation.scaleOutWithPulse(binding.des, offsetFirstLine, duration);

    }

    private void updateCalenderWithMonth(Context context, int selectedMonth, int selectedYear) {

        updateDesign(context);
        if (selectedMonth == 1 && selectedYear == year) {
            binding.jan.setTextColor(context.getResources().getColor(R.color.white));
            binding.jan.setBackgroundResource(R.drawable.sold_blue);
        } else if (selectedMonth == 2 && selectedYear == year) {
            binding.feb.setTextColor(context.getResources().getColor(R.color.white));
            binding.feb.setBackgroundResource(R.drawable.sold_blue);
        } else if (selectedMonth == 3 && selectedYear == year) {
            binding.mar.setTextColor(context.getResources().getColor(R.color.white));
            binding.mar.setBackgroundResource(R.drawable.sold_blue);
        } else if (selectedMonth == 4 && selectedYear == year) {
            binding.apr.setTextColor(context.getResources().getColor(R.color.white));
            binding.apr.setBackgroundResource(R.drawable.sold_blue);
        } else if (selectedMonth == 5 && selectedYear == year) {
            binding.may.setTextColor(context.getResources().getColor(R.color.white));
            binding.may.setBackgroundResource(R.drawable.sold_blue);
        } else if (selectedMonth == 6 && selectedYear == year) {
            binding.jun.setTextColor(context.getResources().getColor(R.color.white));
            binding.jun.setBackgroundResource(R.drawable.sold_blue);
        } else if (selectedMonth == 7 && selectedYear == year) {
            binding.jul.setTextColor(context.getResources().getColor(R.color.white));
            binding.jul.setBackgroundResource(R.drawable.sold_blue);
        } else if (selectedMonth == 8 && selectedYear == year) {
            binding.aug.setTextColor(context.getResources().getColor(R.color.white));
            binding.aug.setBackgroundResource(R.drawable.sold_blue);
        } else if (selectedMonth == 9 && selectedYear == year) {
            binding.sep.setTextColor(context.getResources().getColor(R.color.white));
            binding.sep.setBackgroundResource(R.drawable.sold_blue);
        } else if (selectedMonth == 10 && selectedYear == year) {
            binding.oct.setTextColor(context.getResources().getColor(R.color.white));
            binding.oct.setBackgroundResource(R.drawable.sold_blue);
        } else if (selectedMonth == 11 && selectedYear == year) {
            binding.nov.setTextColor(context.getResources().getColor(R.color.white));
            binding.nov.setBackgroundResource(R.drawable.sold_blue);
        } else if (selectedMonth == 12 && selectedYear == year) {
            binding.des.setTextColor(context.getResources().getColor(R.color.white));
            binding.des.setBackgroundResource(R.drawable.sold_blue);
        }

    }

    private void updateDesign(Context context) {

        binding.jan.setBackgroundColor(context.getResources().getColor(R.color.calGray));
        binding.feb.setBackgroundColor(context.getResources().getColor(R.color.calGray));
        binding.mar.setBackgroundColor(context.getResources().getColor(R.color.calGray));
        binding.apr.setBackgroundColor(context.getResources().getColor(R.color.calGray));
        binding.may.setBackgroundColor(context.getResources().getColor(R.color.calGray));
        binding.jun.setBackgroundColor(context.getResources().getColor(R.color.calGray));
        binding.jul.setBackgroundColor(context.getResources().getColor(R.color.calGray));
        binding.aug.setBackgroundColor(context.getResources().getColor(R.color.calGray));
        binding.sep.setBackgroundColor(context.getResources().getColor(R.color.calGray));
        binding.oct.setBackgroundColor(context.getResources().getColor(R.color.calGray));
        binding.nov.setBackgroundColor(context.getResources().getColor(R.color.calGray));
        binding.des.setBackgroundColor(context.getResources().getColor(R.color.calGray));


        if (currentMonth <= 1 && year == currentYear || year > currentYear)
            binding.jan.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.jan.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        if (2 >= currentMonth && year >= currentYear || year > currentYear)
            binding.feb.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.feb.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        if (3 >= currentMonth && year >= currentYear || year > currentYear)
            binding.mar.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.mar.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        if (4 >= currentMonth && year >= currentYear || year > currentYear)
            binding.apr.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.apr.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        if (5 >= currentMonth && year >= currentYear || year > currentYear)
            binding.may.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.may.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        if (6 >= currentMonth && year >= currentYear || year > currentYear)
            binding.jun.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.jun.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        if (7 >= currentMonth && year >= currentYear || year > currentYear)
            binding.jul.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.jul.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        if (8 >= currentMonth && year >= currentYear || year > currentYear)
            binding.aug.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.aug.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        if (9 >= currentMonth && year >= currentYear || year > currentYear)
            binding.sep.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.sep.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        if (10 >= currentMonth && year >= currentYear || year > currentYear)
            binding.oct.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.oct.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        if (11 >= currentMonth && year >= currentYear || year > currentYear)
            binding.nov.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.nov.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        if (12 >= currentMonth && year >= currentYear || year > currentYear)
            binding.des.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.semiBlack));
        else
            binding.des.setTextColor(Objects.requireNonNull(context).getResources().getColor(R.color.darkGray));

        // -----------------------------------------------------------------------------------------------------



    }


}