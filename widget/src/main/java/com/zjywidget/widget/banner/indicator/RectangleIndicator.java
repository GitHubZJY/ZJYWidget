package com.zjywidget.widget.banner.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * 小矩形指示器
 */
public class RectangleIndicator extends BaseIndicator {

    public RectangleIndicator(Context context) {
        super(context);
    }

    @Override
    public float getCellMargin() {
        return dp2px(4);
    }

    @Override
    public float getCellWidth() {
        return dp2px(6);
    }

    @Override
    protected IndicatorCell getCellView() {
        return new RectangleCell(getContext());
    }

    public class RectangleCell extends IndicatorCell {

        public RectangleCell(Context context) {
            super(context);
        }

        @Override
        public void select() {
            mPaint.setColor(Color.parseColor("#ffffff"));
        }

        @Override
        public void unSelect() {
            mPaint.setColor(Color.parseColor("#000000"));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawRect(0, 0, getCellWidth(), getCellWidth(), mPaint);
        }
    }
}
