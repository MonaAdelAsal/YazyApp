package com.asc.yazy.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.fragment.MyTripsPagerFragment;
import com.asc.yazy.interfaces.IFragment;

public class TripsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Constants.changeStatusBarColor(this, R.color.blue);
        CheckLanguage checkLanguage = new CheckLanguage(TripsActivity.this);
        checkLanguage.UpdateLanguage();
        DataBindingUtil.setContentView(this, R.layout.activity_book);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fullContent, new MyTripsPagerFragment())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fullContent);
            if (fragment instanceof IFragment) {
                ((IFragment) fragment).onBack();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    return true;
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

}
