package com.asc.yazy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.asc.yazy.R;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.fragment.FavoritesFragment;
import com.asc.yazy.fragment.MyTripsPagerFragment;
import com.asc.yazy.fragment.SearchFragment;
import com.asc.yazy.interfaces.IFragmentAccess;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

import java.util.Objects;

public class QuickKAccessActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {


    private static final String TAG = "QuicKAccessActivity";
    private InputMethodManager keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    Constants.changeStatusBarColor(this, R.color.white);
        CheckLanguage checkLanguage = new CheckLanguage(QuickKAccessActivity.this);
        checkLanguage.UpdateLanguage();
        DataBindingUtil.setContentView(this, R.layout.activity_access);
        keyboard = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        quickAccessIntent(getIntent());
        getSupportFragmentManager().addOnBackStackChangedListener(this);

    }

    private void quickAccessIntent(Intent intent) {
        try {

            switch (Objects.requireNonNull(intent.getAction())) {
                case Constants.SEARCH_ACTION:
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.SEARCH);
                    Objects.requireNonNull(this).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new SearchFragment())
                            .addToBackStack("SearchFragment")
                            .commit();
                    break;
                case Constants.TRIPS_ACTION:
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_TRIPS);
                    Objects.requireNonNull(this).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new MyTripsPagerFragment())
                            .addToBackStack("MyTripsPagerFragment")
                            .commit();
                    break;
                case Constants.FAV_ACTION:
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_FAVORITE);
                    Objects.requireNonNull(this).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new FavoritesFragment())
                            .addToBackStack("FavoritesFragment")
                            .commit();
                    break;
                default:
                    Intent intentHome = new Intent(this, MainActivity.class);
                    startActivity(intentHome);
                    break;

            }

        } catch (Exception e) {
            Log.d(TAG, "quickAccessIntent: error " + e.getMessage());
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fullContent);
            if (fragment instanceof IFragmentAccess) {
                if (((IFragmentAccess) fragment).onBackAccess())
                    finish();
                else
                    return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackStackChanged() {
        assert keyboard != null;
        keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

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
