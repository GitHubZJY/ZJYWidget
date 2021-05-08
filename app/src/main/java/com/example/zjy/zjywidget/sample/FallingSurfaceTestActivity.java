package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.example.zjy.zjywidget.sample.base.BaseTestActivity;
import com.zjywidget.widget.fallingsurface.FallingSurfaceView;

/**
 * Created by Yang on 2019/4/14.
 */

public class FallingSurfaceTestActivity extends BaseTestActivity {

    @Override
    protected String getTitleStr() {
        return "YFallingSurfaceView";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falling_surface);

        FallingSurfaceView switchView = findViewById(R.id.falling_view);
        switchView.run();
    }
}
