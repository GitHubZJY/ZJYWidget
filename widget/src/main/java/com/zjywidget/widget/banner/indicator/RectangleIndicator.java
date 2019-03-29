package com.zjywidget.widget.banner.indicator;

import android.content.Context;
import android.graphics.Color;

public class RectangleIndicator extends BaseIndicator{

    public RectangleIndicator(Context context) {
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
        return RECTANGLE;
    }
}
