package com.asc.yazy.core;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CalenderView extends CalendarView {

    public CalenderView(@NonNull Context context) {
        super(context);
    }

    public CalenderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CalenderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CalenderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("Calender", "Done: " + ev.getActionMasked());

        Log.d("onInterceptTouchEvent", "onInterceptTouchEvent: " + ev.getActionMasked());
        // if (ev.getActionMasked() == 1 || ev.getActionMasked() == 0)
        return super.onInterceptTouchEvent(ev);
        //  return true;
    }
}
