package com.example.zjy.zjywidget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zjy.zjywidget.sample.ArcMenuTestActivity;
import com.example.zjy.zjywidget.sample.BannerViewTestActivity;
import com.example.zjy.zjywidget.sample.CameraViewTestActivity;
import com.example.zjy.zjywidget.sample.CircleProgressTestActivity;
import com.example.zjy.zjywidget.sample.PayLoadingTestActivity;
import com.example.zjy.zjywidget.sample.SkillViewTestActivity;
import com.example.zjy.zjywidget.sample.WaveLoadTestActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 各个自定义View demo的入口列表
 * Created by YANG on 2018/02/12.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView mViewListView;
    private EntranceItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ViewItemBean> viewItemBeans = new ArrayList<>();
        viewItemBeans.add(new ViewItemBean("YCameraMarkView", "一个带遮罩扫描动画的相机拍摄View,支持自定义裁剪大小、形状,前后置切换,扫描动画", R.raw.camera_mark_view, CameraViewTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YWaveLoadingView", "可定制Icon的水波纹进度球，同时支持设置纯色填充水波纹", R.raw.wave_load_view, WaveLoadTestActivity.class));
        viewItemBeans.add(new ViewItemBean("skill_view", "带动画的6变形技能图,支持自定义颜色以及属性文案,动态设置分数", R.raw.skill_view, SkillViewTestActivity.class));
        viewItemBeans.add(new ViewItemBean("banner_view", "封装ViewPager快速集成轮播功能,支持自定义指示器", R.raw.banner_view, BannerViewTestActivity.class));
        viewItemBeans.add(new ViewItemBean("circle_progressbar", "带动画的弧形进度条,可自定义颜色,宽度,文案,百分比", R.raw.circle_progressbar, CircleProgressTestActivity.class));
        viewItemBeans.add(new ViewItemBean("arc_menu", "常见的弧形弹出菜单(卫星导航菜单),以一个按钮为中心，扇形展开菜单子项", R.raw.arc_menu, ArcMenuTestActivity.class));
        viewItemBeans.add(new ViewItemBean("pay_loading_view", "仿支付宝的支付成功失败动画，可定制颜色、粗细、动画频率", R.raw.pay_loading_view, PayLoadingTestActivity.class));

        mAdapter = new EntranceItemAdapter(this, viewItemBeans);

        mViewListView = findViewById(R.id.view_lv);
        mViewListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mViewListView.setAdapter(mAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
