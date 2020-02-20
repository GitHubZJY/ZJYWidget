package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.zjywidget.widget.fallingsurface.FallingSurfaceView;
import com.zjywidget.widget.switchview.YSwitchView;

/**
 * Created by 74215 on 2019/4/14.
 */

public class FallingSurfaceTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falling_surface);

        FallingSurfaceView switchView = findViewById(R.id.falling_view);
        switchView.run();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("YFallingSurfaceView");
        }
    }
}
