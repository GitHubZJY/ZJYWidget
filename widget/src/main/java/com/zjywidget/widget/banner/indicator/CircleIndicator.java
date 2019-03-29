package com.zjywidget.widget.banner.indicator;

import android.content.Context;
import android.graphics.Color;

public class CircleIndicator extends BaseIndicator{

    public CircleIndicator(Context context) {
        super(context);
    }

    @Override
    public int getCellMargin() {
        return dp2px(4);
    }

    @Override
    public int getCellRadius() {
        return dp2px(3);
    }

    @Override
    public int getSelectColor() {
        return Color.parseColor("#000000");
    }

    @Override
    public int getUnSelectColor() {
        return Color.parseColor("#ffffff");
    }

    @Override
    public int getType() {
        return CIRCLE;
    }
}
