package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.example.zjy.zjywidget.sample.base.BaseTestActivity;
import com.zjywidget.widget.switchview.YSwitchView;

/**
 * Created by Yang on 2019/4/14.
 */

public class YSwitchTestActivity extends BaseTestActivity {

    @Override
    protected String getTitleStr() {
        return "YSwitchView";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_view);

        YSwitchView switchView = findViewById(R.id.switch_5);
        switchView.setCheck(false);
    }
}
