package com.zjywidget.widget.banner.indicator;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 指示器基类，封装指示器切换逻辑
 */
public abstract class BaseIndicator extends LinearLayout implements Indicator {

    private int mCellCount;
    private int mCurrentPos;
    private List<IndicatorCell> mCellViews;

    public BaseIndicator(Context context) {
        super(context);
        init();
    }

    public void init() {
        mCellViews = new ArrayList<>();
    }

    @Override
    public void setCellCount(int cellCount) {
        mCellCount = cellCount;
        int i = 1;
        while (i <= mCellCount) {
            IndicatorCell view = getCellView();
            mCellViews.add(view);
            addView(view);
            i++;
        }
    }

    @Override
    public void setCurrentPosition(int currentPosition) {
        mCurrentPos = currentPosition;
        invalidateCell();
    }


    protected abstract IndicatorCell getCellView();

    /**
     * 指示器小圆点半径
     */
    protected abstract float getCellWidth();

    /**
     * 指示器小圆点间距
     */
    protected abstract float getCellMargin();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 重新测量当前界面的宽度
        float width = getPaddingLeft() + getPaddingRight() + getCellMargin() * mCellCount + getCellMargin() * (mCellCount - 1);
        float height = getPaddingTop() + getPaddingBottom() + getCellWidth();
        width = resolveSize((int) width, widthMeasureSpec);
        height = resolveSize((int) height, heightMeasureSpec);
        setMeasuredDimension((int) width, (int) height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        invalidateCell();
    }

    public void invalidateCell() {
        for (int i = 0; i < mCellCount; i++) {
            IndicatorCell view = mCellViews.get(i);
            float left = i * getCellWidth() + getCellMargin() * i;
            //float right = (i + 1) * getCellWidth() + getCellMargin() * i;
            view.setLeft((int) left);
            view.setTop(getHeight() / 2 - (int) getCellWidth() / 2);
            view.getLayoutParams().width = (int) getCellWidth();
            view.getLayoutParams().height = (int) getCellWidth();
            if (i == mCurrentPos) {
                view.select();
            } else {
                view.unSelect();
            }
            view.invalidate();
        }
        invalidate();
    }

    /**
     * dp转px
     *
     * @param dpVal dp value
     * @return px value
     */
    protected int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                getContext().getResources().getDisplayMetrics());
    }
}
