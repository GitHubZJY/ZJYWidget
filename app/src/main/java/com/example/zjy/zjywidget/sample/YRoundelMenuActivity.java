package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.example.zjy.zjywidget.sample.base.BaseTestActivity;

/**
 * Created by Yang on 2019/4/14.
 */

public class YRoundelMenuActivity extends BaseTestActivity {

    @Override
    protected String getTitleStr() {
        return "YRoundelMenu";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roundel_menu);
    }
}
