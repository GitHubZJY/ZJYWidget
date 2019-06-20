package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.zjywidget.widget.switchview.YSwitchView;

/**
 * Created by 74215 on 2019/4/14.
 */

public class YSwitchTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_view);

        YSwitchView switchView = findViewById(R.id.switch_5);
        switchView.setCheck(false);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("YSwitchView");
        }
    }
}
