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
        viewItemBeans.add(new ViewItemBean("YCameraMarkView", "一个带遮罩扫描动画的相机拍摄View,支持自定义裁剪大小、形状,前后置切换,扫描动画", CameraViewTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YSkillView", "带动画的6变形技能图,支持自定义颜色以及属性文案,动态设置分数", SkillViewTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YBannerView", "封装ViewPager快速集成轮播功能,支持自定义指示器", BannerViewTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YCircleProgressBar", "带动画的弧形进度条,可自定义颜色,宽度,文案,百分比", CircleProgressTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YArcMenuView", "常见的弧形弹出菜单(卫星导航菜单),以一个按钮为中心，扇形展开菜单子项", ArcMenuTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YPayLoadingView", "仿支付宝的支付成功失败动画，可定制颜色、粗细、动画频率", PayLoadingTestActivity.class));
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
