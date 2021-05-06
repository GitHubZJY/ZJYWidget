package com.example.zjy.zjywidget.sample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.zjywidget.widget.barchart.YBarChart;
import com.zjywidget.widget.switchview.YSwitchView;

/**
 * Created by 74215 on 2019/4/14.
 */
public class BarChartTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        YBarChart barChart = findViewById(R.id.bar_chart_view);
        String[] nameData = new String[] {"名称1","名称2","名称3","名称4","名称5","名称6"};
        float[] valueData = new float[]{0.6f, 0.9f, 1.5f, 4.4f, 0.2f, 2.3f};
        barChart.setData(nameData, valueData);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("YBarChart");
        }
    }
}
