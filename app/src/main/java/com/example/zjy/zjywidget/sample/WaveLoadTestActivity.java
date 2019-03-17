package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.zjywidget.widget.waveload.YWaveLoadView;

/**
 * Created by 74215 on 2019/3/17.
 */

public class WaveLoadTestActivity extends AppCompatActivity{

    private YWaveLoadView mLoadView1;
    private YWaveLoadView mLoadView2;
    private YWaveLoadView mLoadView3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_load);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("YWaveLoadView");
        }

        mLoadView1 = findViewById(R.id.wave_load_1);
        mLoadView2 = findViewById(R.id.wave_load_2);
        mLoadView3 = findViewById(R.id.wave_load_3);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadView2.startLoad();
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadView3.startLoad();
            }
        }, 2000);
    }
}
