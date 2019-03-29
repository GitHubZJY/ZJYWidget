package com.zjywidget.widget.banner.transformers;

import android.view.View;

public class ScalePageTransformer extends BasePageTransformer {
    private static final float MIN_SCALE = 0.9F;

    @Override
    public void handleInvisiblePage(View view, float position) {
        view.setScaleY(MIN_SCALE);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        float scale = Math.max(MIN_SCALE,1 - Math.abs(position));
        view.setScaleY(scale);
    }

    @Override
    public void handleRightPage(View view, float position) {
        float scale = Math.max(MIN_SCALE,1 - Math.abs(position));
        view.setScaleY(scale);
    }
}
