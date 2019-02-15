package com.example.zjy.zjywidget.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zjy.zjywidget.R;
import com.zjywidget.widget.camerapreview.YCameraMarkView;

public class CameraViewTestActivity extends AppCompatActivity {

    private YCameraMarkView mCameraView;
    private ImageView mTakeBtn;
    private TextView mLoadingTipTv;

    private Handler handler = new Handler(Looper.getMainLooper());

    private boolean mIsTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mCameraView = findViewById(R.id.camera_view);
        mTakeBtn = findViewById(R.id.take_btn);
        mLoadingTipTv = findViewById(R.id.loading_tip);

//        mCameraView.setDuration(1000);
//        mCameraView.setOverlayAlpha(0.6f);
//        mCameraView.setCropEnable(true);
//        mCameraView.setCropSize(600, 600);
//        mCameraView.setBack();
//        mCameraView.setFlash(true);
//        mCameraView.setCropMode(CameraMarkView.CROP_RECT);
//        mCameraView.setShowScan(false);
//        mCameraView.setStrokeWidth(5.0f);

        mTakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsTaken){
                    mCameraView.start();
                    mTakeBtn.setBackground(getResources().getDrawable(R.drawable.ic_camera));
                    mIsTaken = false;
                    return;
                }
                mCameraView.takePic();
                mTakeBtn.setVisibility(View.GONE);
                mLoadingTipTv.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsTaken = true;
                        mCameraView.stopScan();
                        mTakeBtn.setVisibility(View.VISIBLE);
                        mTakeBtn.setBackground(getResources().getDrawable(R.drawable.icn_retry));
                        mLoadingTipTv.setVisibility(View.GONE);
                    }
                }, 6000);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mIsTaken){
            return;
        }
        mCameraView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraView.stop();
    }
}
