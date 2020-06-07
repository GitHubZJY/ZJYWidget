package com.zjywidget.widget.wheeldialog;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zjywidget.widget.R;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * <b>Project:</b> https://github.com/GitHubZJY/ZJYWidget <br>
 * <b>Create Date:</b> 2020/06/03 <br>
 * <b>@author:</b> Yang <br>
 * <b>Description:</b> 简约款上下滑动选择器 <br>
 */
public class YWheelView extends View {

    private static final String TAG = "MyWheelView";

    private static final String RESILIENCE_DISTANCE_OF_ONCE = "resilience_distance_of_once";
    private static final String RESILIENCE_LEFT_TIMES = "left_times";

    private static final int RESILIENCE_TIMES = 5;
    private static final int RESILIENCE_TIME_INTERVAL = 50;

    private List<String> data;
    private int mSelectedIndex = 0;

    private float lastY;
    private float scrollY;

    private int viewWidth;
    private int viewHeight;
    private float mItemHeight;
    private int mItemCount;
    private int mHalfItemCount;
    private float maxTextSize;
    private float minTextSize;

    private Paint mSelectedBgPaint;
    private Paint mTextPaint;
    private int mSelectTxtColor;
    private int mNormalTxtColor;
    private float mSelectTxtScale;
    private float mNormalTxtScale;

    private Handler handler;

    private IWheelViewSelectedListener wheelViewSelectedListener;

    public YWheelView(Context context) {
        this(context, null);
    }

    public YWheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleStyleable(context, attrs);
        init();
    }

    private void init() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mSelectedBgPaint = new Paint();
        mSelectedBgPaint.setColor(Color.WHITE);
        mSelectedBgPaint.setStyle(Paint.Style.FILL);

        mSelectedIndex = 0;
        handler = new WheelViewHandler(this);
    }

    private void handleStyleable(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.YWheelView);
        mItemCount = ta.getDimensionPixelSize(R.styleable.YWheelView_wheel_item_count, 5);
        mSelectTxtColor = ta.getColor(R.styleable.YWheelView_wheel_item_select_txt_color, Color.BLACK);
        mNormalTxtColor = ta.getColor(R.styleable.YWheelView_wheel_item_normal_txt_color, Color.LTGRAY);
        mSelectTxtScale = ta.getFloat(R.styleable.YWheelView_wheel_item_select_txt_scale, 0.6f);
        mNormalTxtScale = ta.getFloat(R.styleable.YWheelView_wheel_item_normal_txt_scale, 0.48f);
        ta.recycle();

        if (mItemCount <= 0) {
            throw new IllegalArgumentException("item count must bigger than 0");
        }
        mHalfItemCount = mItemCount / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        mItemHeight = ((float) viewHeight) / mItemCount;
        maxTextSize = mSelectTxtScale * mItemHeight;
        minTextSize = mNormalTxtScale * mItemHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (null == data || 0 == data.size()) {
            return;
        }
        drawSelectedArea(canvas);
        drawAllText(canvas);
    }

    /**
     * 绘制选中区域
     *
     * @param canvas
     */
    private void drawSelectedArea(Canvas canvas) {
        canvas.drawRect(0, mItemHeight * mHalfItemCount, viewWidth, mItemHeight * (mHalfItemCount + 1), mSelectedBgPaint);
    }

    private void drawAllText(Canvas canvas) {
        String text;
        float midY;
        int startShowIndex = Math.max(0, mSelectedIndex - (mHalfItemCount + 1));
        int endShowIndex = Math.min(data.size() - 1, mSelectedIndex + (mHalfItemCount + 1));
        for (int i = startShowIndex; i <= endShowIndex; i++) {
            text = data.get(i);
            if (i == mSelectedIndex) {
                mTextPaint.setColor(mSelectTxtColor);
            } else {
                mTextPaint.setColor(mNormalTxtColor);
            }
            midY = mItemHeight * (mHalfItemCount - (mSelectedIndex - i)) + mItemHeight / 2 - scrollY;
            setTextPaint(mTextPaint, midY);
            canvas.drawText(text, (viewWidth - mTextPaint.measureText(text)) / 2,
                    midY + getTextBaselineToCenter(mTextPaint), mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Message message;
        Bundle bundle;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeMessages(WheelViewHandler.RESILIENCE);
                lastY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                scrollY -= event.getY() - lastY;
                lastY = event.getY();
                confirmSelectedItem();
                return true;
            case MotionEvent.ACTION_UP:
                message = handler.obtainMessage();
                message.what = WheelViewHandler.RESILIENCE;
                bundle = new Bundle();
                bundle.putFloat(RESILIENCE_DISTANCE_OF_ONCE, scrollY / RESILIENCE_TIMES);
                bundle.putInt(RESILIENCE_LEFT_TIMES, RESILIENCE_TIMES);
                message.setData(bundle);
                message.sendToTarget();
                return true;
        }
        return false;
    }

    /**
     * 获取字符串的中基线
     */
    private float getTextBaselineToCenter(Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return ((float) (-fontMetrics.bottom - fontMetrics.top)) / 2;
    }

    private void confirmSelectedItem() {
        //计算移动了几个item的height了, < 0说明向上， >0说明向下
        int changedItemNumber = Math.round(scrollY / mItemHeight);

        int lastItem = getSelectedIndex();
        //计算这次的【合法的】的index
        int tempSelectedItem = getSelectedIndex() + changedItemNumber;
        if (tempSelectedItem < 0)
            tempSelectedItem = 0;
        if (tempSelectedItem >= data.size())
            tempSelectedItem = data.size() - 1;
        this.mSelectedIndex = tempSelectedItem;
        //减去相应的scrollY值（为了可以上滑和下滑超出）
        scrollY -= mItemHeight * (mSelectedIndex - lastItem);
        invalidate();
        if (lastItem != tempSelectedItem && null != wheelViewSelectedListener){
            wheelViewSelectedListener.wheelViewSelectedChanged(this, data, mSelectedIndex);
        }
    }

    private void setTextPaint(Paint paint, float midY) {
        paint.setTextSize(maxTextSize - (maxTextSize - minTextSize) * Math.abs(viewHeight / 2 - midY) / (viewHeight / 2));
    }

    private void resilienceToCenter(float distance) {
        scrollY -= distance;
        invalidate();
    }

    public void setWheelViewSelectedListener(IWheelViewSelectedListener wheelViewSelectedListener) {
        this.wheelViewSelectedListener = wheelViewSelectedListener;
    }

    private static class WheelViewHandler extends Handler {

        static final int RESILIENCE = 1;

        private WeakReference<YWheelView> viewReference;

        private WheelViewHandler(YWheelView wheelView) {
            viewReference = new WeakReference<>(wheelView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            YWheelView myWheelView;
            Message message;
            Bundle bundle;
            int leftTimes;
            switch (msg.what) {
                case RESILIENCE:
                    bundle = msg.getData();
                    myWheelView = viewReference.get();
                    if (null != myWheelView)
                        myWheelView.resilienceToCenter(bundle.getFloat(RESILIENCE_DISTANCE_OF_ONCE, 0));

                    leftTimes = bundle.getInt(RESILIENCE_LEFT_TIMES, 0);
                    if (leftTimes > 1) {//如果还要重绘，发送重绘的消息，这里重复使用了bundle
                        Log.d("zjy", "leftTimes: " + leftTimes + ", distance: " + bundle.getFloat(RESILIENCE_DISTANCE_OF_ONCE, 0));
                        bundle.putInt(RESILIENCE_LEFT_TIMES, leftTimes - 1);
                        message = new Message();
                        message.what = RESILIENCE;
                        message.setData(bundle);
                        this.sendMessageDelayed(message, RESILIENCE_TIME_INTERVAL);
                    }
                    break;
            }
        }
    }


    /**
     * 绑定选项数据，已经初始化选中下标
     *
     * @param data              选项数据
     * @param selectedItemIndex 初始化选中的下标
     */
    public void bindData(List<String> data, int selectedItemIndex) {
        this.data = data;
        setSelectedIndex(selectedItemIndex);
    }

    /**
     * 选中某个选项
     *
     * @param mSelectedIndex
     */
    public void setSelectedIndex(int mSelectedIndex) {
        //外部自己负责处理这个index是否合法
        this.mSelectedIndex = mSelectedIndex;
        //既然外部设置index，就不要这个偏移量了
        this.scrollY = 0;
        invalidate();
        if (null != wheelViewSelectedListener){
            wheelViewSelectedListener.wheelViewSelectedChanged(this, data, mSelectedIndex);
        }
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }


    public interface IWheelViewSelectedListener {

        void wheelViewSelectedChanged(YWheelView myWheelView, List<String> data, int position);

    }

}