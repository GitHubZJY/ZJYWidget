package com.example.zjy.zjywidget.camerapreview.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * @author : YANG
 * @describe : bitmap翻转工具类
 * @date : 2018/7/26
 */
public class MatrixUtil {

    /**
     * 翻转bitmap (-1,1)左右翻转  (1,-1)上下翻转
     *
     * @param srcBitmap
     * @param sx
     * @param sy
     * @return
     */
    public static Bitmap turnCurrentLayer(Bitmap srcBitmap, float sx, float sy) {
        Bitmap cacheBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);// 创建缓存像素的位图
        int w = cacheBitmap.getWidth();
        int h = cacheBitmap.getHeight();

        Canvas cv = new Canvas(cacheBitmap);

        Matrix mMatrix = new Matrix();//使用矩阵 完成图像变换

        mMatrix.postScale(sx, sy);//重点代码

        Bitmap resultBimtap = Bitmap.createBitmap(srcBitmap, 0, 0, w, h, mMatrix, true);
        cv.drawBitmap(resultBimtap,
                new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight()),
                new Rect(0, 0, w, h), null);
        return resultBimtap;
    }

    /**
     * 读取图片属性：旋转角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exitInterface = new ExifInterface(path);
            int orientation = exitInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            return degree;
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || bitmap == null) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return bmp;
    }
}
