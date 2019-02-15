package com.zjywidget.widget.camerapreview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zjywidget.widget.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @describe 支持裁剪扫描的遮罩相机
 * @author YANG
 * @data 2019/02/11
 */
public class YCameraMarkView extends FrameLayout {

    //默认开启裁剪
    private static final boolean DEFAULT_CROP_ENABLE = true;
    //默认裁剪框宽度为280dp
    public static final int FRAME_DEFAULT_WIDTH = 600;
    //默认裁剪框高度为280dp
    public static final int FRAME_DEFAULT_HEIGHT = 600;
    //默认遮罩层透明度为60%
    private static final float OVERLAY_DEFAULT_ALPHA = 0.6f;

    //是否开启裁剪
    private boolean mCropEnable;
    //裁剪模式
    private int mCropMode = CROP_CIRCLE;
    //矩形
    public static final int CROP_RECT = 1;
    //圆形
    public static final int CROP_CIRCLE = 2;

    //拍摄结果存储路径
    private String mSavePath = Environment.getExternalStorageDirectory() + "/" + "test.png";

    //相机
    private CameraView mCameraView;
    //遮罩层
    private MarkView mMarkView;
    //扫描层
    private ScanView mScanView;

    public YCameraMarkView(@NonNull Context context) {
        this(context, null);
    }

    public YCameraMarkView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YCameraMarkView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mCameraView = new CameraView(context);
        mCameraView.setLayoutParams(params);
        addView(mCameraView);
        mMarkView = new MarkView(context);
        mMarkView.setLayoutParams(params);
        addView(mMarkView);
        mScanView = new ScanView(context);
        mScanView.setLayoutParams(params);
        addView(mScanView);

        mCameraView.addCallback(mCameraCallback);

        handleStyleable(context, attrs, defStyleAttr);
    }

    private void handleStyleable(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CameraMarkView, defStyle, 0);
        try {
            float overlayAlpha = ta.getFloat(R.styleable.CameraMarkView_overlay_alpha, OVERLAY_DEFAULT_ALPHA);
            boolean cropEnable = ta.getBoolean(R.styleable.CameraMarkView_crop_enabled, DEFAULT_CROP_ENABLE);
            float cropWidth = ta.getDimensionPixelSize(R.styleable.CameraMarkView_crop_width, FRAME_DEFAULT_WIDTH);
            float cropHeight = ta.getDimensionPixelSize(R.styleable.CameraMarkView_crop_height, FRAME_DEFAULT_HEIGHT);
            boolean isFlash = ta.getBoolean(R.styleable.CameraMarkView_flash_enable, false);
            boolean isShowScan = ta.getBoolean(R.styleable.CameraMarkView_scan_enable, true);
            float strokeWidth = ta.getDimensionPixelSize(R.styleable.CameraMarkView_frame_stroke_weight, 2);
            int frameColor = ta.getColor(R.styleable.CameraMarkView_frame_color, Color.WHITE);
            int cropMode = ta.getInt(R.styleable.CameraMarkView_crop_mode, CROP_CIRCLE);
            int cameraPosition = ta.getInt(R.styleable.CameraMarkView_camera_position, CameraView.FACING_BACK);
            long duration = ta.getInt(R.styleable.CameraMarkView_scan_duration, 1500);

            mCropMode = cropMode;
            setCropMode(mCropMode);
            setOverlayAlpha(overlayAlpha);
            setCropEnable(cropEnable);
            setCropSize(cropWidth, cropHeight);
            setFlash(isFlash);
            setShowScan(isShowScan);
            setStrokeWidth(strokeWidth);
            setStrokeColor(frameColor);
            setDuration(duration);
            if (cameraPosition == CameraView.FACING_FRONT) {
                setFront();
            } else {
                setBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }
    }

    private CameraView.Callback mCameraCallback = new CameraView.Callback() {
        @Override
        public void onPictureTaken(CameraView cameraView, final Bitmap bitmap) {
            super.onPictureTaken(cameraView, bitmap);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveBitmap(bitmap);
                }
            }).start();
            mScanView.startRadar();
            mCameraView.stop();
        }
    };

    /**
     * 开始预览
     */
    public void start() {
        mCameraView.start();
    }

    /**
     * 拍照
     * 回调 mCameraCallback
     */
    public void takePic() {
        mCameraView.takePicture();
    }

    /**
     * 停止预览
     */
    public void stop() {
        mScanView.stopRadar();
        mCameraView.stop();
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        mScanView.stopRadar();
    }

    /**
     * 设置裁剪模式
     *
     * @param mode
     */
    public void setCropMode(int mode) {
        mMarkView.setCropMode(mode);
        mScanView.setCropMode(mode);
    }

    /**
     * 设置遮罩透明度
     *
     * @param alpha 0-1
     */
    public void setOverlayAlpha(float alpha) {
        if (alpha <= 0) {
            alpha = 0;
        } else if (alpha >= 1) {
            alpha = 1;
        }
        mMarkView.setOverlayAlpha(alpha);
    }

    /**
     * 设置扫描动画周期时长
     *
     * @param duration ms
     */
    public void setDuration(long duration) {
        mScanView.setDuration(duration);
    }

    /**
     * 设置拍摄结果存储路径
     *
     * @param path
     */
    public void setSavePath(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        mSavePath = path;
    }

    /**
     * 设置是否开启裁剪
     *
     * @param enable
     */
    public void setCropEnable(boolean enable) {
        mCropEnable = enable;
        mMarkView.setShowCropFrame(enable);
    }

    /**
     * 设置裁剪宽高
     *
     * @param width
     * @param height
     */
    public void setCropSize(float width, float height) {
        mScanView.setCropParams(width, height);
        mMarkView.setCropParams(width, height);
    }

    /**
     * 设置是否自动对焦
     *
     * @param autoFocus
     */
    public void setAutoFocus(boolean autoFocus) {
        mCameraView.setAutoFocus(autoFocus);
    }

    /**
     * 设置是否开启闪光灯
     *
     * @param isOn
     */
    public void setFlash(boolean isOn) {
        mCameraView.setFlash(isOn ? CameraView.FLASH_ON : CameraView.FLASH_OFF);
    }

    /**
     * 使用前置摄像头
     */
    public void setFront() {
        mCameraView.setFacing(CameraView.FACING_FRONT);
    }

    /**
     * 使用后置摄像头
     */
    public void setBack() {
        mCameraView.setFacing(CameraView.FACING_BACK);
    }

    /**
     * 设置是否展示扫描动画
     *
     * @param isScan
     */
    public void setShowScan(boolean isScan) {
        mScanView.setShowScan(isScan);
    }

    /**
     * 设置裁剪框边缘宽度
     *
     * @param width
     */
    public void setStrokeWidth(float width) {
        mMarkView.setStrokeWidth(width);
    }

    /**
     * 设置裁剪框边缘颜色
     *
     * @param color
     */
    public void setStrokeColor(int color) {
        mMarkView.setFrameColor(color);
    }

    /**
     * 裁剪并保存bitmap到本地
     *
     * @param mBitmap
     */
    public void saveBitmap(Bitmap mBitmap) {
        RectF cropRect = mMarkView.getFrameRect();
        File f = new File(mSavePath);
        try {
            f.createNewFile();
        } catch (IOException e) {

        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (mCropEnable) {
            Bitmap cropBitmap = Bitmap.createBitmap(mBitmap, (int) cropRect.left, (int) cropRect.top, (int) (cropRect.right - cropRect.left), (int) (cropRect.bottom - cropRect.top));
            cropBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } else {
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        }

        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
