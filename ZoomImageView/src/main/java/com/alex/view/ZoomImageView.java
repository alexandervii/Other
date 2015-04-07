package com.alex.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by pengbosj on 2015/4/7.
 */
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener,View.OnTouchListener {

    private boolean mOnce = false;
    //初始化时缩放的值
    private float mInitScale;
    //双击放大时到达的值
    private float mMidScale;
    //放大的极限
    private float mMaxScale;
    //缩放和平移的矩阵
    private Matrix mScaleMatrix;
    //多点触控
    private ScaleGestureDetector mScaleGestureDetector;


    public ZoomImageView(Context context) {
        this(context, null, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleMatrix = new Matrix();
        super.setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector  = new ScaleGestureDetector(context,this);
        setOnTouchListener(this);
    }

    /**
     * 获取ImageView加载完成的图片
     */
    @Override
    public void onGlobalLayout() {
        if(!mOnce) {
            //得到控件的宽和高
            int width = getWidth();
            int height = getHeight();
            //得到图片，和图片的宽和高
            Drawable d = getDrawable();
            if(d == null) {
                return;
            }
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();
            float scale = 1.0f;
            //如果图片宽度大于控件宽度，图片高度小于控件高度，则取宽度的比值作为缩放比进行缩小
            if(dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }
            //如果图片宽度小于控件宽度，图片高度大于控件高度，则取高度的比值作为缩放比进行缩小
            if(dh > height && dw < width) {
                scale = height * 1.0f / dh;
            }
            //如果图片的宽，高度均大于或者小于控件的宽高度，则取宽高的比值较小值作为缩放比进行缩小
            if((dw > width && dh > height) || (dw < width && dh < height)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            //得到初始化的比例
            mInitScale = scale;
            mMidScale = mInitScale * 2;
            mMaxScale = mInitScale * 4;

            //将图片移动到控件中心
            int transX = (getWidth() - dw) / 2;
            int transY = (getHeight() - dh) / 2;

            mScaleMatrix.postTranslate(transX,transY);
            //以控件中心进行缩放
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);

            setImageMatrix(mScaleMatrix);

            mOnce = true;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    /**
     * 获取当前图片的缩放值
     * @return
     */
    public float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        float scale = getScale();
        float factor = mScaleGestureDetector.getScaleFactor();
        if(getDrawable() == null) {
            return true;
        }
        //缩放范围的控制
        //如果缩放比小于最大值，并且，缩放因子大于1(表示有放大趋势)
        //如果缩放比大于初始值，并且，缩放因子小于1(表示有缩小趋势)
        if((scale < mMaxScale && factor > 1.0f) ||
                (scale > mInitScale && factor < 1.0f)) {
            if(scale * factor < mInitScale) {
                factor = mInitScale / scale;
            }
            if(scale * factor > mMaxScale) {
                factor = mMaxScale / scale;
            }
            mScaleMatrix.postScale(factor,factor,getWidth()/2,getHeight()/2);
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
}
