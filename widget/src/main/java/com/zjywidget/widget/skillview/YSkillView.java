package com.zjywidget.widget.skillview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.zjywidget.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YANG on 2019/1/9.
 */

public class YSkillView extends View {

    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mScorePaint;
    private Paint mScorePointPaint;
    private Path mPath;
    private Path mScorePath;
    private float mRadius;
    private float mCenterX;
    private float mCenterY;
    private int mMaxValue;
    private float mTextSize;
    private float mWidth;
    private float mHeight;
    private float mLineWidth;
    private int mBgColor;
    private int mTextColor;
    private int mScoreAreaColor;
    private int mScorePointColor;

    private final static int DEFAULT_LINE_WIDTH = 2;
    private final static int DEFAULT_SKILL_TEXT_SIZE = 16;
    private final static int DEFAULT_MAX_VALUE = 5;
    private final static int DEFAULT_BG_COLOR = Color.parseColor("#eeffffff");
    private final static int DEFAULT_TEXT_COLOR = Color.parseColor("#ffffff");
    private final static int DEFAULT_SCORE_COLOR = Color.parseColor("#aae6e6fa");
    private final static int DEFAULT_POINT_COLOR = Color.parseColor("#e6e6fa");
    private final static int DEFAULT_ANIM_DURATION = 1000;
    private final static int DEFAULT_RADIUS = 200;

    private float[] mScoreArr = new float[]{};
    private String[] mTextArr = new String[]{"文案1", "文案2", "文案3", "文案4", "文案5", "文案6"};
    private float mFontHeight;

    private float mAnimatorValue;
    private AnimatorSet mAnimatorSet;
    private long mAnimDuration;

    public YSkillView(Context context) {
        this(context, null);
    }

    public YSkillView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YSkillView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleStyleable(context, attrs, defStyleAttr);
        init();
    }

    private void handleStyleable(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SkillView, defStyle, 0);
        try {
            mLineWidth = ta.getDimensionPixelSize(R.styleable.SkillView_line_width, DEFAULT_LINE_WIDTH);
            mBgColor = ta.getColor(R.styleable.SkillView_bg_color, DEFAULT_BG_COLOR);
            mTextColor = ta.getColor(R.styleable.SkillView_text_color, DEFAULT_TEXT_COLOR);
            mScoreAreaColor = ta.getColor(R.styleable.SkillView_score_color, DEFAULT_SCORE_COLOR);
            mScorePointColor = ta.getColor(R.styleable.SkillView_point_color, DEFAULT_POINT_COLOR);
            mTextSize = ta.getDimensionPixelSize(R.styleable.SkillView_skill_text_size, DEFAULT_SKILL_TEXT_SIZE);
            mMaxValue = ta.getInt(R.styleable.SkillView_max_value, DEFAULT_MAX_VALUE);
            mAnimDuration = ta.getInt(R.styleable.SkillView_anim_duration, DEFAULT_ANIM_DURATION);
            mRadius = ta.getDimensionPixelSize(R.styleable.SkillView_radius, DEFAULT_RADIUS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setColor(mBgColor);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mScorePaint = new Paint();
        mScorePaint.setColor(mScoreAreaColor);
        mScorePaint.setStyle(Paint.Style.FILL);

        mScorePointPaint = new Paint();
        mScorePointPaint.setColor(mScorePointColor);
        mScorePointPaint.setStyle(Paint.Style.FILL);
        mScorePointPaint.setStrokeWidth(3);

        mPath = new Path();
        mScorePath = new Path();

        initAnim();
    }

    private void initAnim() {
        mAnimatorSet = new AnimatorSet();
        List<Animator> mAnimList = new ArrayList<>();
        for (int i = 0; i < mScoreArr.length; i++) {
            final int index = i;
            ValueAnimator scoreAnimator = ValueAnimator.ofFloat(0, mScoreArr[index]);
            scoreAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            scoreAnimator.setDuration(mAnimDuration);
            scoreAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                    mScoreArr[index] = mAnimatorValue;
                    //重绘
                    invalidate();
                }
            });
            mAnimList.add(scoreAnimator);
        }
        mAnimatorSet.playTogether(mAnimList);
        mAnimatorSet.start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        mHeight = bottom - top;
        float contentR = mWidth < mHeight ? mWidth : mHeight;
        mRadius = contentR / 2 - ((contentR / 2) / (mMaxValue + 1)) * 2;
        mCenterX = contentR / 2;
        mCenterY = contentR / 2;
        mTextSize = (int) (mRadius / (mMaxValue + 1)) * 7 / 8;
        mTextPaint.setTextSize(mTextSize);
        mFontHeight = mTextPaint.getFontMetrics().descent - mTextPaint.getFontMetrics().ascent;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
    }

    private void drawBg(Canvas canvas) {

        float r = mRadius / mMaxValue;
        for (int i = 1; i < mMaxValue + 2; i++) {
            float curR;
            if (i == mMaxValue + 1) {
                curR = r * i + 10;
            } else {
                curR = r * i;
            }
            mPath.reset();
            for (int j = 1; j <= 6; j++) {
                float x = (float) (mCenterX + curR * Math.sin((60 * j) * Math.PI / 180));
                float y = (float) (mCenterY + curR * Math.cos((60 * j) * Math.PI / 180));

                if (i == mMaxValue + 1) {
                    canvas.drawText(mTextArr[j - 1], x, y + mFontHeight / 2, mTextPaint);
                } else {
                    if (j == 1) {
                        mPath.moveTo(x, y);
                    } else {
                        mPath.lineTo(x, y);
                    }
                }
            }
            mPath.close();
            canvas.drawPath(mPath, mPaint);
        }
        mPath.reset();


        for (int j = 1; j <= 6; j++) {
            mPath.moveTo(mCenterX, mCenterY);
            float x = (float) (mCenterX + mRadius * Math.sin((60 * j) * Math.PI / 180));
            float y = (float) (mCenterY + mRadius * Math.cos((60 * j) * Math.PI / 180));
            mPath.lineTo(x, y);
            canvas.drawPath(mPath, mPaint);
            mPath.reset();
        }

        for (int j = 0; j < mScoreArr.length; j++) {
            if (j >= 6) {
                //超过6边,不予展示
                continue;
            }
            float x = (float) (mCenterX + mScoreArr[j] * r * Math.sin((60 * j) * Math.PI / 180));
            float y = (float) (mCenterY + mScoreArr[j] * r * Math.cos((60 * j) * Math.PI / 180));
            if (j == 0) {
                mScorePath.moveTo(x, y);
            } else {
                mScorePath.lineTo(x, y);
            }
            canvas.drawCircle(x, y, 5, mScorePointPaint);
        }
        mScorePath.close();

        canvas.drawPath(mScorePath, mScorePaint);
    }

    /**
     * 设置分数
     *
     * @param scores
     */
    public void setScore(float[] scores) {
        mScoreArr = scores;
        initAnim();
    }

    /**
     * 设置分数
     *
     * @param scores
     */
    public void setScore(List<Float> scores) {
        if (scores != null && scores.size() > 0) {
            for (int i = 0; i < scores.size(); i++) {
                mScoreArr[i] = scores.get(i);
            }
            initAnim();
        }
    }

    /**
     * 设置属性文案
     *
     * @param scores
     */
    public void setTextArr(String[] scores) {
        mTextArr = scores;
        invalidate();
    }

    /**
     * 设置属性文案
     *
     * @param scores
     */
    public void setTextArr(List<String> scores) {
        if (scores != null && scores.size() > 0) {
            for (int i = 0; i < scores.size(); i++) {
                mTextArr[i] = scores.get(i);
            }
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }
    }
}
