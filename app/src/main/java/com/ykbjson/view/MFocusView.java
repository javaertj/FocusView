package com.ykbjson.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

/**
 * com.ykbjson.view.MFocusView
 *
 * @author Kebin.Yan
 * @Description 子view获得焦点时焦点框有动画效果的视图容器
 * @date Create At :2015年7月2日 上午9:01:37
 */
public class MFocusView extends RelativeLayout implements OnFocusChangeListener, OnClickListener {
    private final String TAG = getClass().getSimpleName();
    /**
     * 焦点框
     */
    private FloatView floatView;
    /**
     * 是否初始化过
     */
    private boolean isInit;

    public MFocusView(Context context) {
        this(context, null);
    }

    public MFocusView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public MFocusView(Context context, AttributeSet attr, int style) {
        super(context, attr, style);
    }

    /**
     * 初始化焦点框
     */
    private void initFloatView() {
        int firstChildwidth = -1;
        int firstChildHeight = -1;
        View child = getChildAt(0);
        if (null != child) {
            firstChildwidth = (int) (child.getRight() - child.getLeft());
            firstChildHeight = (int) (child.getBottom() - child.getTop());
        }
        floatView = new FloatView(getContext());
        LayoutParams floatParams = new LayoutParams(firstChildwidth, firstChildHeight);
        floatView.setId(4);
        floatView.setLayoutParams(floatParams);
        floatView.setFocusable(false);
        floatView.setFocusableInTouchMode(false);
        floatView.setScaleType(ScaleType.FIT_XY);
        floatView.setImageResource(R.drawable.focus_bound);
        addView(floatView);
        if (null != child)
            floatView.onReLayout(child.getLeft(), child.getTop(), firstChildwidth, firstChildHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !isInit) {
            isInit = !isInit;
            if (getChildCount() > 0) {
                Log.e(TAG, "onLayout,childCount :  " + getChildCount());
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).setOnClickListener(this);
                    getChildAt(i).setOnFocusChangeListener(this);
                }
            }
            initFloatView();
        }
    }

    @Override
    public void onClick(View child) {
        onChildChange(child);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        Log.e(TAG, "onFocusChanged,gainFocus :  " + gainFocus + " direction : " + direction);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        onChildChange();
    }

    @Override
    public void onFocusChange(View child, boolean hasFocus) {
        Log.e(TAG, "onFocusChanged,hasFocus :  " + hasFocus);
        if (!hasFocus)
            return;
        onChildChange(child);
    }

    /**
     * 焦点框视图改变
     */
    private void onChildChange() {
        View child = findFocus();
        onChildChange(child);
    }

    /**
     * 焦点框视图改变
     *
     * @param child 当前右焦有的视图
     */
    private void onChildChange(View child) {
        if (null == child)
            return;

        float focusX = child.getLeft();
        float focusY = child.getTop();
        float focusW = child.getRight() - focusX;
        float focusH = child.getBottom() - focusY;

        floatView.onReLayout(focusX, focusY, focusW, focusH);
        Log.e(TAG, "onChildChange,child : " + child);
    }

}

