package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.example.zjy.zjywidget.sample.base.BaseTestActivity;
import com.zjywidget.widget.banner.YBannerView;
import com.zjywidget.widget.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yang on 2019/2/12.
 */

public class BannerViewTestActivity extends BaseTestActivity {

    private YBannerView mBannerView;

    @Override
    protected String getTitleStr() {
        return "YBannerView";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bannerl_view);

        mBannerView = findViewById(R.id.banner_view);
        mBannerView.setIndicator(new CircleIndicator(this));
        List<String> bannerData = new ArrayList<>();
        bannerData.add("http://pic41.photophoto.cn/20161217/0017030086344808_b.jpg");
        bannerData.add("http://photocdn.sohu.com/20150114/Img407794285.jpg");
        bannerData.add("http://img.zcool.cn/community/015372554281b00000019ae9803e5c.jpg");
        mBannerView.setBannerData(bannerData);
    }
}
