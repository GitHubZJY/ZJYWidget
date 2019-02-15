package com.zjywidget.widget.camerapreview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import static com.zjywidget.widget.camerapreview.YCameraMarkView.CROP_CIRCLE;
import static com.zjywidget.widget.camerapreview.YCameraMarkView.CROP_RECT;


/**
 * @describe 遮罩层
 * @autor yang
 */
public class MarkView extends View {

    //是否可以开始绘制的标志位
    private boolean mIsInitialized = false;
    /********View**********/
    //View的实际宽度
    private float mViewWidth = 0;
    //View的实际高度
    private float mViewHeight = 0;
    //View的实际范围
    private RectF mViewRect;
    /********View**********/

    /********裁剪框**********/
    //裁剪框画笔
    private Paint mFramePaint;
    //裁剪框范围
    private RectF mFrameRect;
    //裁剪框宽度
    private float mCropWidth;
    //裁剪框高度
    private float mCropHeight;
    //裁剪框颜色
    private int mFrameColor;
    //裁剪框边宽度
    private float mFrameStrokeWeight = 2.0f;
    //裁剪路径
    private Path mCropPath;
    /********裁剪框**********/

    /********遮罩层**********/
    //遮罩层画笔
    private Paint mOverlayPaint;
    //遮罩层颜色
    private int mOverlayColor;
    //遮罩层透明度(0~1)
    private float mOverlayAlpha;
    /********遮罩层**********/

    private boolean mIsShowFrame = true;

    //当前裁剪模式
    private int mCropMode = CROP_RECT;

    public MarkView(Context context) {
        this(context, null);
    }

    public MarkView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mFramePaint = new Paint();
        mFramePaint.setAntiAlias(true);
        mOverlayPaint = new Paint();
        mCropPath = new Path();
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
        mViewRect = new RectF(0f, 0f, mViewWidth, mViewHeight);
        float left = (mViewWidth - mCropWidth) / 2;
        float top = (mViewHeight - mCropHeight) / 2;
        float right = left + mCropWidth;
        float bottom = top + mCropHeight;
        if (mFrameRect == null) {
            mFrameRect = new RectF(left, top, right, bottom);
            mIsInitialized = true;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mIsInitialized) {
            drawCropFrame(canvas);
        }
    }

    /**
     * 绘制裁剪框+遮罩
     *
     * @param canvas
     */
    private void drawCropFrame(Canvas canvas) {
        if (!mIsShowFrame) return;
        drawOverlay(canvas);
        drawFrame(canvas);
    }

    /**
     * 绘制遮罩层
     *
     * @param canvas
     */
    private void drawOverlay(Canvas canvas) {
        mOverlayPaint.setAntiAlias(true);
        mOverlayPaint.setFilterBitmap(true);
        mOverlayPaint.setColor(mOverlayColor);
        mOverlayPaint.setStyle(Paint.Style.FILL);
        mCropPath.addRect(mViewRect, Path.Direction.CW);
        switch (mCropMode) {
            case CROP_CIRCLE:
                mCropPath.addCircle(mViewWidth / 2, mViewHeight / 2, mFrameRect.width() / 2, Path.Direction.CCW);
                break;
            case CROP_RECT:
                mCropPath.addRect(mFrameRect, Path.Direction.CCW);
                break;
        }
        canvas.drawPath(mCropPath, mOverlayPaint);
    }

    /**
     * 绘制裁剪框的的四条边
     *
     * @param canvas
     */
    private void drawFrame(Canvas canvas) {
        mFramePaint.setAntiAlias(true);
        mFramePaint.setFilterBitmap(true);
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setColor(mFrameColor);
        mFramePaint.setStrokeWidth(mFrameStrokeWeight);
        switch (mCropMode) {
            case CROP_CIRCLE:
                canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mFrameRect.width() / 2, mFramePaint);
                break;
            case CROP_RECT:
                canvas.drawRect(mFrameRect, mFramePaint);
                break;
        }
    }

    /**
     * 设置裁剪框以外的颜色
     *
     * @param overlayColor color resId or color int(ex. 0xFFFFFFFF)
     */
    public void setOverlayColor(int overlayColor) {
        this.mOverlayColor = overlayColor;
        invalidate();
    }

    /**
     * 设置遮罩层透明度
     *
     * @param alpha
     */
    public void setOverlayAlpha(float alpha) {
        this.mOverlayAlpha = alpha;
        mOverlayColor = Color.parseColor("#" + intToHex(mOverlayAlpha) + "000000");
        invalidate();
    }

    /**
     * 设置裁剪框的颜色
     */
    public void setFrameColor(int frameColor) {
        this.mFrameColor = frameColor;
        invalidate();
    }

    /**
     * 设置裁剪框的宽
     */
    public void setFrameStrokeWidth(int width) {
        mFrameStrokeWeight = dip2px(getContext(), width);
        invalidate();
    }


    /**
     * 是否显示裁剪框
     *
     * @param enabled should show crop frame?
     */
    public void setShowCropFrame(boolean enabled) {
        mIsShowFrame = enabled;
        invalidate();
    }

    public void setCropParams(float width, float height) {
        mCropWidth = width;
        mCropHeight = height;
        invalidate();
    }

    /**
     * 获取裁剪框的宽
     */
    private float getFrameWidth() {
        return (mFrameRect.right - mFrameRect.left);
    }

    /**
     * 获取裁剪框的高
     */
    private float getFrameHeight() {
        return (mFrameRect.bottom - mFrameRect.top);
    }

    /**
     * 获取裁剪框
     */
    public RectF getFrameRect() {
        return mFrameRect;
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setCropMode(int mode) {
        if (mode != CROP_CIRCLE && mode != CROP_RECT) {
            return;
        }
        mCropMode = mode;
        invalidate();
    }

    public void setStrokeWidth(float strokeWidth) {
        mFrameStrokeWeight = strokeWidth;
        invalidate();
    }

    /**
     * 透明度（0-1）转换为十六进制
     *
     * @param alpha
     * @return
     */
    private static String intToHex(float alpha) {
        if (alpha == 0) {
            //传入透明度为0，直接返回00，全透明
            return "00";
        }
        int n = (int) ((alpha / 1f) * 255);
        StringBuffer s = new StringBuffer();
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            s = s.append(b[n % 16]);
            n = n / 16;
        }
        a = s.reverse().toString();
        return a;
    }
}
