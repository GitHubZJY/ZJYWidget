package com.example.zjy.zjywidget;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zjy.zjywidget.sample.ArcMenuTestActivity;
import com.example.zjy.zjywidget.sample.BannerViewTestActivity;
import com.example.zjy.zjywidget.sample.CameraViewTestActivity;
import com.example.zjy.zjywidget.sample.CircleProgressTestActivity;
import com.example.zjy.zjywidget.sample.FallingSurfaceTestActivity;
import com.example.zjy.zjywidget.sample.PayLoadingTestActivity;
import com.example.zjy.zjywidget.sample.ScratchTestActivity;
import com.example.zjy.zjywidget.sample.SkillViewTestActivity;
import com.example.zjy.zjywidget.sample.WaveLoadTestActivity;
import com.example.zjy.zjywidget.sample.YRoundelMenuActivity;
import com.example.zjy.zjywidget.sample.YSwitchTestActivity;

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
        viewItemBeans.add(new ViewItemBean("YRoundelMenu", "一个小巧精致的弹出式圆盘菜单，支持添加多个子View", R.raw.roundel_menu, YRoundelMenuActivity.class));
        viewItemBeans.add(new ViewItemBean("YCameraMarkView", "一个带遮罩扫描动画的相机拍摄View,支持自定义裁剪大小、形状,前后置切换,扫描动画", R.raw.camera_mark_view, CameraViewTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YWaveLoadingView", "可定制Icon的水波纹进度球，同时支持设置纯色填充水波纹", R.raw.wave_load_view, WaveLoadTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YSkillView", "带动画的6变形技能图,支持自定义颜色以及属性文案,动态设置分数", R.raw.skill_view, SkillViewTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YBannerView", "封装ViewPager快速集成轮播功能,支持自定义指示器", R.raw.banner_view, BannerViewTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YCircleProgressBar", "带动画的弧形进度条,可自定义颜色,宽度,文案,百分比", R.raw.circle_progressbar, CircleProgressTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YArcMenuView", "常见的弧形弹出菜单(卫星导航菜单),以一个按钮为中心，扇形展开菜单子项", R.raw.arc_menu, ArcMenuTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YPayLoadingView", "仿支付宝的支付成功失败动画，可定制颜色、粗细、动画频率", R.raw.pay_loading_view, PayLoadingTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YScratchView", "常见的刮刮乐效果，支持更换底图和布局，支持刮出结果的监听", R.raw.scratch_view, ScratchTestActivity.class));
        viewItemBeans.add(new ViewItemBean("YFallingSurfaceView", "直播交互常见的红包雨效果，自定义红包数量、红包样式、降落速度", R.raw.falling_surface, FallingSurfaceTestActivity.class));

        //viewItemBeans.add(new ViewItemBean("YSwitchView", "高仿IOS风格的开关控件，包括过渡动画，支持定制颜色、大小、切换动画时长", R.raw.switch_view, YSwitchTestActivity.class));

        mAdapter = new EntranceItemAdapter(this, viewItemBeans);

        mViewListView = findViewById(R.id.view_lv);
        mViewListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mViewListView.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_github) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://github.com/GitHubZJY/ZJYWidget"));
            startActivity(Intent.createChooser(intent, null));
            return true;
        }
        return super.onOptionsItemSelected(item);

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
