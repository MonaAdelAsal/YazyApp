package com.asc.yazy.core;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewMedium extends AppCompatTextView {

    public TextViewMedium(Context context) {
        super(context);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Raleway-Medium.ttf");
        setTypeface(typeface);
    }

    public TextViewMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Raleway-Medium.ttf");
        setTypeface(typeface);
    }

    public TextViewMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Raleway-Medium.ttf");
        setTypeface(typeface);
    }
}