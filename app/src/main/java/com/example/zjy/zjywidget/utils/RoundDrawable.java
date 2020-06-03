package com.example.zjy.zjywidget.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Created by YANG on 2018/3/1.
 * 提交圆角图标染色工具类
 * 使用示例: new RoundDrawable(color).setRadius(5).build();
 */

public class RoundDrawable {

    //透明度
    private int alpha = 100;
    //边界线条宽度
    private int strokeWidth = 10;
    //边界线条颜色
    private int strokeColor;
    //虚线
    private float dashWidth;
    private float dashGap;
    //填充颜色
    private int color;
    //所有角的角度
    private int radius;
    //外矩形 左上、右上、右下、左下的圆角半径
    private float[] radiusArr;
    //是否填充
    private boolean isFill;
    //Drawable染色源
    private Drawable drawable;

    public RoundDrawable(int color) {
        this.color = color;
        this.isFill = true;
        this.radiusArr = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
        this.alpha = 255;
        this.strokeWidth = 10;
    }

    public RoundDrawable(Drawable drawable, int color) {
        this.drawable = drawable;
        this.color = color;
    }

    public RoundDrawable setAlpha(int alpha) {
        this.alpha = alpha;
        return this;
    }

    public RoundDrawable setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public RoundDrawable setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public RoundDrawable setDashWidth(float dashWidth) {
        this.dashWidth = dashWidth;
        return this;
    }

    public RoundDrawable setDashGap(float dashGap) {
        this.dashGap = dashGap;
        return this;
    }

    public RoundDrawable setColor(int color) {
        this.color = color;
        return this;
    }

    public RoundDrawable setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public RoundDrawable setRadiusArr(float[] radiusArr) {
        this.radiusArr = radiusArr;
        return this;
    }

    public RoundDrawable setFill(boolean fill) {
        isFill = fill;
        return this;
    }

    public Drawable build() {
        if (drawable != null) {
            return drawColor(drawable, color);
        }
        strokeColor = color;
        GradientDrawable drawable = new GradientDrawable();
        drawable.setAlpha(alpha);
        if (radius != 0) {
            drawable.setCornerRadius(radius);
        } else {
            drawable.setCornerRadii(radiusArr);
        }
        drawable.setColor(isFill ? color : Color.TRANSPARENT);
        drawable.setStroke(isFill ? 0 : strokeWidth, strokeColor, dashWidth, dashGap);
        return drawable;
    }


    public static Drawable getCircleDrawable(int color) {
        return new RoundDrawable(color).setRadius(180).build();
    }

    /**
     * 将drawable颜色着色为color
     *
     * @param drawable
     * @param color
     * @return 重绘后的Drawable
     */
    public Drawable drawColor(Drawable drawable, int color) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        wrappedDrawable.mutate();
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }
}
