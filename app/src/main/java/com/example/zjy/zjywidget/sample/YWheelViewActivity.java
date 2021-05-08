package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.zjy.zjywidget.R;
import com.example.zjy.zjywidget.sample.base.BaseTestActivity;
import com.zjywidget.widget.wheeldialog.YSelectDialog;
import com.zjywidget.widget.wheeldialog.YWheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yang on 2019/4/14.
 */
public class YWheelViewActivity extends BaseTestActivity {

    @Override
    protected String getTitleStr() {
        return "YWheelView";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_view);

        YWheelView wheelView = findViewById(R.id.wheel_view);
        List<String> mItems = new ArrayList<>();
        mItems.add("广州");
        mItems.add("上海");
        mItems.add("北京");
        mItems.add("深圳");
        mItems.add("杭州");
        wheelView.bindData(mItems, mItems.size() / 2);
        wheelView.setWheelViewSelectedListener(new YWheelView.IWheelViewSelectedListener() {
            @Override
            public void wheelViewSelectedChanged(YWheelView wheelView, List<String> data, int position) {
            }
        });

        Button showDialogBtn = findViewById(R.id.show_dialog_btn);
        showDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> data = new ArrayList<>();
                data.add("选项1");
                data.add("选项2");
                data.add("选项3");
                data.add("选项4");
                data.add("选项5");
                YSelectDialog dialog = new YSelectDialog(YWheelViewActivity.this);
                dialog.show();
                dialog.bindData(data);
            }
        });
    }
}
