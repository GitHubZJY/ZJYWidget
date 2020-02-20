package com.zjywidget.widget.fallingsurface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zjywidget.widget.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FallingSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    /**
     * 用于标注线程是否继续
     */
    private boolean mFlag = true;

    private SurfaceHolder surfaceHolder;

    private Paint paint;

    /**
     * 掉落对象的集合
     */
    private List<FallingItem> fallingItems;

    /**
     * 用于随机生成X轴坐标
     */
    private Random random = new Random();

    private int duration = 3000;

    private int count = 0;

    private int maxCount = 60;
    private int curIndex = 0;
    /**
     * 当前生成的红包数量
     */
    private int curGenerateCount = 0;
    private int lastStartX = 0;

    private Bitmap mBitmap;
    private int bitmapWidth;
    private int bitmapHeight;
    private Matrix mMatrix;

    private int mCanvasHeight;
    private int mCanvasWidth;

    public FallingSurfaceView(Context context) {
        super(context);
    }

    public FallingSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        fallingItems = new ArrayList<>();
        paint = new Paint();

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_red_package);
        bitmapWidth = mBitmap.getWidth();
        bitmapHeight = mBitmap.getHeight();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        //设置背景透明
        this.setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mMatrix = new Matrix();

    }

    public FallingSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //初始化画笔等
        init();
        mFlag = true;
        //启动线程绘制
        new Thread(this).start();
    }

    private void init() {
        //抗锯齿
        paint.setAntiAlias(true);
    }

    private void addItem() {
        if(curGenerateCount >= maxCount) {
            return;
        }
        curIndex ++;
        if(curIndex % 10 != 0) {
            return;
        }
        FallingItem item = new FallingItem();
        int startInLeft = 0;
        if(lastStartX > bitmapWidth) {
            startInLeft = random.nextInt(lastStartX - bitmapWidth);
        }
        int startInRight = 0;
        if(lastStartX + bitmapWidth - 1 < mCanvasWidth){
            startInRight = random.nextInt(mCanvasWidth - lastStartX - bitmapWidth + 1) + lastStartX;
        }
        if(startInLeft > 0 && startInRight > 0){
            item.startX = random.nextBoolean() ? startInLeft : startInRight;
        }else{
            if(startInLeft == 0){
                item.startX = startInRight;
            }
            if(startInRight == 0){
                item.startX = startInLeft;
            }
        }
        //int startInRight = random.nextInt(mCanvasWidth - bitmapWidth - lastStartX) + lastStartX + bitmapWidth;
        if(item.startX > mCanvasWidth - bitmapWidth){
            item.startX = mCanvasWidth - bitmapWidth;
        }
        item.startY = -60;
        item.speed = (random.nextInt(3)+2)*10;
        item.rotate = random.nextInt(360);
        lastStartX = item.startX;
        //添加到集合
        fallingItems.add(item);
        curGenerateCount++;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mFlag = false;
    }

    private int onceTime;

    @Override
    public void run() {
        Canvas canvas = null;
        FallingItem item = null;
        while (mFlag) {
            long startTime = System.currentTimeMillis();
            try {
                canvas = surfaceHolder.lockCanvas();
                if(mCanvasHeight == 0) {
                    mCanvasHeight = canvas.getHeight();
                    mCanvasWidth = canvas.getWidth();
                }
                //清空画布
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            } catch (Exception e) {
                break;
            }

            for (int i = 0; i < fallingItems.size(); i++) {
                item = fallingItems.get(i);
                mMatrix.setRotate(item.rotate, (float) bitmapWidth / 2, (float) bitmapHeight / 2);
                mMatrix.postTranslate(item.startX, item.startY);
                canvas.drawBitmap(mBitmap, mMatrix, paint);
                item.setStartY(item.getStartY() + item.speed);
            }

             //解锁画布
             surfaceHolder.unlockCanvasAndPost(canvas);

            //添加坠落对象
            addItem();

            if (fallingItems.size() > 50) {
                fallingItems.remove(0);
            }
            onceTime = (int)(System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                checkInRect((int) event.getX(), (int) event.getY());
                break;
        }
        return true;
    }

    /**
     * 是否点击在红包区域
     * @param x
     * @param y
     */
    private void checkInRect(int x, int y) {
        Log.d("Falling", "checkInRect");
        int length = fallingItems.size();
        for (int i = 0; i < length; i++) {
            FallingItem moveModel = fallingItems.get(i);
            Rect rect = new Rect((int) moveModel.startX, (int) moveModel.startY, (int) moveModel.startX + bitmapWidth, (int) moveModel.startY + bitmapHeight);
            if (rect.contains(x, y)) {
                count++;
                resetMoveModel(moveModel);
                Log.d("Falling", "count: " + count);
                break;
            }
        }
    }

    private void resetMoveModel(FallingItem moveModel) {
        moveModel.startX = 0;
        moveModel.startY = -100;
        if(fallingItems.contains(moveModel)){
            fallingItems.remove(moveModel);
        }
    }


    class FallingItem {

        /**
         * 起始X坐标
         */
        private int startX;
        /**
         * 线的起始Y坐标
         */
        private int startY;
        /**
         * 坠落速度
         */
        private int speed;
        /**
         * 旋转的度数
         */
        private int rotate;

        public int getRotate() {
            return rotate;
        }

        public void setRotate(int rotate) {
            this.rotate = rotate;
        }

        public int getSpeed() {
            return speed;
        }

        public FallingItem setSpeed(int speed) {
            this.speed = speed;
            return this;
        }

        public int getStartX() {
            return startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public int getStartY() {
            return startY;
        }

        public void setStartY(int startY) {
            this.startY = startY;
        }
    }
}
