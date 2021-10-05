package com.zjywidget.widget.indicator;



import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.zjywidget.widget.R;


/**
 * <b>Project:</b> https://github.com/GitHubZJY/ZJYWidget <br>
 * <b>Create Date:</b> 2021/09/23 <br>
 * <b>@author:</b> Yang <br>
 * <b>Description:</b> 可滚动的粘性小圆点指示器 <br>
 */
public class YStickIndicator extends View {

    /**
     * 动画时长
     */
    private long animTime;
    /**
     * 最大展示数量（当前总数超过最大展示数量时，自动滑动整组小圆点）
     */
    private int showCount;
    /**
     * 小圆点之间的间距
     */
    private float dotPadding;
    /**
     * 当前选中的小圆点的宽度
     */
    private float bigDotWidth;
    /**
     * 默认小圆点的宽度
     */
    private float smallDotWidth;
    /**
     * 当前选中的小圆点的颜色
     */
    private int selectColor;
    /**
     * 默认小圆点的颜色
     */
    private int defaultColor;
    /**
     * 是否开启粘性动画
     */
    private boolean isStickEnable;

    private int curIndex;
    private int totalCount;
    private float lastX;
    private float curX;

    private Paint paint;
    private RectF selectRectF;

    private ValueAnimator scrollAnimator;

    private Path stickPath;
    private ValueAnimator stickAnimator;
    private float stickAnimX;
    private boolean isSwitchFinish = true;

    public YStickIndicator(Context context) {
        this(context, null);
    }

    public YStickIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YStickIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleStyleable(context, attrs);
        init();
    }

    private void handleStyleable(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.YStickIndicator, 0, 0);
        try {
            animTime = ta.getInteger(R.styleable.YStickIndicator_stick_indicator_anim_time, 500);
            dotPadding = ta.getDimension(R.styleable.YStickIndicator_stick_indicator_dot_padding, dpToPx(5));
            smallDotWidth = ta.getDimension(R.styleable.YStickIndicator_stick_indicator_small_dot_width, dpToPx(5));
            bigDotWidth = ta.getDimension(R.styleable.YStickIndicator_stick_indicator_big_dot_width, dpToPx(6));
            selectColor = ta.getColor(R.styleable.YStickIndicator_stick_indicator_select_color, Color.WHITE);
            defaultColor = ta.getColor(R.styleable.YStickIndicator_stick_indicator_default_color, Color.parseColor("#F6DCC7"));
            showCount = ta.getInteger(R.styleable.YStickIndicator_stick_indicator_show_count, 5);
            isStickEnable = ta.getBoolean(R.styleable.YStickIndicator_stick_indicator_stick_enable, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }
    }

    private void init() {
        selectRectF = new RectF();
        paint = new Paint();
        scrollAnimator = new ValueAnimator();
        scrollAnimator.setDuration(animTime);
        scrollAnimator.addUpdateListener(animation -> {
            curX = (float) animation.getAnimatedValue();
            invalidate();
        });

        stickAnimator = new ValueAnimator();
        stickAnimator.setDuration(animTime);
        stickAnimator.addUpdateListener(animation -> {
            stickAnimX = (float) animation.getAnimatedValue();
            invalidate();
        });
        stickPath = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = Math.min(totalCount, showCount);
        int width = (int) ((count - 1) * smallDotWidth + (count - 1) * dotPadding + bigDotWidth);
        int height = (int) bigDotWidth;
        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSpecSize = MeasureSpec.getMode(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, height);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, height);
        } else {
            setMeasuredDimension(width, height);
        }

        requestLayout();
    }


    public void setCurIndex(int index) {
        if (index == curIndex) {
            return;
        }
        //当前滑动到的新index 处于左右两边2个之外的区域
        if (totalCount > showCount) {
            if (index > curIndex) {
                //往左边滑动
                //判断是否需要先滚动
                int start = showCount % 2 == 0 ? showCount/2 - 1 : showCount / 2;
                int end = totalCount - showCount / 2;
                if (index > start && index < end) {
                    startScrollAnim(Duration.LEFT, () -> invalidateIndex(index));
                } else {
                    invalidateIndex(index);
                }
            } else {
                //往右边滑动
                int start = showCount / 2;
                int end = showCount % 2 == 0 ? totalCount - showCount / 2 + 1 : totalCount - showCount / 2;
                if (index > start - 1 && index < end - 1) {
                    startScrollAnim(Duration.RIGHT, () -> invalidateIndex(index));
                } else {
                    invalidateIndex(index);
                }
            }
        } else {
            invalidateIndex(index);
        }
    }

    public void setTotalCount(int count) {
        if (totalCount < count && totalCount >= showCount && curIndex >= totalCount - 2) {
            startScrollAnim(Duration.LEFT, () -> {
                invalidateTotal(count);
            });
            return;
        }
        if (totalCount > showCount && curX < 0 && curIndex >= totalCount - 2) {
            //超过最大展示数量，且当前有移动距离
            curX = curX + dotPadding + smallDotWidth;
            lastX = curX;
            invalidateTotal(count);
            return;
        }
        invalidateTotal(count);
    }

    public void reset() {
        totalCount = 0;
        curIndex = 0;
        curX = 0;
        lastX = 0;
    }

    private void invalidateTotal(int count) {
        totalCount = count;
        requestLayout();
        invalidate();
    }

    private void invalidateIndex(int index) {
        if (!isStickEnable) {
            curIndex = index;
            invalidate();
            return;
        }
        if (stickAnimator.isRunning()) {
            stickAnimator.end();
        }
        float startValues = getCurIndexX() + bigDotWidth / 2;
        if (index > curIndex) {
            stickAnimator.setFloatValues(startValues, startValues + dotPadding + smallDotWidth);
        } else {
            stickAnimator.setFloatValues(startValues, startValues - dotPadding - smallDotWidth);
        }

        isSwitchFinish = false;
        stickAnimator.removeAllListeners();
        stickAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isSwitchFinish = true;
                curIndex = index;
                invalidate();
            }
        });
        stickAnimator.start();
    }


    private void startScrollAnim(Duration duration, ScrollEndListener listener) {
        if (scrollAnimator.isRunning()) {
            scrollAnimator.end();
        }
        scrollAnimator.setDuration(animTime);
        float startValues = curX;
        float endValues = duration == Duration.LEFT ? curX - dotPadding - smallDotWidth : curX + dotPadding + smallDotWidth;
        scrollAnimator.setFloatValues(startValues, endValues);
        scrollAnimator.removeAllListeners();
        scrollAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                lastX = curX;
                invalidate();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.scrollEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                curX = lastX;
                invalidate();
            }
        });
        scrollAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startX = curX;
        float selectX = 0;
        for (int i = 0; i < totalCount; i++) {
            if (curIndex == i) {
                paint.setColor(selectColor);
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(2);

                selectRectF.left = startX;
                selectRectF.top = getHeight() / 2f - bigDotWidth / 2;
                selectRectF.right = startX + bigDotWidth;
                selectRectF.bottom = getHeight() / 2f + bigDotWidth / 2;
                //canvas.drawRoundRect(selectRectF, bigDotRadius, bigDotRadius, paint);
                canvas.drawCircle(startX + (bigDotWidth) / 2, bigDotWidth / 2, (bigDotWidth) / 2, paint);
                selectX = startX + bigDotWidth / 2;
                startX += (bigDotWidth + dotPadding);
            } else {
                paint.setColor(defaultColor);
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(2);

                startX += smallDotWidth / 2;
                canvas.drawCircle(startX, bigDotWidth / 2, (smallDotWidth) / 2, paint);
                startX += (smallDotWidth / 2 + dotPadding);
            }
        }

        if (isStickEnable) {
            if (isSwitchFinish) {
                stickPath.reset();
            } else {
                paint.setColor(selectColor);
                float quadStartX = selectX;
                float quadStartY = getHeight() / 2f - bigDotWidth / 2;
                stickPath.reset();
                stickPath.moveTo(quadStartX, quadStartY);
                stickPath.quadTo(quadStartX + (stickAnimX - quadStartX) / 2, bigDotWidth / 2, stickAnimX, quadStartY);
                stickPath.lineTo(stickAnimX, quadStartY + bigDotWidth);
                stickPath.quadTo(quadStartX + (stickAnimX - quadStartX) / 2, bigDotWidth / 2, quadStartX, quadStartY + bigDotWidth);
                stickPath.close();
                canvas.drawCircle(stickAnimX, bigDotWidth / 2, (bigDotWidth) / 2, paint);
                canvas.drawPath(stickPath, paint);
            }
        }
    }

    /**
     * 获取当前选中下标的X坐标
     */
    private float getCurIndexX() {
        if (curX < 0) {
            int translateCount = (int) (-curX / (smallDotWidth + dotPadding));
            return (curIndex - translateCount) * (smallDotWidth + dotPadding);
        }
        return curIndex *(smallDotWidth + dotPadding);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * 滑动方向
     */
    private enum Duration {
        LEFT,
        RIGHT
    }

    private interface ScrollEndListener {
        void scrollEnd();
    }
}



