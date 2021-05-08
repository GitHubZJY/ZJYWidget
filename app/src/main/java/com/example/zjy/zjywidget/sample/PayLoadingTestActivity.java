package com.example.zjy.zjywidget.sample;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.zjy.zjywidget.R;
import com.example.zjy.zjywidget.sample.base.BaseTestActivity;
import com.zjywidget.widget.paystatusview.YPayLoadingView;

/**
 * Created by Yang on 2019/2/19.
 */

public class PayLoadingTestActivity extends BaseTestActivity implements View.OnClickListener{

    YPayLoadingView mPayLoadingView;
    Button mStartBtn, mSuccessBtn, mFailBtn;


    @Override
    protected String getTitleStr() {
        return "YPayLoadingView";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_loading);

        mPayLoadingView = findViewById(R.id.pay_loading_view);
        mStartBtn = findViewById(R.id.start_btn);
        mSuccessBtn = findViewById(R.id.success_btn);
        mFailBtn = findViewById(R.id.fail_btn);
        mStartBtn.setOnClickListener(this);

        mSuccessBtn.setOnClickListener(this);
        mFailBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_btn:
                mPayLoadingView.startLoading();
                break;
            case R.id.success_btn:
                mPayLoadingView.showResult(true);
                break;
            case R.id.fail_btn:
                mPayLoadingView.showResult(false);
                break;
        }
    }
}
