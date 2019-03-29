package com.zjywidget.widget.banner.transformers;

import android.view.View;

public class CubePageTransformer extends BasePageTransformer {
    private float mMaxRotation = 90.0f;

    public CubePageTransformer() {
    }

    public CubePageTransformer(float maxRotation) {
        setMaxRotation(maxRotation);
    }

    @Override
    public void handleInvisiblePage(View view, float position) {
        view.setPivotX(view.getMeasuredWidth());
        view.setPivotY( view.getMeasuredHeight() * 0.5f);
        view.setRotationY(0);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        view.setPivotX(view.getMeasuredWidth());
        view.setPivotY( view.getMeasuredHeight() * 0.5f);
        view.setRotationY(mMaxRotation * position);
    }

    @Override
    public void handleRightPage(View view, float position) {
        view.setPivotX( 0);
        view.setPivotY(view.getMeasuredHeight() * 0.5f);
        view.setRotationY( mMaxRotation * position);
    }

    public void setMaxRotation(float maxRotation) {
        if (maxRotation >= 0.0f && maxRotation <= 90.0f) {
            mMaxRotation = maxRotation;
        }
    }

}
