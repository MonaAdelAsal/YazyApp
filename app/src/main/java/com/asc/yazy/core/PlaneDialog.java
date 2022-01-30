package com.asc.yazy.core;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.asc.yazy.R;

import java.util.Objects;

public class PlaneDialog extends Dialog {


    private final Dialog plane;

    @SuppressLint("SetTextI18n")
    public PlaneDialog(@NonNull Context context) {
        super(context);
        plane = new Dialog(Objects.requireNonNull(context), android.R.style.Theme_Dialog);
        plane.setContentView(R.layout.plane_loading);
        plane.setCancelable(false);
        Objects.requireNonNull(plane.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final SeekBar sbHeight = plane.findViewById(R.id.sbHeight);
        sbHeight.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        final int[] progress = {0};

        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                if (progress[0] != 100) {
                    //   AppLogger.log("Prog",progress[0]+"");
                    progress[0] = progress[0] + 1;
                    sbHeight.setProgress(progress[0]);
                    ha.postDelayed(this, 100);
                    if (progress[0] == 100)
                        progress[0] = 0;
                }
            }
        }, 100);
        plane.show();
    }

    public void Dismiss() {
        plane.dismiss();
        dismiss();
    }
}
