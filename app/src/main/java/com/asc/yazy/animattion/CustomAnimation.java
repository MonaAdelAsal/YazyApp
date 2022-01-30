package com.asc.yazy.animattion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;


public class CustomAnimation {


    public static void scaleOutWithPulse(final View view, final int delay, final int duration) {


        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                view,
                PropertyValuesHolder.ofFloat("scaleX", 1, 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1, 1.1f));
        scaleDown.setDuration(duration);
        scaleDown.setStartDelay(delay);
        scaleDown.start();
        view.setVisibility(View.INVISIBLE);

        scaleDown.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(
                        view,
                        PropertyValuesHolder.ofFloat("scaleX", 0.9f),
                        PropertyValuesHolder.ofFloat("scaleY", 0.9f));
                scale.setDuration(150);
                scale.start();

                scale.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                                view,
                                PropertyValuesHolder.ofFloat("scaleX", 1f),
                                PropertyValuesHolder.ofFloat("scaleY", 1f));
                        scaleDown.setDuration(250);
                        scaleDown.start();
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        /*
        Animation animation = new Animation() {

            private int startWidth = 0;
            private int startHeight = 0;
            private int endW;
            private int endH;

            @Override
            public void initialize(int width, int height, int parentWidth, int parentHeight) {
                super.initialize(width, height, parentWidth, parentHeight);
                setDuration(duration);
                setStartOffset(delay);
                endW = view.getWidth();
                endH = view.getHeight();


            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                view.getLayoutParams().width = (int) (startWidth + (interpolatedTime * (endW - startWidth)));
                view.getLayoutParams().height = (int) (startHeight + (interpolatedTime * (endH - startHeight)));
                view.requestLayout();


            }


        };
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

               // view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                        view,
                        PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.1f));
                scaleDown.setDuration(100);
                scaleDown.start();

                scaleDown.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(
                                view,
                                PropertyValuesHolder.ofFloat("scaleX",  0.9f),
                                PropertyValuesHolder.ofFloat("scaleY",  0.9f));
                        scale.setDuration(200);
                        scale.start();

                        scale.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                                        view,
                                        PropertyValuesHolder.ofFloat("scaleX", 1f),
                                        PropertyValuesHolder.ofFloat("scaleY", 1f));
                                scaleDown.setDuration(300);
                                scaleDown.start();
                            }
                        });
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
            public void onAnimationRepeat(Animation animation) {

            }
        });

*/
        // view.startAnimation(animation);

    }


}
