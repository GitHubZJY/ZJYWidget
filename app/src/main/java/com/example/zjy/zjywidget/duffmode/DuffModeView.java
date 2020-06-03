package com.example.zjy.zjywidget.duffmode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Yang on 2019/6/3.
 * PorterDuffXfermode Test View
 */

public class DuffModeView extends View {

    private Paint paint;

    private int width, height;

    private PorterDuffXfermode duffXfermode;

    private Bitmap srcBm, dstBm;

    public DuffModeView(Context context) {
        this(context, null);
    }

    public DuffModeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DuffModeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //禁用硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //初始化画笔
        paint = new Paint();

        duffXfermode = new PorterDuffXfermode(PorterDuff.Mode.XOR);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;
        srcBm = createSrcBitmap(width, height);
        dstBm = createDstBitmap(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //离屏绘制
        int layerID = canvas.saveLayer(0, 0, width, height, paint, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(dstBm, 0, 0, paint);
        paint.setXfermode(duffXfermode);
        canvas.drawBitmap(srcBm, 0, 0, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layerID);
    }

    public Bitmap createDstBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint scrPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrPaint.setColor(Color.parseColor("#00b7ee"));
        canvas.drawCircle(width / 3, height / 3, width / 3, scrPaint);
        return bitmap;
    }

    public Bitmap createSrcBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint dstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dstPaint.setColor(Color.parseColor("#ec6941"));
        canvas.drawRect(new Rect(width / 3, height / 3, width, height), dstPaint);
        return bitmap;
    }
}
