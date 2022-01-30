package com.asc.yazy.activity;

import static com.asc.yazy.utils.Constants.newNotification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.asc.yazy.R;
import com.asc.yazy.adapter.MainContainerViewPagerAdapter;
import com.asc.yazy.api.model.ModelBooking;
import com.asc.yazy.api.model.ModelOffer;
import com.asc.yazy.api.pagination.PaginationProvider;
import com.asc.yazy.cash.UtilsPreference;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.core.GlobalInfoDialog;
import com.asc.yazy.core.RatingDialog;
import com.asc.yazy.core.RtlViewPager;
import com.asc.yazy.databinding.ActivityMainBinding;
import com.asc.yazy.fragment.FavoritesFragment;
import com.asc.yazy.fragment.MultiOfferDetailsFragment;
import com.asc.yazy.fragment.MyTripsPagerFragment;
import com.asc.yazy.fragment.NewTripDetailsFragment;
import com.asc.yazy.fragment.OfferDetailsFragment;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.interfaces.IUpdatableFragment;
import com.asc.yazy.service.ServiceFireBaseMessaging;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.messaging.FirebaseMessaging;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Objects;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnBackStackChangedListener {


    private static final String TAG = "MainActivity";
    public static MainContainerViewPagerAdapter mainContainerViewPagerAdapter;
    @SuppressLint("StaticFieldLeak")
    public static TextView NotificationCount;
    private static RtlViewPager container;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (newNotification != 0) {
                NotificationCount.setVisibility(View.VISIBLE);
                if (NotificationCount.getText() == null || NotificationCount.getText().toString().isEmpty()) {
                    int NewCount = 1;
                    Log.d("MainActivityNo", "onReceive: null");
                    NotificationCount.setText(("" + NewCount));
                    newNotification = 0;
                } else {
                    int count = Integer.parseInt(NotificationCount.getText().toString());
                    int NewCount = count + 1;
                    Log.d("MainActivityNo", "onReceive: " + count);
                    NotificationCount.setText(("" + NewCount));
                    newNotification = 0;
                }

            }
        }
    };
    boolean doubleBackToExitPressedOnce = false;
    private ActivityMainBinding binding;
    private final ViewPager.OnPageChangeListener onPagerChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onPageSelected(int position) {
            //  RefreshNotificationCount();
            updateBottomNavigation();

            Log.d(TAG, "onPageSelected: position " + position);
            switch (position) {
                case 0:
                    binding.bottomNavigation.lineHome.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    binding.bottomNavigation.lineSearch.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    binding.bottomNavigation.lineProfile.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    binding.bottomNavigation.lineNotifications.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    binding.bottomNavigation.lineSettings.setVisibility(View.VISIBLE);
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private final KeyboardVisibilityEventListener keyboardVisibilityEventListener = new KeyboardVisibilityEventListener() {
        @Override
        public void onVisibilityChanged(boolean isOpen) {
            if (isOpen) {
                binding.bottomNavigation.getRoot().setVisibility(View.GONE);
            } else {
                binding.bottomNavigation.getRoot().setVisibility(View.VISIBLE);
            }
        }
    };
    private InputMethodManager keyboard;
    private GuideView caseView;
    AnimatorListenerAdapter onPlaneAnimationEnds = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            binding.likeAnimation.animate().alpha(0.0f);
            Constants.changeStatusBarColor(MainActivity.this, R.color.white);
            introViewBehaviour();

        }
    };

    public static void updateBottomNavigation(int i) {

        container.setCurrentItem(i);
    }

    @Override
    public void onBackStackChanged() {
        assert keyboard != null;
        keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fullContent);
        if (fragment instanceof IUpdatableFragment) {
            ((IUpdatableFragment) fragment).onFragmentUpdate();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Constants.changeStatusBarColor(this, R.color.blue);
        CheckLanguage checkLanguage = new CheckLanguage(MainActivity.this);
        checkLanguage.UpdateLanguage();
        initServices();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(Constants.TRANSACTION_NOTIFICATION_ID));
        Constants.changeStatusBarColor(this, R.color.blue);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        container = binding.mainContent;
        PaginationProvider.USER_TOKEN = UtilsPreference.getInstance(this).getUser().getAccess_token();

        binding.likeAnimation.playAnimation();
        binding.likeAnimation.addAnimatorListener(onPlaneAnimationEnds);

        binding.bottomNavigation.layoutHome.setOnClickListener(this);
        binding.bottomNavigation.layoutSearch.setOnClickListener(this);
        binding.bottomNavigation.layoutProfile.setOnClickListener(this);
        binding.bottomNavigation.layoutSettings.setOnClickListener(this);
        binding.bottomNavigation.layoutNotifications.setOnClickListener(this);
        NotificationCount = binding.bottomNavigation.tvNotificationsCount;
        if (UtilsPreference.getInstance(this).getUser().getAccess_token() == null) {
            NotificationCount.setVisibility(View.GONE);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        binding.mainContent.setOnPageChangeListener(onPagerChangeListener);

        keyboard = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        KeyboardVisibilityEvent.setEventListener(this, keyboardVisibilityEventListener);
        new SetUpViewPagerTask().execute();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        handelNotificationNavigation(getIntent());
        notifyToPendingBookings();
        CheckIfHasDynamicLink(getIntent());

    }

    private void quickAccessIntent(Intent intent) {
        try {

            switch (Objects.requireNonNull(intent.getAction())) {
                case Constants.SEARCH_ACTION:
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_SEARCH);
                    binding.mainContent.setCurrentItem(1);
                    break;
                case Constants.TRIPS_ACTION:
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_TRIPS);
                    Objects.requireNonNull(this).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new MyTripsPagerFragment())
                            .addToBackStack(null)
                            .commit();
                    break;
                case Constants.FAV_ACTION:
                    AnalyticsUtility.logAction(AnalyticsUtility.Actions.PROFILE_FAVORITE);
                    Objects.requireNonNull(this).getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                            .add(R.id.fullContent, new FavoritesFragment())
                            .addToBackStack(null)
                            .commit();
                    break;
                default:

            }

        } catch (Exception e) {
            Log.d(TAG, "quickAccessIntent: error " + e.getMessage());
        }
    }

    private void notifyToPendingBookings() {

        if (UtilsPreference.getInstance(this).getPreference(Constants.IS_BOOKINGS_PENDING, false)) {
            binding.bottomNavigation.imgNotify.setVisibility(View.VISIBLE);
            binding.bottomNavigation.pulsator.setColor(getResources().getColor(R.color.colorAccent));
            binding.bottomNavigation.pulsator.start();
        } else {
            binding.bottomNavigation.imgNotify.setVisibility(View.GONE);
            binding.bottomNavigation.pulsator.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyToPendingBookings();
        // CheckIfHasDynamicLink();
    }

    private void introViewBehaviour() {

        boolean firstRun = UtilsPreference.getInstance(this).getPreference(Constants.IS_HOME_FIRST_RUN, true);

        if (!firstRun)
            return;

        caseView = new GuideView.Builder(this)
                .setTitle(Objects.requireNonNull(this).getResources().getString(R.string.intro_search))
                .setContentText(Objects.requireNonNull(this).getResources().getString(R.string.intro_search_details))
                .setTargetView(binding.bottomNavigation.layoutSearch)
                .setGravity(Gravity.center)//optional
                .setDismissType(DismissType.anywhere) //optional - default dismissible by TargetView
                .build();
        caseView.show();
        UtilsPreference.getInstance(this).savePreference(Constants.IS_HOME_FIRST_RUN, false);


    }

    private void initServices() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        Intent myService = new Intent(this, ServiceFireBaseMessaging.class);
        startService(myService);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: " + intent.getAction());
        handelNotificationNavigation(intent);
        CheckIfHasDynamicLink(intent);
    }

    private void handelNotificationNavigation(Intent intent) {

        if (intent == null)
            return;
        final String type = intent.getStringExtra(Constants.TRANSACTION_TYPE);
        final String targetID = intent.getStringExtra(Constants.TRANSACTION_TARGET_ID);
        final String notificationID = intent.getStringExtra(Constants.TRANSACTION_NOTIFICATION_ID);
        final String title = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
        final String body = intent.getStringExtra(Constants.NOTIFICATION_BODY);

        if (type == null)
            return;
        switch (type) {
            case "5":
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                binding.mainContent.setCurrentItem(3);
                break;
            case "2":
            case "6":
                GlobalInfoDialog.getInstance(this).setTitle(getString(R.string.app_name)).setMessage(body).show();
                break;
            case "1":
                ModelOffer offer = new ModelOffer();
                if (targetID == null)
                    return;
                offer.setId(targetID);
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.NOTIFICATION_OPEN_OFFER);
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new OfferDetailsFragment(offer))
                        .addToBackStack("OfferDetailsFragment")
                        .commit();

                break;
            case "3":
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.NOTIFICATION_OPEN_RATE);
                if (notificationID == null)
                    return;
                assert targetID != null;
                RatingDialog.getInstance(this).setNotificationID(Integer.parseInt(targetID)).show();
                break;
            case "4":
                ModelBooking trip = new ModelBooking();
                trip.setBooking_id(targetID);
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_BOOK_DETAILS);
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                        .add(R.id.fullContent, new NewTripDetailsFragment(trip))
                        .addToBackStack("OfferDetailsFragment")
                        .commit();
                break;
        }
    }

    private void CheckIfHasDynamicLink(Intent intent) {

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent != null ? intent : Objects.requireNonNull(this).getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    Log.d(TAG, "onSuccess: ");

                    if (pendingDynamicLinkData != null && pendingDynamicLinkData.getLink() != null) {
                        Log.d("SplashActivityLogger", "CheckIfHasDynamicLink: " + pendingDynamicLinkData.getLink());
                        String payload = pendingDynamicLinkData.getLink().getQueryParameter("payload");
                        if (payload == null || payload.split("-").length != 2)
                            return;
                        String offerId = payload.split("-")[0];
                        String type = payload.split("-")[1];
                        ModelOffer offer = new ModelOffer();
                        offer.setId(offerId);
                        if (type.equals("0")) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                                    .add(R.id.fullContent, new OfferDetailsFragment(offer))
                                    .addToBackStack("OfferDetailsFragment")
                                    .commit();
                        } else if (type.equals("1")) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right, R.anim.exit_to_right)
                                    .add(R.id.fullContent, new MultiOfferDetailsFragment(offer))
                                    .addToBackStack("OfferDetailsFragment")
                                    .commit();
                        }
                    } else {
                        Log.d(TAG, "DetailID" + "null");
                        //
                    }

                })
                .addOnFailureListener(this, e -> Log.w(TAG, "getDynamicLink:onFailure", e));
    }


    @SuppressLint("SetTextI18n")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (caseView != null && caseView.isShowing()) {
            caseView.dismiss();
            return false;
        }


        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //RefreshNotificationCount();
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fullContent);
            if (fragment instanceof IFragment) {
                ((IFragment) fragment).onBack();
                return false;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.mainContent.getCurrentItem() > 0) {
            updateBottomNavigation();
            binding.mainContent.setCurrentItem(0);
            /*
            if (id.length() == 0) {
                binding.mainContent.setCurrentItem(0);
            }
            */
            binding.bottomNavigation.lineHome.setVisibility(View.VISIBLE);
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                return false;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.prease_back, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v == null)
            return;

        switch (v.getId()) {
            case R.id.layoutHome:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_HOME);
                binding.mainContent.setCurrentItem(0);
                break;

            case R.id.layoutSearch:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_SEARCH);
                binding.mainContent.setCurrentItem(1);
                break;

            case R.id.layoutProfile:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_PROFILE);
                binding.mainContent.setCurrentItem(2);
                break;

            case R.id.layoutNotifications:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_NOTIFICATIONS);
                binding.mainContent.setCurrentItem(3);
                break;

            case R.id.layoutSettings:
                AnalyticsUtility.logAction(AnalyticsUtility.Actions.OPEN_MENU);
                binding.mainContent.setCurrentItem(4);
                break;
        }
    }

    private void updateBottomNavigation() {

        binding.bottomNavigation.lineHome.setVisibility(View.INVISIBLE);
        binding.bottomNavigation.lineSearch.setVisibility(View.INVISIBLE);
        binding.bottomNavigation.lineProfile.setVisibility(View.INVISIBLE);
        binding.bottomNavigation.lineNotifications.setVisibility(View.INVISIBLE);
        binding.bottomNavigation.lineSettings.setVisibility(View.INVISIBLE);

    }

    @SuppressLint("StaticFieldLeak")
    private class SetUpViewPagerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            mainContainerViewPagerAdapter = new MainContainerViewPagerAdapter(getSupportFragmentManager());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            binding.mainContent.setOffscreenPageLimit(5);
            binding.mainContent.setAdapter(mainContainerViewPagerAdapter);
            binding.mainContent.setCurrentItem(0);
            updateBottomNavigation();
            binding.bottomNavigation.lineHome.setVisibility(View.VISIBLE);
            if (Constants.id != null && !Constants.id.isEmpty()) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new NewTripDetailsFragment(Constants.id)).commit();
            }
            quickAccessIntent(getIntent());


        }
    }


}
