package com.zjywidget.widget.banner.indicator;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;

/**
 * 指示器单元
 */
public abstract class IndicatorCell extends View {

    protected Paint mPaint;

    public IndicatorCell(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    /**
     * 选中时的绘制，子类自定义实现
     */
    abstract void select();

    /**
     * 未选中时的绘制，子类自定义实现
     */
    abstract void unSelect();

}
