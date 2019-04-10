package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.zjywidget.widget.banner.YBannerView;
import com.zjywidget.widget.banner.indicator.CircleIndicator;
import com.zjywidget.widget.banner.indicator.RectangleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 74215 on 2019/2/12.
 */

public class BannerViewTestActivity extends AppCompatActivity {

    private YBannerView mBannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bannerl_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("YBannerView");
        }

        mBannerView = findViewById(R.id.banner_view);
        mBannerView.setIndicator(new CircleIndicator(this));
        List<String> bannerData = new ArrayList<>();
        bannerData.add("http://pic41.photophoto.cn/20161217/0017030086344808_b.jpg");
        bannerData.add("http://photocdn.sohu.com/20150114/Img407794285.jpg");
        bannerData.add("http://img.zcool.cn/community/015372554281b00000019ae9803e5c.jpg");
        mBannerView.setBannerData(bannerData);
    }
}
