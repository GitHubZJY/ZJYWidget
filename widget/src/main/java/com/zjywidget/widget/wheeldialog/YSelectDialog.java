package com.zjywidget.widget.wheeldialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import com.zjywidget.widget.R;

import java.util.ArrayList;
import java.util.List;


public class YSelectDialog extends Dialog {

    private View vContentView;
    private YWheelView vWheelView;
    private List<String> mItems = new ArrayList<>();
    private OnActionListener mActionListener;

    public YSelectDialog(Context context) {
        super(context, R.style.BaseDialog);
        init();
    }

    protected void init() {
        //默认点击黑色背景不能关闭
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        if (window == null) {
            return;
        }
        //去掉白色的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //获取需要显示的宽、高
        DisplayMetrics metrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //去掉默认间距
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        vContentView = getLayoutInflater().inflate(R.layout.base_select_popup_window_layout, null);
        setContentView(vContentView);
        initView(vContentView);
    }

    private void initView(View view){
        vWheelView = view.findViewById(R.id.wheel_view);
    }

    private void bindView(){
        if (mItems.size() > 0) {
            vWheelView.bindData(mItems, mItems.size() / 2);
            vWheelView.setWheelViewSelectedListener(new YWheelView.IWheelViewSelectedListener() {
                @Override
                public void wheelViewSelectedChanged(YWheelView wheelView, List<String> data, int position) {
                    if (mActionListener != null) {
                        mActionListener.onChooseOperation(mItems.get(position));
                    }
                }
            });
        }
    }

    public YSelectDialog bindData(List<? extends String> operationList) {
        mItems.clear();
        mItems.addAll(operationList);
        bindView();
        return this;
    }

    public interface OnActionListener {
        /**
         * 当选择时会回调
         *
         * @param operationModel 选择的操作的模型
         */
        void onChooseOperation(String operationModel);
    }

    @Override
    public void show() {
        super.show();
        if (vContentView == null) {
            return;
        }
        TranslateAnimation animation = new TranslateAnimation(1, 0.0F,
                1, 0.0F, 1, 1.0F, 1, 0.0F);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(180L);
        animation.setFillAfter(true);
        vContentView.startAnimation(animation);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (vContentView == null) {
            return;
        }
        TranslateAnimation animation = new TranslateAnimation(1, 0.0F,
                1, 0.0F, 1, 0.0F, 1, 1.0F);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(180L);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismissDialog();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        vContentView.startAnimation(animation);
    }

    private void dismissDialog() {
        super.dismiss();
    }

    public void setOnActionListener(OnActionListener actionListener) {
        mActionListener = actionListener;
    }

}