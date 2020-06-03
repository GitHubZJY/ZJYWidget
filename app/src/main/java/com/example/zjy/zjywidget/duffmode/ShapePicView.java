package com.example.zjy.zjywidget.duffmode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.zjy.zjywidget.R;

/**
 * Created by Yang on 2019/6/12.
 * 混合模式裁剪任意形状图片
 */

public class ShapePicView extends View {

    private Paint paint;

    private int width, height;

    private PorterDuffXfermode duffXfermode;

    private Bitmap srcBm, dstBm;


    public ShapePicView(Context context) {
        this(context, null);
    }

    public ShapePicView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapePicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //禁用硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //初始化画笔
        paint = new Paint();

        duffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;
        dstBm = createDstBitmap();

        srcBm = createSrcBitmap(dstBm.getWidth(), dstBm.getHeight());


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

    public Bitmap createDstBitmap() {
        return BitmapFactory.decodeResource(getResources(), R.drawable.bg_duffmode_test);
    }

    public Bitmap createSrcBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint srcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        srcPaint.setStyle(Paint.Style.FILL);
        srcPaint.setColor(Color.parseColor("#ec6941"));
        //绘制三角形
        Path path = new Path();
        path.moveTo(width / 2, 0);
        path.lineTo(width / 6, height);
        path.lineTo(width * 5 / 6, height);
        path.close();
        canvas.drawPath(path, srcPaint);
        //绘制圆形
        //canvas.drawCircle(width/2, height/2, height/2, dstPaint);
        return bitmap;
    }
}
