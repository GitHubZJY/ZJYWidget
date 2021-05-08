package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zjy.zjywidget.R;
import com.example.zjy.zjywidget.sample.base.BaseTestActivity;
import com.zjywidget.widget.skillview.YSkillView;

/**
 * Created by Yang on 2019/2/12.
 */

public class SkillViewTestActivity extends BaseTestActivity {

    private YSkillView mSkillView;

    @Override
    protected String getTitleStr() {
        return "YSkillView";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_view);

        mSkillView = findViewById(R.id.skill_view);
        mSkillView.setScore(new float[]{1,2,5,4,3,5});
        mSkillView.setTextArr(new String[]{"团战", "生存", "KDA", "发育", "输出", "推进"});
    }
}
