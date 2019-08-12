package com.zhn.myalbum.widget.splash;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义View组件
 */
public class MyColorAnimationView extends View implements
        ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private static final int RED = 0xffFF8080;
    private static final int BLUE = 0xff8080FF;
    private static final int WHITE = 0xffffffff;
    private static final int GREEN = 0xff80ff80;
    private static final int L_BLUE = 0xff31696A;
    private static final int DURATION = 3000;
    ValueAnimator colorAnim = null;

    /**
     * 回调 回调
     */

    private PageChangeListener mPageChangeListener;
    private TouchListener mTouchListener;

    ViewPager.OnPageChangeListener mOnPageChangeListener;
    ViewPager.OnTouchListener mOnTouchListener;

    /**
     * setter
     */

    public void setmOnPageChangeListener(
            ViewPager.OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }

    public void setOnTouchListener(
            ViewPager.OnTouchListener onTouchListener) {
        this.mOnTouchListener = onTouchListener;
    }

    /**
     * 这是你唯一需要关心的方法
     *
     * @param mViewPager
     *            你必须在设置 Viewpager 的 Adapter 这后，才能调用这个方法。
     * @param obj
     *            ,这个obj实现了 ColorAnimationView.OnPageChangeListener ，实现回调
     * @param count
     *            ,viewpager孩子的数量
     * @param colors
     *            int... colors ，你需要设置的颜色变化值~~ 如何你传人 空，那么触发默认设置的颜色动画
     * */
    /**
     * This is the only method you need care about.
     *
     * @param mViewPager ,you need set the adpater before you call this.
     * @param count      ,this param set the count of the viewpaper's child
     * @param colors     ,this param set the change color use (int... colors), so,you
     *                   could set any length if you want.And by default. if you set
     *                   nothing , don't worry i have already creat a default good
     *                   change color!
     */
    public void setmViewPager(ViewPager mViewPager, int count, int... colors) {
        // this.mViewPager = mViewPager;
        if (mViewPager.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        mPageChangeListener.setViewPagerChildCount(count);
        //设置监听
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        mViewPager.setOnTouchListener(mTouchListener);

        if (colors.length == 0) {
            createDefaultAnimation();
        } else {
            createAnimation(colors);
        }

    }

    /**
     * 三个构造函数
     */
    public MyColorAnimationView(Context context) {
        this(context, null, 0);
    }

    public MyColorAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyColorAnimationView(Context context,
                                AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPageChangeListener = new PageChangeListener();
        mTouchListener = new TouchListener();
    }


    private void seek(long seekTime) {
        if (colorAnim == null) {
            createDefaultAnimation();
        }
        colorAnim.setCurrentPlayTime(seekTime);
    }

    private void createAnimation(int... colors) {
        if (colorAnim == null) {
            colorAnim = ObjectAnimator.ofInt(this, "backgroundColor", colors);
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.setDuration(DURATION);
            colorAnim.addUpdateListener(this);
        }
    }

    private void createDefaultAnimation() {
        colorAnim = ObjectAnimator.ofInt(this, "backgroundColor",  RED,WHITE,
                BLUE, GREEN, L_BLUE);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setDuration(DURATION);
        colorAnim.addUpdateListener(this);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidate();
        // long playtime = colorAnim.getCurrentPlayTime();
    }

    //用于回调
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        //数量
        private int viewPagerChildCount;

        //getter setter
        public void setViewPagerChildCount(int viewPagerChildCount) {
            this.viewPagerChildCount = viewPagerChildCount;
        }

        public int getViewPagerChildCount() {
            return viewPagerChildCount;
        }

        //实现接口的方法
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

            int count = getViewPagerChildCount() - 1;
            if (count != 0) {
                float length = (position + positionOffset) / count;
                int progress = (int) (length * DURATION);
                MyColorAnimationView.this.seek(progress);
            }
            // call the method by default
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }

        }

        @Override
        public void onPageSelected(int position) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }

    private class TouchListener implements ViewPager.OnTouchListener {

        public TouchListener() {
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (mOnTouchListener != null) {
            }
            mOnTouchListener.onTouch(view, motionEvent);
            return false;
        }
    }
}
