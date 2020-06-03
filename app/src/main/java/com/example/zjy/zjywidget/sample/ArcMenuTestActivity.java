package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.example.zjy.zjywidget.R;
import com.zjywidget.widget.arcmenu.YArcMenuView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 74215 on 2019/2/12.
 */

public class ArcMenuTestActivity extends AppCompatActivity {

    private YArcMenuView mArcMenuView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_menu);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("YArcMenuView");
        }

        mArcMenuView = findViewById(R.id.arc_menu);
        List<Integer> menuItems = new ArrayList<>();
        menuItems.add(R.drawable.ic_menu_camera);
        menuItems.add(R.drawable.ic_menu_photo);
        menuItems.add(R.drawable.ic_menu_share);
        mArcMenuView.setMenuItems(menuItems);

        mArcMenuView.setClickItemListener(new YArcMenuView.ClickMenuListener() {
            @Override
            public void clickMenuItem(int resId) {
                switch (resId){
                    case R.drawable.ic_menu_camera:
                        Toast.makeText(getApplicationContext(), "点击了相机", Toast.LENGTH_SHORT).show();
                        break;
                    case R.drawable.ic_menu_photo:
                        Toast.makeText(getApplicationContext(), "点击了相册", Toast.LENGTH_SHORT).show();
                        break;
                    case R.drawable.ic_menu_share:
                        Toast.makeText(getApplicationContext(), "点击了分享", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
