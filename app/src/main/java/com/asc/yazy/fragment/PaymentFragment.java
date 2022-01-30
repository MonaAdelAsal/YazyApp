package com.asc.yazy.fragment;

import android.animation.Animator;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieDrawable;
import com.asc.yazy.R;
import com.asc.yazy.core.CheckLanguage;
import com.asc.yazy.databinding.FragmentConfrimBookBinding;
import com.asc.yazy.interfaces.IFragment;
import com.asc.yazy.utils.AnalyticsUtility;
import com.asc.yazy.utils.Constants;

public class PaymentFragment extends Fragment implements View.OnClickListener, IFragment {

    private final static int animationDuration = 400;
    private long mLastClickTime = System.currentTimeMillis();
    private FragmentConfrimBookBinding binding;
    private TransitionDrawable transition;

    public PaymentFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confrim_book, container, false);
        binding.btnHome.setOnClickListener(this);
        AnalyticsUtility.logEvent(AnalyticsUtility.Events.PAYMENT_CONFIRMATION, AnalyticsUtility.Events.PAYMENT_CONFIRMATION);
        AnalyticsUtility.setScreen(this);
        CheckLanguage checkLanguage = new CheckLanguage(getActivity());
        checkLanguage.UpdateLanguage();
        transition = (TransitionDrawable) binding.layoutBackGround.getBackground();
        waiting();
        Constants.changeStatusBarColor(PaymentFragment.this.getActivity(), R.color.greyblue);
        binding.btnHome.setOnClickListener(v -> {
            Constants.changeStatusBarColor(PaymentFragment.this.getActivity(), R.color.greyblue);
            transition.resetTransition();
            accepted();
        });
        binding.btnTryAgain.setOnClickListener(v -> {
            Constants.changeStatusBarColor(PaymentFragment.this.getActivity(), R.color.red);
            transition.resetTransition();
            transition.startTransition(animationDuration);
            rejected();
        });
        return binding.getRoot();
    }


    private void waiting() {
        binding.tvConfrim.setVisibility(View.INVISIBLE);
        binding.tvMessage.setVisibility(View.INVISIBLE);
        binding.tvTransactionId.setVisibility(View.INVISIBLE);
        binding.tvTransactionIdValue.setVisibility(View.INVISIBLE);
        binding.loadingAnimation.setMinAndMaxFrame(0, 120);
        binding.loadingAnimation.playAnimation();
        binding.loadingAnimation.setRepeatMode(LottieDrawable.RESTART);
        binding.loadingAnimation.setRepeatCount(1000);
    }

    private void accepted() {
        binding.loadingAnimation.setMinAndMaxFrame(220, 400);
        binding.loadingAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                binding.loadingAnimation.pauseAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


    }

    private void rejected() {
        binding.loadingAnimation.setMinAndMaxFrame(657, 823);
        binding.loadingAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                binding.loadingAnimation.pauseAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


    }


    @Override
    public void onClick(View v) {
        if (v == null)
            return;
        long now = System.currentTimeMillis();
        if (now - mLastClickTime < Constants.CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;
        if (v == binding.btnHome){
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onBack() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }
}
