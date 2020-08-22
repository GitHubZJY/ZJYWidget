package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.zjywidget.widget.profileslide.YProfileSlideView;

/**
 * Created by 74215 on 2019/4/14.
 */

public class YProfileSlideTestActivity extends AppCompatActivity {

    private YProfileSlideView vSlideView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_slide);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("YProfileSlide");
        }

        vSlideView = findViewById(R.id.slide_view);
        vSlideView.addSlideListener(new YProfileSlideView.SlideListener() {
            @Override
            public void slideEnd() {
                Toast.makeText(YProfileSlideTestActivity.this, "解锁", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
