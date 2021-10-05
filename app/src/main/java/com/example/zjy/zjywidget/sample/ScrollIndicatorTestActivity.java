package com.example.zjy.zjywidget.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.zjy.zjywidget.R;
import com.zjywidget.widget.indicator.YStickIndicator;

import java.util.ArrayList;
import java.util.List;

public class ScrollIndicatorTestActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private YStickIndicator indicator;
    private Button addBtn;
    List<Integer> itemList = new ArrayList<>();
    MyPageAdapter pageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_indicator);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("YStickIndicator");
        }

        viewPager = findViewById(R.id.view_pager);
        indicator = findViewById(R.id.indicator);
        addBtn = findViewById(R.id.add_btn);

        addBtn.setOnClickListener(view -> {
            itemList.add(Color.DKGRAY);
            indicator.setTotalCount(itemList.size());
            viewPager.setAdapter(pageAdapter);
        });

        itemList.add(Color.RED);
        itemList.add(Color.BLUE);
        itemList.add(Color.YELLOW);
        itemList.add(Color.GREEN);
        itemList.add(Color.LTGRAY);
        itemList.add(Color.MAGENTA);
        itemList.add(Color.BLACK);
        itemList.add(Color.CYAN);
        pageAdapter = new MyPageAdapter(itemList);
        viewPager.setAdapter(pageAdapter);

        indicator.setTotalCount(itemList.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setCurIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private View generateItem(int pos, int color) {
        View itemView = new View(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        itemView.setLayoutParams(params);
        itemView.setBackgroundColor(color);

        itemView.setOnClickListener(view -> {
            itemList.remove(pos);
            indicator.setTotalCount(itemList.size());
            viewPager.setAdapter(pageAdapter);
        });
        return itemView;
    }


    public class MyPageAdapter extends PagerAdapter {

        private List<Integer> itemList = new ArrayList<>();

        public MyPageAdapter(List<Integer> list) {
            this.itemList = list;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = generateItem(position, itemList.get(position));
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
