package com.example.zjy.zjywidget.camerapreview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.zjy.zjywidget.R;

import static com.example.zjy.zjywidget.camerapreview.YCameraMarkView.CROP_CIRCLE;
import static com.example.zjy.zjywidget.camerapreview.YCameraMarkView.CROP_RECT;

/**
 * @describe 扫描层
 * @autor yang
 */
public class ScanView extends View{

    //是否可以开始绘制的标志位
    private boolean mIsInitialized = false;
    /********View**********/
    //View的实际宽度
    private float mViewWidth = 0;
    //View的实际高度
    private float mViewHeight = 0;
    /********View**********/

    /********裁剪框**********/
    //裁剪框画笔
    private Paint mFramePaint;
    //裁剪框范围
    private RectF mFrameRect;
    //裁剪框宽度
    private float mCropWidth = YCameraMarkView.FRAME_DEFAULT_WIDTH;
    //裁剪框高度
    private float mCropHeight = YCameraMarkView.FRAME_DEFAULT_HEIGHT;
    /********裁剪框**********/

    /********雷达扫描**********/
    //网格
    //private Drawable mGridDrawable;
    //雷达
    private Drawable mRadarDrawable;
    //雷达上顶点坐标
    private int mRadarTop = 0;
    //圆形蒙版截取扫描动画
    private Bitmap mMarkBitmap;
    //雷达画笔
    private Paint mRadarPaint;
    //扫描动画器
    private ValueAnimator mValueAnimator;
    /********雷达扫描**********/

    //是否开启裁剪
    private boolean mIsShowScan = true;

    //当前裁剪模式
    private int mCropMode = CROP_RECT;

    private long mDuration = 1500;

    public ScanView(Context context) {
        this(context, null);
    }

    public ScanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mFramePaint = new Paint();
        mFramePaint.setAntiAlias(true);
        mRadarPaint = new Paint();
        mRadarDrawable = getResources().getDrawable(R.drawable.bg_radar);

        mValueAnimator = ValueAnimator.ofInt(0, dip2px(context, 600));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);
        mViewWidth = viewWidth - getPaddingLeft() - getPaddingRight();
        mViewHeight = viewHeight - getPaddingTop() - getPaddingBottom();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        setupLayout();
    }

    /**
     * 计算裁剪框的位置
     */
    private void setupLayout() {
        if (mViewWidth == 0 || mViewHeight == 0) {
            return;
        }
        float left = (mViewWidth - mCropWidth) / 2;
        float top = (mViewHeight - mCropHeight) / 2;
        float right = left + mCropWidth;
        float bottom = top + mCropHeight;
        if (!mIsShowScan) {
            left = 0;
            top = 0;
            right = mViewWidth;
            bottom = mViewHeight;
        }
        if (mFrameRect == null) {
            mFrameRect = new RectF(left, top, right, bottom);
            mIsInitialized = true;
        }
        mMarkBitmap = Bitmap.createBitmap((int) mViewWidth, (int) mViewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mMarkBitmap);
        switch (mCropMode) {
            case CROP_CIRCLE:
                canvas.drawCircle(left + mCropWidth / 2, top + mCropWidth / 2, mCropWidth / 2, mRadarPaint);
                break;
            case CROP_RECT:
                canvas.drawRect(mFrameRect, mRadarPaint);
                break;
        }

        mRadarTop = (int)top - mRadarDrawable.getIntrinsicHeight();
        initRadarAnim();
    }

    /**
     * 雷达扫描动画
     */
    private void initRadarAnim() {
        mValueAnimator.setIntValues(mRadarTop, (int)(mFrameRect.top + mCropHeight + dip2px(getContext(), 20)));
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                mRadarTop = (int) valueAnimator.getAnimatedValue();
                //LogUtils.d("zjy", "onAnimationUpdate -->" + mRadarTop);
                invalidate();
            }
        });
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setDuration(mDuration);
    }

    public void setDuration(long duration){
        mDuration = duration;
        mValueAnimator.setDuration(mDuration);
    }

    public void startRadar() {
        if(!mIsShowScan){
            return;
        }
        if (mValueAnimator != null) {
            mValueAnimator.start();
        }
    }

    public void stopRadar() {
        if(!mIsShowScan){
            return;
        }
        if (mValueAnimator != null) {
            mRadarTop = (int)mFrameRect.top - mRadarDrawable.getIntrinsicHeight();
            mValueAnimator.cancel();
            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mIsInitialized && mIsShowScan) {
            drawRadar(canvas);
        }
    }

    private void drawRadar(Canvas canvas) {
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //dst 绘制目标图层
        mRadarDrawable.setBounds((int) mFrameRect.left, mRadarTop, (int) mFrameRect.right, mRadarTop + mRadarDrawable.getIntrinsicHeight());
        mRadarDrawable.draw(canvas);

        mRadarPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(mMarkBitmap, 0, 0, mRadarPaint);
        mRadarPaint.setXfermode(null);
        canvas.restoreToCount(sc);
    }


    public void setCropMode(int mode){
        if(mode != CROP_CIRCLE && mode != CROP_RECT){
            return;
        }
        mCropMode = mode;
        invalidate();
    }

    public void setCropParams(float width, float height){
        mCropWidth = width;
        mCropHeight = height;
        invalidate();
    }

    public void setShowScan(boolean isShow){
        mIsShowScan = isShow;
        invalidate();
    }


    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


}
