package com.alex.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by pengbosj on 2015/4/7.
 */
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener,View.OnTouchListener {

    private boolean mOnce = false;
    /*初始化时缩放的值*/
    private float mInitScale;
    /*双击放大时到达的值*/
    private float mMidScale;
    /*放大的极限*/
    private float mMaxScale;
    /*缩放和平移的矩阵*/
    private Matrix mScaleMatrix;
    /*多点触控*/
    private ScaleGestureDetector mScaleGestureDetector;


    /*自由移动*/
    /*记录上次多点触控的数量*/
    private int mLastPointerCount;

    /*上次移动的x坐标*/
    private float mLastX;
    /*上次移动的y坐标*/
    private float mLastY;

    /*判断是否移动的最小单位*/
    private int mTouchSlop;
    /*是否能拖动*/
    private boolean isCanDrag;
    /*是否能左右移动*/
    private boolean isCheckLeftAndRight;
    /*是否能上下移动*/
    private boolean isCheckTopAndBottom;


    /*双击放大和缩小*/
    private GestureDetector mGestureDetector;
    /*是否已经在缩放了*/
    private boolean isAutoScale;


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
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if(isAutoScale) {
                            return true;
                        }
                        float x = e.getX();
                        float y = e.getY();
                        //如果当前放大值小于mMidScale，则放大到mMidScale
                        //否则缩小到mInitScale
                        float scale;
                        if(getScale() < mMidScale) {
//                            scale = mMidScale / getScale();
//                            mScaleMatrix.postScale(scale,scale,x,y);
//                            setImageMatrix(mScaleMatrix);
                            scale = mMidScale;
                        } else {
//                            scale = mInitScale / getScale();
//                            mScaleMatrix.postScale(scale,scale,x,y);
//                            setImageMatrix(mScaleMatrix);
                            scale = mInitScale;
                        }
                        //进行梯度缩放
                        postDelayed(new AutoScaleRunnable(scale,x,y),16);
                        isAutoScale = true;
                        return true;
                    }
                });
    }

    private class AutoScaleRunnable implements Runnable {

        /*缩放的目标值*/
        private float mTargetScale;
        /*缩放的中心点*/
        private float x;
        private float y;
        /*放大的梯度*/
        private final float BIGGER = 1.07F;
        /*缩小的梯度*/
        private final float SMALLER = 0.93F;

        private float mTempScale;

        private AutoScaleRunnable(float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;

            if(getScale() < mTargetScale) {
                mTempScale = BIGGER;
            } else if(getScale() > mTargetScale) {
                mTempScale = SMALLER;
            }
        }

        @Override
        public void run() {
            //进行缩放
            mScaleMatrix.postScale(mTempScale,mTempScale,x,y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);

            float currentScale = getScale();

            //如果为达到目标值，则继续
            if((mTempScale < 1.0f && currentScale > mTargetScale) ||
                    (mTempScale > 1.0f && currentScale < mTargetScale)) {
                postDelayed(this,16);
            } else {// 设置为目标值
                float scale = mTargetScale / currentScale;
                mScaleMatrix.postScale(scale,scale,x,y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }
        }
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
            //如果图片的宽，高度均大于或者小于控件的宽高度，则取宽高的比值较小值作为缩放比进行缩小或者放大
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
    public boolean onScale(ScaleGestureDetector detector) {
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
            //以屏幕中心点为基准放大
//            mScaleMatrix.postScale(factor,factor,getWidth()/2,getHeight()/2);
            //以触控的中心点为基准放大
            mScaleMatrix.postScale(factor,factor, detector.getFocusX(),detector.getFocusY());
            //在缩放的同时，图片可能出现白边，或者不居中，则需缩放的同时进行调整
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    /**
     * 获得图片放大缩小以后的宽高，以及left,top,right,bottom;
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();
        Drawable d = getDrawable();
        if(d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    /**
     * 在缩放时进行边界控制以及位置的控制
     */
    private void checkBorderAndCenterWhenScale() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();
        if(rectF.width() >= width) {
            if(rectF.left > 0) {
                deltaX = -rectF.left;
            }
            if(rectF.right < width) {
                deltaX = width - rectF.right;
            }
        }
        if(rectF.height() >= height) {
            if(rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if(rectF.bottom < height) {
                deltaY = height - rectF.bottom;
            }
        }
        //如果图片的宽或者高小于控件的宽或者高，则让图片居中
        if(rectF.width() < width) {
           deltaX = width/2f - rectF.right + rectF.width()/2f;
        }
        if(rectF.height() < height) {
            deltaY = height/2f - rectF.bottom + rectF.height()/2f;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
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
        //这里是防止双击造成的移动操作
        if(mGestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        mScaleGestureDetector.onTouchEvent(motionEvent);
        float x = 0;
        float y = 0;
        //拿到多点触控的数量
        int pointerCount = motionEvent.getPointerCount();
        //获取多点触控的中心点的位置
        for(int i=0; i<pointerCount; i++) {
            x += motionEvent.getX(i);
            y += motionEvent.getY(i);
        }
        x /= pointerCount;
        y /= pointerCount;
        if(mLastPointerCount != pointerCount) {
            mLastX = x;
            mLastY = y;
            isCanDrag = false;
        }
        mLastPointerCount = pointerCount;
        RectF rectF = getMatrixRectF();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果当前图片是放大的效果，则不能被父类拦截事件（+0.01是为了防止边界图片的宽高刚好比空间宽高大0.0X）
                if(rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    if(getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    if(getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                float dx = x - mLastX;
                float dy = y - mLastY;
                if(!isCanDrag) {
                    isCanDrag = isMove(dx,dy);
                }
                if(isCanDrag) {
                    if(getDrawable() != null) {
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        //如果宽度小于控件宽度，不允许左右移动
                        if(rectF.width() <= getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        //如果高度小于控件高度，不允许上下移动
                        if(rectF.height() <= getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }
                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate(dx, dy);
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastY = y;
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
        }
        return true;
    }

    /**
     * 当移动时，进行边界检查
     */
    private void checkBorderWhenTranslate(float dx, float dy) {
        RectF rectF = getMatrixRectF();

        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        //如果图片的顶端是大于零，并且图片高度大于控件高度，则向上偏移，直至图片顶端到达控件顶端
        if(rectF.top > 0 && isCheckTopAndBottom) {
            deltaY = -rectF.top;
        }
        //如果图片的底端是小于控件高度，并且图片高度大于控件高度，则向下偏移，直至图片底端到达控件底端
        if(rectF.bottom < height && isCheckTopAndBottom) {
            deltaY = height - rectF.bottom;
        }
        //如果图片的左端大于零，并且图片宽度大于控件宽度，则向左偏移，直至图片左端到达控件左端
        if(rectF.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectF.left;
        }
        //如果图片的右端小于控件宽度，并且图片宽度大于控件宽度，则向右偏移，直至图片右端到达控件右端
        if(rectF.right < width && isCheckLeftAndRight) {
            deltaX = width - rectF.right;
        }
        mScaleMatrix.postTranslate(deltaX,deltaY);
    }

    /**
     * 判断是否移动了
     * @param dx
     * @param dy
     * @return
     */
    private boolean isMove(float dx, float dy) {
        return Math.sqrt(dx*dx + dy*dy) > mTouchSlop;
    }
}
