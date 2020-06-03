package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zjy.zjywidget.R;
import com.zjywidget.widget.scratchview.YScratchView;

/**
 * Created by Yang on 2019/5/12.
 * 刮刮乐View 测试界面
 */

public class ScratchTestActivity extends AppCompatActivity{

    private YScratchView mScratchView;
    private TextView mTipTv;
    private TextView mResultTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("YScratchView");
        }

        mScratchView = findViewById(R.id.scratch_view);
        mTipTv = findViewById(R.id.tip_tv);
        mResultTv = findViewById(R.id.text_view);

        mScratchView.setScratchListener(new YScratchView.ScratchListener() {
            @Override
            public void finish() {
                mTipTv.setVisibility(View.VISIBLE);
            }
        });

        mResultTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "点击了结果布局", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
