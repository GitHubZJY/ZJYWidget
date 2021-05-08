package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.example.zjy.zjywidget.sample.base.BaseTestActivity;
import com.zjywidget.widget.profileslide.YProfileSlideView;

/**
 * Created by Yang on 2019/4/14.
 */

public class YProfileSlideTestActivity extends BaseTestActivity {

    private YProfileSlideView vSlideView;

    @Override
    protected String getTitleStr() {
        return "YProfileSlide";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_slide);

        vSlideView = findViewById(R.id.slide_view);
        vSlideView.addSlideListener(new YProfileSlideView.SlideListener() {
            @Override
            public void slideEnd() {
                Toast.makeText(YProfileSlideTestActivity.this, "解锁", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
