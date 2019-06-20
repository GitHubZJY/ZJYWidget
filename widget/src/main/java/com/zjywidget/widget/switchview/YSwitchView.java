package com.zjywidget.widget.switchview;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zjywidget.widget.R;

/**
 * Created by Yang on 2019/6/15.
 */

public class YSwitchView extends View {

    private final int DEFAULT_WIDTH = 200;

    private int mWidth, mHeight;
    private Path mBgPath, mExpandPath;
    private RectF mBgRectF, mExpandRectF;
    private Paint mBgPaint, mBarPaint;
    private int mBarX;

    private AnimatorSet mExpandAnimatorSet;
    private ValueAnimator mExpandAnimator;
    private ValueAnimator mBarAnimator;

    private int mStatus = 1;
    private final int SWITCH_ON = 1;
    private final int SWITCH_OFF = 0;

    private int mBarStrokeWidth = 3;
    private int mBarRadius;
    private int mOpenColor;
    private int mCloseColor;
    private int mScrollDuration;

    private boolean mIsBigBar;
    private int mBarOverDistance;


    public YSwitchView(Context context) {
        this(context, null);
    }

    public YSwitchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YSwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleStyleable(context, attrs, defStyleAttr);
        init();
    }

    private void handleStyleable(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.YSwitchView, defStyle, 0);
        try {
            mBarRadius = ta.getDimensionPixelSize(R.styleable.YSwitchView_switch_bar_radius, -1);
            mOpenColor = ta.getColor(R.styleable.YSwitchView_switch_open_color, Color.GREEN);
            mCloseColor = ta.getColor(R.styleable.YSwitchView_switch_close_color, Color.LTGRAY);
            mScrollDuration = ta.getInteger(R.styleable.YSwitchView_switch_duration, 300);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }
    }

    private void init() {
        mBgPath = new Path();
        mExpandPath = new Path();

        mBgRectF = new RectF();
        mExpandRectF = new RectF();

        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(Color.LTGRAY);

        mBarPaint = new Paint();
        mBarPaint.setStyle(Paint.Style.FILL);
        mBarPaint.setColor(Color.WHITE);

        mExpandAnimatorSet = new AnimatorSet();
        mExpandAnimator = new ValueAnimator();
        mBarAnimator = new ValueAnimator();

        mExpandAnimator.setFloatValues(1, 0.03f);
        mExpandAnimator.setInterpolator(new LinearInterpolator());
        mExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                updateExpandPath(value);
                invalidate();
            }
        });

        mBarAnimator.setInterpolator(new LinearInterpolator());
        mBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBarX = (int) animation.getAnimatedValue();
            }
        });

        mExpandAnimatorSet.playTogether(mExpandAnimator, mBarAnimator);
        mExpandAnimatorSet.setDuration(mScrollDuration);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width, height;
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        if (wMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = DEFAULT_WIDTH;
        }
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        if (hMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            //默认宽高比为0.6
            height = (int) (width * 0.6f);
        }
        if (height >= width) {
            throw new IllegalArgumentException("height can not bigger than width !");
        }
        mBarOverDistance = mBarRadius - height / 2;
        mIsBigBar = mBarOverDistance > 0;
        width = mIsBigBar ? width + mBarOverDistance * 2 : width;
        height = mIsBigBar ? height + mBarOverDistance * 2 : height;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        mHeight = bottom - top;

        if (mBarRadius == -1) {
            mBarRadius = mHeight / 2;
        }
        mBarAnimator.setIntValues(mWidth - mBarRadius, mBarRadius);
        mBarX = mWidth - mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        drawBg(canvas);
        //绘制开关过程中的白色伸缩动画
        drawAnim(canvas);
        //绘制开关小球
        drawBar(canvas);
    }

    /**
     * 绘制背景圆角矩形
     *
     * @param canvas
     */
    private void drawBg(Canvas canvas) {
        setBgRect(mBgRectF, 0, 0, mHeight, mHeight);
        mBgPaint.setColor(mStatus == SWITCH_ON ? mOpenColor : mCloseColor);
        mBgPath.arcTo(mBgRectF, 90, 180);
        setBgRect(mBgRectF, mWidth - mHeight, 0, mWidth, mHeight);
        mBgPath.arcTo(mBgRectF, 270, 180);
        mBgPath.close();
        canvas.drawPath(mBgPath, mBgPaint);
    }

    /**
     * 绘制切换开关过程中的变换矩形
     *
     * @param canvas
     */
    private void drawAnim(Canvas canvas) {
        mBgPaint.setColor(Color.WHITE);
        canvas.drawPath(mExpandPath, mBgPaint);
        mBgPaint.reset();
    }

    /**
     * 绘制开关小球
     *
     * @param canvas
     */
    private void drawBar(Canvas canvas) {
        mBarPaint.setColor(Color.LTGRAY);
        canvas.drawCircle(mBarX, mHeight / 2, mBarRadius, mBarPaint);
        mBarPaint.setColor(Color.WHITE);
        canvas.drawCircle(mBarX, mHeight / 2, mBarRadius - mBarStrokeWidth, mBarPaint);
    }

    /**
     * 设置矩形的范围
     *
     * @param rectF  矩形
     * @param left   左顶点
     * @param top    上顶点
     * @param right  右顶点
     * @param bottom 下顶点
     */
    private void setBgRect(RectF rectF, float left, float top, float right, float bottom) {
        rectF.left = mIsBigBar ? left + mBarOverDistance : left;
        rectF.top = mIsBigBar ? top + mBarOverDistance : top;
        rectF.right = mIsBigBar ? right - mBarOverDistance : right;
        rectF.bottom = mIsBigBar ? bottom - mBarOverDistance : bottom;
    }

    private void updateExpandPath(float progress){
        float yProgress = (1 - progress) * mHeight;
        float xProgress = progress * mWidth;
        mExpandPath.reset();
        setBgRect(mExpandRectF, xProgress / 2, progress * mHeight / 2, xProgress / 2 + yProgress, progress * mHeight / 2 + yProgress);
        mExpandPath.arcTo(mExpandRectF, 90, 180);
        setBgRect(mExpandRectF, mWidth - xProgress / 2 - yProgress, progress * mHeight / 2, mWidth - xProgress / 2, progress * mHeight / 2 + yProgress);
        mExpandPath.arcTo(mExpandRectF, 270, 180);
        mExpandPath.close();
    }

    /**
     * 设置当前开关状态
     * @param flag
     */
    public void setCheck(boolean flag){
        flag = false;
        mStatus = flag ? SWITCH_ON : SWITCH_OFF;
        if(flag){
            mBarX = mIsBigBar ? mWidth - mBarRadius : mWidth - mHeight / 2;
            updateExpandPath(1);
        }else{
            mBarX = mIsBigBar ? mBarRadius : mHeight / 2;
            updateExpandPath(0.03f);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                switch (mStatus) {
                    case SWITCH_OFF:
                        mExpandAnimator.setFloatValues(0.03f, 1);
                        mBarAnimator.setIntValues(mIsBigBar ? mBarRadius : mHeight / 2, mIsBigBar ? mWidth - mBarRadius : mWidth - mHeight / 2);
                        mStatus = SWITCH_ON;
                        break;
                    case SWITCH_ON:
                        mExpandAnimator.setFloatValues(1, 0.03f);
                        mBarAnimator.setIntValues(mIsBigBar ? mWidth - mBarRadius : mWidth - mHeight / 2, mIsBigBar ? mBarRadius : mHeight / 2);
                        mStatus = SWITCH_OFF;
                        break;
                    default:
                        break;
                }
                mExpandAnimatorSet.start();
                break;

        }
        return super.onTouchEvent(event);
    }
}
