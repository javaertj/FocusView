package com.ykbjson.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

/**
 * com.ykbjson.view.FloatView
 *
 * @author Kebin.Yan
 * @Description 焦点框视图
 * @date Create At :2015年7月2日 上午9:02:14
 */
public class FloatView extends ImageView {
    private final String TAG = getClass().getSimpleName();
    /**
     * x轴缩放率
     */
    private float scalX = 1.0f;
    /**
     * y轴缩放率
     */
    private float scalY = 1.0f;
    /**
     * 上一次view的x坐标
     */
    public float nFocusX;
    /**
     * 上一次view的y坐标
     */
    public float nFocusY;
    /**
     * 上一次view的宽度
     */
    public int nFocusW;
    /**
     * 上一次view的高度
     */
    public int nFocusH;

    public FloatView(Context context) {
        super(context);
    }

    /**
     * 改变当前视图位置和大小
     *
     * @param focusX 新的x坐标
     * @param focusY 新的y坐标
     * @param focusW 新的宽度
     * @param focusH 新的高度
     */
    public void onReLayout(final float focusX, final float focusY, final float focusW, final float focusH) {
        scalX = focusW / getWidth();
        scalY = focusH / getHeight();

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setObjectValues(new LayoutParams(nFocusW, nFocusH));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<LayoutParams>() {
            @Override
            public LayoutParams evaluate(float fraction, LayoutParams startValue, LayoutParams endValue) {
                Log.e(TAG, "evaluate , fraction = " + fraction);
                LayoutParams params = new LayoutParams(0, 0);
                params.width = (int) ((focusW - getWidth()) * fraction);
                params.height = (int) ((focusH - getHeight()) * fraction);
                return params;
            }
        });

        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LayoutParams nParams = (LayoutParams) animation.getAnimatedValue();
                LayoutParams params = (LayoutParams) getLayoutParams();
                params.width += nParams.width;
                params.height += nParams.height;
                setLayoutParams(params);
            }
        });

        valueAnimator.setStartDelay(250);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(this, "translationX", nFocusX, focusX), ObjectAnimator.ofFloat(this, "translationY", nFocusY, focusY), valueAnimator);
        set.setDuration(500);
        set.setTarget(this);
        set.start();

        nFocusX = focusX;
        nFocusY = focusY;
        nFocusW = (int) focusW;
        nFocusH = (int) focusH;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw " + " scalX : " + scalX + " scalY : " + scalY + " nFocusX : " + nFocusX + " nFocusY : " + nFocusY);
        super.onDraw(canvas);
    }
}
