package com.zjywidget.widget.banner.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.View;

public abstract class BaseIndicator extends View implements Indicator {

    private Paint mPaint;
    private int mCellCount;
    private int mCurrentPos;

    public BaseIndicator(Context context) {
        super(context);
        init();
    }

    public void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void setCellCount(int cellCount) {
        mCellCount = cellCount;
    }

    @Override
    public void setCurrentPosition(int currentPosition) {
        mCurrentPos = currentPosition;
        invalidate();
    }

    /**
     * 指示器小圆点半径
     */
    public abstract int getCellRadius();

    /**
     * 指示器小圆点间距
     */
    public abstract int getCellMargin();

    /**
     * 指示器小圆点激活状态的颜色
     */
    public abstract int getSelectColor();

    /**
     * 指示器小圆点未激活状态的颜色
     */
    public abstract int getUnSelectColor();

    /**
     * 指示器类型
     */
    public abstract int getType();


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 重新测量当前界面的宽度
        int width = getPaddingLeft() + getPaddingRight() + getCellMargin() * 2 * mCellCount + getCellMargin() * (mCellCount - 1);
        int height = getPaddingTop() + getPaddingBottom() + getCellRadius() * 2;
        width = resolveSize(width, widthMeasureSpec);
        height = resolveSize(height, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mCellCount; i++) {
            if (i == mCurrentPos) {
                mPaint.setColor(getSelectColor());
            } else {
                mPaint.setColor(getUnSelectColor());
            }
            int left = getPaddingLeft() + i * getCellRadius() * 2 + getCellMargin() * i;
            int right = getPaddingLeft() + (i + 1) * getCellRadius() * 2 + getCellMargin() * i;
            switch (getType()) {
                case CIRCLE:
                    canvas.drawCircle(left + getCellRadius(), getHeight() / 2, getCellRadius(), mPaint);
                    break;
                case RECTANGLE:
                    canvas.drawRect(left, getHeight() / 2 - getCellRadius(), right, getHeight() / 2 + getCellRadius(), mPaint);
                    break;
            }
        }
    }

    /**
     * dp转px
     *
     * @param dpVal dp value
     * @return px value
     */
    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                getContext().getResources().getDisplayMetrics());
    }
}
