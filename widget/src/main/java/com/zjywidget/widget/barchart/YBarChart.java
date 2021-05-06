package com.zjywidget.widget.barchart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.zjywidget.widget.R;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * <b>Project:</b> https://github.com/GitHubZJY/ZJYWidget <br>
 * <b>Create Date:</b> 2021/5/6 <br>
 * <b>@author:</b> Yang <br>
 * <b>Description:</b> 带动画效果的自定义柱形图 <br>
 */
public class YBarChart extends View {

    private static final String TAG = YBarChart.class.getSimpleName();

    /**
     * 柱形数据
     */
    private float[] valueData;
    /**
     * 名称数据
     */
    private String[] nameData;
    /**
     * 垂直方向上每一段的高度
     */
    private float itemHeight;
    /**
     * 分割线数量
     */
    private int lineCount;
    /**
     * 分割线及坐标系颜色
     */
    private int lineColor;
    /**
     * 数值颜色
     */
    private int dataColor;
    /**
     * 柱形图填充颜色
     */
    private int columnColor;
    /**
     * 是否显示动画
     */
    private boolean isAnim;
    //柱形图的动画器
    private ValueAnimator animator;
    //当前的动画进度
    private float curProgress;
    //用于统一浮点型数值的格式
    private NumberFormat numberFormat;
    //柱形的最大值
    private float maxValue;
    //每两个分割线之间代表的值（即垂直方向上的每一段）
    private int splitValue;

    private Paint paint;

    public YBarChart(Context context) {
        this(context, null);
    }

    public YBarChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YBarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleStyleable(context, attrs, defStyleAttr);
        init();
    }

    private void handleStyleable(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.YScratchView, defStyle, 0);
        try {
            lineCount = ta.getInteger(R.styleable.YBarChart_bar_chart_line_count, 5);
            lineColor = ta.getColor(R.styleable.YBarChart_bar_chart_line_color, Color.parseColor("#666666"));
            dataColor = ta.getColor(R.styleable.YBarChart_bar_chart_data_color, Color.parseColor("#000000"));
            columnColor = ta.getColor(R.styleable.YBarChart_bar_chart_column_color, Color.parseColor("#F47817"));
            isAnim = ta.getBoolean(R.styleable.YBarChart_bar_chart_anim, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }
    }

    private void init() {
        paint = new Paint();

        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(1);

        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (!isAnim) {
                    return;
                }
                curProgress = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        if (isAnim) {
            animator.start();
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        itemHeight = (bottom - top - 150) / (lineCount * 1.0f);
    }

    public void setData(String[] nameData, float[] valueData) {
        if (nameData.length != valueData.length) {
            Log.d(TAG, "The length of nameData should equal to the valueData!");
            return;
        }
        maxValue = 0;
        count = 0;
        for (float element : valueData) {
            maxValue = Math.max(maxValue, element);
        }

        try {
            int decimal;
            int units = getUnits(maxValue);
            if (units > 0) {
                decimal = (int) Math.pow(10, units);
                int count = (int) maxValue / 10;
                if (maxValue % decimal == 0) {
                    splitValue = count * 10 / lineCount;
                } else {
                    splitValue = (count + 1) * 10 / lineCount;
                }
            } else {
                splitValue = 2;
            }
        } catch (Exception e) {

        }

        this.valueData = valueData;
        this.nameData = nameData;
        if (animator != null) {
            animator.isStarted();
            animator.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (valueData == null) {
            return;
        }
        paint.reset();
        paint.setColor(lineColor);
        float maxTextWidth = 0;
        float lineLeft = 0;
        float bottomLineY = 0;

        //绘制分界线和左侧数值
        for (int i = lineCount; i >= 0; i--) {
            String text = splitValue * i + "";
            paint.setTextSize(30);
            if (maxTextWidth == 0) {
                maxTextWidth = paint.measureText(text);
                lineLeft = maxTextWidth + 20;
            }
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            float lineY = itemHeight * (lineCount - i) + 80;
            float textY = lineY + (fontMetrics.descent - fontMetrics.ascent) / 4;
            canvas.drawText(text, 0, textY, paint);
            canvas.drawLine(lineLeft, lineY, getWidth(), lineY, paint);
            if (i == 0) {
                bottomLineY = lineY;
            }
        }

        //绘制柱形图和数据
        float dataWidth = 60;
        float margin = ((getWidth() - lineLeft - dataWidth * valueData.length) / valueData.length);
        for (int j = 0; j < valueData.length; j++) {
            paint.setColor(columnColor);
            float left;
            if (j == 0) {
                left = lineLeft + j * dataWidth + margin / 2f;
            } else {
                left = lineLeft + j * dataWidth + margin / 2f + margin * j;
            }

            float right = left + dataWidth;
            float top = bottomLineY - (valueData[j] / (splitValue * lineCount)) * (getHeight() - 150) * curProgress;
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawRect(left, top, right, bottomLineY, paint);

            paint.setTextSize(40);
            paint.setColor(dataColor);
            canvas.drawText(formatProgressValue(valueData[j] * curProgress), left + dataWidth / 2, top - 20, paint);
            paint.setTextSize(30);
            paint.setColor(lineColor);
            canvas.drawText(nameData[j] + "", left + dataWidth / 2, bottomLineY + 40, paint);
        }

    }

    private String formatProgressValue(float progress) {
        BigDecimal bigInterestRate = new BigDecimal(progress);
        return numberFormat.format(bigInterestRate);
    }

    int count = 0;

    /**
     * 获取数值整数位数
     */
    private int getUnits(float last) {
        float s = last / 10;
        if (s > 1) {
            count++;
            getUnits(s);
        }
        return count;
    }
}
