package com.asc.yazy.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asc.ocr.ScannerASC;
import com.asc.yazy.interfaces.OnIntentReceived;


public class ScanFragment extends Fragment {

    private final int MY_REQUEST_CODE = 200;
    private int index;
    private OnIntentReceived onIntentReceived;

    public ScanFragment() {
    }

    public ScanFragment(int index, OnIntentReceived onIntentReceived) {
        this.index = index;
        this.onIntentReceived = onIntentReceived;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startScanning();
    }

    public void startScanning() {
        Intent i = new Intent(getActivity(), ScannerASC.class);
        startActivityForResult(i, MY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                //    Toast.makeText(getActivity(),DocumentType +" "+DocumentNumber+" "+Name+" "+Nationality,Toast.LENGTH_LONG).show();
                if (onIntentReceived != null)
                    onIntentReceived.onIntentResult(index, data);

            }
        }
    }
}

