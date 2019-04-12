package com.zjywidget.widget.roundelmenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class YRoundelMenu extends ViewGroup {

    private int mWidth;
    private int mHeight;
    private Paint mNarrowPaint;
    private AnimatorSet mExpandAnimatorSet;
    private ObjectAnimator mExpandXAnimator, mExpandYAnimator;

    public YRoundelMenu(Context context) {
        this(context, null);
    }

    public YRoundelMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YRoundelMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mNarrowPaint = new Paint();
        mNarrowPaint.setColor(Color.BLACK);
        mNarrowPaint.setAntiAlias(true);
        mNarrowPaint.setStyle(Paint.Style.FILL);

        mExpandAnimatorSet = new AnimatorSet();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth/2, mHeight/2, dp2px(20), mNarrowPaint);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mWidth = r - l;
        mHeight = b - t;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startExpandAnim();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void startExpandAnim(){
        mExpandXAnimator = new ObjectAnimator();
        mExpandXAnimator.setValues(PropertyValuesHolder.ofFloat("scaleX", 1f, 4f, 4.1f, 3.9f, 4.03f, 3.97f, 4f));
        mExpandXAnimator.setTarget(this);
        mExpandYAnimator = new ObjectAnimator();
        mExpandYAnimator.setValues(PropertyValuesHolder.ofFloat("scaleY", 1f, 4f, 4.1f, 3.9f, 4.03f, 3.97f, 4f));
        mExpandYAnimator.setTarget(this);

        mExpandAnimatorSet.playTogether(mExpandXAnimator, mExpandYAnimator);
        mExpandAnimatorSet.setDuration(1500);
        mExpandAnimatorSet.start();
    }

    /**
     * dp转px
     * @param dpVal   dp value
     * @return px value
     */
    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                getContext().getResources().getDisplayMetrics());
    }
}
