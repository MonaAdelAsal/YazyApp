package com.asc.yazy.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.asc.yazy.R;
import com.asc.yazy.api.model.ModelDetails;
import com.asc.yazy.api.model.ModelTravelAgency;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.fragment.BookingControllerFragment;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.Constants;

public class BookingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Constants.changeStatusBarColor(this, R.color.blue);
        CheckLanguage checkLanguage = new CheckLanguage(BookingActivity.this);
        checkLanguage.UpdateLanguage();
        DataBindingUtil.setContentView(this, R.layout.activity_book);
        if (getIntent().hasExtra(Constants.OFFER) && getIntent().hasExtra(Constants.TRAVEL_AGENCY)) {
            ModelDetails offer = (ModelDetails) getIntent().getSerializableExtra(Constants.OFFER);
            String selectedDate = getIntent().getStringExtra(Constants.SELECTED_DATE);
            ModelTravelAgency travelAgency = (ModelTravelAgency) getIntent().getSerializableExtra(Constants.TRAVEL_AGENCY);
            assert offer != null;
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mainContent, new BookingControllerFragment(offer, travelAgency, selectedDate))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mainContent);
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
