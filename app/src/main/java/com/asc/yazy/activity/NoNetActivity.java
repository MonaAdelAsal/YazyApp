package com.asc.yazy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.asc.yazy.R;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.fragment.NoNetFragment;
import com.asc.yazy.interfaces.IUpdatableFragment;

public class NoNetActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {


    private InputMethodManager keyboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Constants.changeStatusBarColor(this, R.color.white);
        CheckLanguage checkLanguage = new CheckLanguage(NoNetActivity.this);
        checkLanguage.UpdateLanguage();
        DataBindingUtil.setContentView(this, R.layout.activity_authentication);
        Intent intent = getIntent();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        keyboard = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new NoNetFragment()).commit();
    }


    @Override
    public void onBackStackChanged() {

        assert keyboard != null;
        keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof IUpdatableFragment) {
            ((IUpdatableFragment) fragment).onFragmentUpdate();
        }
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

