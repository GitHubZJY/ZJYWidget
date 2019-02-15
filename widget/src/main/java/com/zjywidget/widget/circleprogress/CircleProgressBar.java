package com.zjywidget.widget.circleprogress;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.zjywidget.widget.R;


/**
 * Created by YANG on 2017/3/20.
 * <p>
 * 圆形动画进度条
 */

public class CircleProgressBar extends View {


    private Paint paint;

    //控件默认宽高
    private final int DEFAULT_WIDTH = 200;
    private final int DEFAULT_HEIGHT = 200;

    //外圆的半径
    private float roundWidth;
    //外圆的边界粗细度
    private float bgStrokeWidth;
    //外圆颜色
    private int roundColor;
    //进度弧线的边界粗细度
    private float progressStrokeWidth;

    //进度弧线颜色
    private int progressColor;

    //中间的百分比文字颜色
    private int textColor;

    //中间的百分比文字大小
    private float textSize;

    //进度值
    private int progressValue;

    //最大值
    private int maxValue;

    //中间的文字内容
    private String centerText = "";

    //动画时间
    private int animTime = 1500;

    private RectF mArcRectF;


    public CircleProgressBar(Context context) {
        super(context, null);
    }


    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CircleProgressBar);
        roundWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressBar_roundWidth, 60);
        bgStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressBar_bgStrokeWidth, 7);
        roundColor = typedArray.getColor(R.styleable.CircleProgressBar_roundColor, Color.LTGRAY);
        progressStrokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_progressStrokeWidth, 14);
        progressColor = typedArray.getColor(R.styleable.CircleProgressBar_progressColor, Color.parseColor("#FF4081"));
        textColor = typedArray.getColor(R.styleable.CircleProgressBar_textColor, Color.BLACK);
        textSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgressBar_textSize, 40);
        progressValue = typedArray.getInteger(R.styleable.CircleProgressBar_progressValue, 40);
        maxValue = typedArray.getInteger(R.styleable.CircleProgressBar_maxValue, 100);
        centerText = typedArray.getString(R.styleable.CircleProgressBar_centerText);

        if (progressValue < 0) {
            progressValue = 0;
        }

        if (maxValue < 0) {
            maxValue = 0;
        }
        typedArray.recycle();

        paint = new Paint();
        mArcRectF = new RectF();

        //设置动画
        setAnimation(0, progressValue, animTime);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //最终宽高
        int finallyWidth;
        int finallyHeight;

        //兼容wrap_content模式
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (wSpecMode == MeasureSpec.EXACTLY) {
            finallyWidth = wSpecSize;
        } else {
            finallyWidth = DEFAULT_WIDTH;
        }

        if (hSpecMode == MeasureSpec.EXACTLY) {
            finallyHeight = hSpecSize;
        } else {
            finallyHeight = DEFAULT_HEIGHT;
        }

        setMeasuredDimension(finallyWidth, finallyHeight);
    }


    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        float radius = center - roundWidth / 2;
        /**
         * 绘制后面的整圆
         */
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(bgStrokeWidth); //设置圆环的宽度
        paint.setColor(roundColor);
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(center, center, radius, paint);


        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT);
        if (!TextUtils.isEmpty(centerText)) {
            //如果是设置文本内容，则直接测量文本长度并绘制
            float textWidth = paint.measureText(centerText);
            canvas.drawText(centerText, center - textWidth / 2, center + textSize / 2, paint); //画出进度百分比
        } else {
            //如果是设置百分比，则计算百分比并绘制
            int percent = (int) (((float) progressValue / (float) maxValue) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
            float textWidth = paint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
            if (percent != 0) {
                canvas.drawText(percent + "%", center - textWidth / 2, center + textSize / 2, paint); //画出进度百分比
            }
        }


        /**
         * 绘制有效的进度条弧线
         */
        //设置圆环的宽度
        paint.setStrokeWidth(progressStrokeWidth);
        //设置进度的颜色
        paint.setColor(progressColor);
        paint.setStrokeCap(Paint.Cap.ROUND);
        //用于定义的圆弧的形状和大小的界限
        mArcRectF.left = center - radius;
        mArcRectF.top = center - radius;
        mArcRectF.right = center + radius;
        mArcRectF.bottom = center + radius;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(mArcRectF, 90 - 180 * ((float) progressValue / (float) maxValue), 360 * progressValue / maxValue, false, paint);  //根据进度画圆弧
    }


    /**
     * 弧线动画
     *
     * @param last    起始值
     * @param current 最终值
     * @param length  动画时间
     */
    private void setAnimation(float last, float current, int length) {
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                progressValue = (int) value;
                invalidate();
            }
        });
        progressAnimator.start();
    }
}
