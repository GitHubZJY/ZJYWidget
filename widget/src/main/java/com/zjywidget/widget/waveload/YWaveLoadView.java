package com.zjywidget.widget.waveload;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.zjywidget.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YANG on 2019/03/17.
 * 可定制图标的水波纹进度球
 */
public class YWaveLoadView extends View {

    //水波纹路径
    private Path mWavePath1, mWavePath2;
    //每节波浪的宽度
    private int mItemWidth = 120;
    //波浪的偏移量
    private int mOffsetX1, mOffsetX2;

    private Paint mIconPaint;
    private Paint mBallPaint;
    private Paint mTextPaint;

    //整个View的宽高
    private float mWidth, mHeight;

    //当前进度(0~100)
    private int mProgress;
    //圆形遮罩
    private Bitmap mBallBitmap;
    //图标资源对象
    private Drawable mDrawable;
    //当前水位高度的纵坐标
    private int mWaterTop;
    //每节波浪上下波动的幅度
    private int mWaveHeight;

    /**
     * 混合模式
     */
    private PorterDuffXfermode mDuffXfermode;

    /**
     * 动画器
     */
    private ValueAnimator mProgressAnim;
    private ValueAnimator mWaveHeightAnim;
    private List<Animator> mAnimList;
    private AnimatorSet mAnimatorSet;

    /**
     * 球的边缘宽度
     */
    private float mBallStrokeWidth;

    /**
     * 球的颜色
     */
    private int mBallColor;

    /**
     * 图标drawable Id
     */
    private int mIconResId;

    /**
     * 是否展示百分比文本（只在COLOR_STYLE模式下有效）
     */
    private boolean mIsShowText;

    /**
     * 中心文字大小（只在COLOR_STYLE模式下有效）
     */
    private int mTextSize;

    /**
     * 动画时长
     */
    private int mDuration;

    /**
     * 是否自动开启动画
     */
    private boolean mIsAutoLoad;

    /**
     * 样式
     * 1.ICON_STYLE:图标样式   2.COLOR_STYLE:纯颜色样式
     */
    private int mMode;
    public static final int ICON_STYLE = 1;
    public static final int COLOR_STYLE = 2;

    public YWaveLoadView(Context context) {
        this(context, null);
    }

    public YWaveLoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YWaveLoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleStyleable(context, attrs, defStyleAttr);
        init();
    }

    private void handleStyleable(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaveLoadView, defStyle, 0);
        try {
            mMode = ta.getInteger(R.styleable.WaveLoadView_wave_load_mode, ICON_STYLE);
            mIsAutoLoad = ta.getBoolean(R.styleable.WaveLoadView_wave_load_auto_load, true);
            mIsShowText = ta.getBoolean(R.styleable.WaveLoadView_wave_load_show_text, true);
            mTextSize = ta.getDimensionPixelSize(R.styleable.WaveLoadView_wave_load_text_size, 40);
            mDuration = ta.getInteger(R.styleable.WaveLoadView_wave_load_duration, 5000);
            mIconResId = ta.getResourceId(R.styleable.WaveLoadView_wave_load_icon, -1);
            mBallColor = ta.getColor(R.styleable.WaveLoadView_wave_load_color, Color.parseColor("#e58c7e"));
            mBallStrokeWidth = ta.getDimensionPixelSize(R.styleable.WaveLoadView_wave_load_stroke_width, 4);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }
    }

    private void init() {

        mIconPaint = new Paint();
        mIconPaint.setFilterBitmap(true);
        mIconPaint.setAntiAlias(true);
        mIconPaint.setDither(true);

        mBallPaint = new Paint();
        mBallPaint.setStyle(Paint.Style.STROKE);
        mBallPaint.setStrokeWidth(mBallStrokeWidth);
        mBallPaint.setColor(mBallColor);
        mBallPaint.setAntiAlias(true);
        mBallPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.WHITE);

        mWavePath1 = new Path();
        mWavePath2 = new Path();


        initAnim();

        mDrawable = getResources().getDrawable(mIconResId);

        mDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    /**
     * 初始化所有动画器
     */
    private void initAnim() {
        mAnimatorSet = new AnimatorSet();
        mAnimList = new ArrayList<>();

        ValueAnimator mOffsetAnimator1 = ValueAnimator.ofInt(0, mItemWidth);
        mOffsetAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffsetX1 = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        mOffsetAnimator1.setInterpolator(new LinearInterpolator());

        mOffsetAnimator1.setDuration(500);
        mOffsetAnimator1.setRepeatCount(-1);

        ValueAnimator mOffsetAnimator2 = ValueAnimator.ofInt(0, mItemWidth / 2);
        mOffsetAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffsetX2 = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        mOffsetAnimator2.setInterpolator(new LinearInterpolator());

        mOffsetAnimator2.setDuration(800);
        mOffsetAnimator2.setRepeatCount(-1);

        mProgressAnim = ValueAnimator.ofInt();
        mProgressAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWaterTop = (int) animation.getAnimatedValue();
                float progress = (-(float) mWaterTop / mHeight + 1) * 100;
                mProgress = (int) progress;
                invalidate();
            }
        });

        mProgressAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mProgressAnim.setDuration(mDuration);

        mWaveHeightAnim = ValueAnimator.ofInt();
        mWaveHeightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWaveHeight = (int) animation.getAnimatedValue() / 6;
                invalidate();
            }
        });

        mWaveHeightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mWaveHeightAnim.setDuration(mDuration);

        mAnimList.add(mOffsetAnimator1);
        mAnimList.add(mOffsetAnimator2);
        mAnimList.add(mProgressAnim);
        mAnimList.add(mWaveHeightAnim);

    }

    public void startLoad(){
        if (mAnimList != null && mAnimList.size() > 0 &&
                !mAnimatorSet.isRunning() && !mAnimatorSet.isStarted()) {
            mAnimatorSet.playTogether(mAnimList);
            mAnimatorSet.start();
        }
    }

    public void stopLoad(){
        mAnimatorSet.cancel();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mWidth = right - left;
        mHeight = bottom - top;
        //创建圆形遮罩图
        createBallBitmap();

        if (mMode == ICON_STYLE) {
            mDrawable.setBounds(0, 0, (int) mWidth, (int) mHeight);
        }
        //进度动画和波浪高度动画需要Height的值
        mProgressAnim.setIntValues((int) mHeight, 0);
        mWaveHeightAnim.setIntValues(0, (int) mHeight / 2, 0);
        mWaterTop = (int)mHeight;
        //避免layout多次调用多次启动动画
        if (mIsAutoLoad &&
                mAnimList != null && mAnimList.size() > 0 &&
                !mAnimatorSet.isRunning() && !mAnimatorSet.isStarted()) {
            mAnimatorSet.playTogether(mAnimList);
            mAnimatorSet.start();
        }
    }

    private void createBallBitmap() {
        mBallBitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBallBitmap);
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, mWidth / 2f - mBallStrokeWidth * 3f / 2f, mIconPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWavePath1.reset();
        int halfItem = mItemWidth / 2;
        //为了闭合整个View, 否则波浪遮罩顶部显示不正常
        mWavePath1.moveTo(0, 0);
        //必须先减去一个浪的宽度，以便第一遍动画能够刚好位移出一个波浪，形成无限波浪的效果
        mWavePath1.lineTo(-mItemWidth + mOffsetX1, mWaterTop);
        for (int i = -mItemWidth; i < mItemWidth + mWidth; i += mItemWidth) {
            mWavePath1.rQuadTo(halfItem / 2, -mWaveHeight, halfItem, 0);
            mWavePath1.rQuadTo(halfItem / 2, mWaveHeight, halfItem, 0);
        }

        //闭合路径波浪以下区域
        mWavePath1.lineTo(mWidth, mHeight);
        mWavePath1.lineTo(0, mHeight);
        mWavePath1.lineTo(-mItemWidth + mOffsetX1, mWaterTop);
        mWavePath1.close();

        mWavePath2.reset();
        int wave2ItemWidth = mItemWidth / 2;
        int halfItem2 = wave2ItemWidth / 2;
        //为了闭合整个View, 否则波浪遮罩顶部显示不正常
        mWavePath2.moveTo(0, 0);
        //必须先减去一个浪的宽度，以便第一遍动画能够刚好位移出一个波浪，形成无限波浪的效果
        mWavePath2.lineTo(-wave2ItemWidth + mOffsetX2, mWaterTop);
        for (int i = -wave2ItemWidth; i < wave2ItemWidth + mWidth; i += wave2ItemWidth) {
            mWavePath2.rQuadTo(halfItem2 / 2, -mWaveHeight / 2, halfItem2, 0);
            mWavePath2.rQuadTo(halfItem2 / 2, mWaveHeight / 2, halfItem2, 0);
        }

        //闭合路径波浪以下区域
        mWavePath2.lineTo(mWidth, mHeight);
        mWavePath2.lineTo(0, mHeight);
        mWavePath2.lineTo(-wave2ItemWidth + mOffsetX2, mWaterTop);
        mWavePath2.close();

        //隔离层绘制混合模式
        int layerId = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);
        //绘制Icon
        if (mMode == COLOR_STYLE) {
            mBallPaint.setStyle(Paint.Style.FILL);
            //绘制水波纹1
            canvas.drawPath(mWavePath1, mBallPaint);
            //绘制水波纹2
            canvas.drawPath(mWavePath2, mBallPaint);
            mIconPaint.setXfermode(mDuffXfermode);
            //绘制圆形位图
            canvas.drawBitmap(mBallBitmap, 0, 0, mIconPaint);
            mIconPaint.setXfermode(null);
        } else {
            mDrawable.draw(canvas);
            mIconPaint.setXfermode(mDuffXfermode);
            //绘制水波纹1
            canvas.drawPath(mWavePath1, mIconPaint);
            //绘制水波纹2
            canvas.drawPath(mWavePath2, mIconPaint);
            //绘制圆形位图
            canvas.drawBitmap(mBallBitmap, 0, 0, mIconPaint);
            mIconPaint.setXfermode(null);
        }
        canvas.restoreToCount(layerId);

        //绘制中心百分比文本
        if (mMode == COLOR_STYLE && mIsShowText) {
            mBallPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(mWidth / 2f, mHeight / 2f, mWidth / 2f - mBallStrokeWidth, mBallPaint);
            float textWidth = mTextPaint.measureText(mProgress + "%");
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float baseLine = mHeight / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2;
            canvas.drawText(mProgress + "%", mWidth / 2f - textWidth / 2, baseLine, mTextPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //防止内存泄漏
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }
    }
}
