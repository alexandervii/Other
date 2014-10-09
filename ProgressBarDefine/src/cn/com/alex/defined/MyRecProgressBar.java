package cn.com.alex.defined;

import cn.com.alex.R;
import cn.com.alex.utils.DensityUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

@SuppressLint("NewApi")
public class MyRecProgressBar extends View {

    private static final String TAG = "Alex";

    private Paint mPaint;// 画笔
    private Rect mBackgroundRect;// 进度条背景矩形
    private RectF mProgressRect;// 进度条前景矩形

    private RectF mBackgroundRectF;// 带圆角的背景

    private int mBackgroundColor;// 背景颜色
    private int mProgressColor;// 前景颜色
    private int mTextColor;//文本颜色
    private float mTextSize;//文本大小
    
    private int mMinimunTextSize;//最小字符大小
    
    private boolean mIsShowText;//是否显示文本

    private int mWidth;// 进度条的长度
    private int mHeight;// 进度条的高度
    
    private int mMinimumWidth;//进度条最小宽度
    private int mMinimumHeight;//进度条最小高度 

    private int mTop;// 顶部
    private int mLeft;// 左边
    private int mRight;// 右边
    private int mBottom;// 底边

    private int mMaxProgress = 0;// 最大进度
    private int mProgress = 0;// 当前进度

    private int mProgressLength = 0;// 进度的长度
    private int mXOffSet = 30;//内部x的偏移，为了体现外边是矩形，内部是圆角矩形
    private int mYOffSet = 10;//内部y的偏移
    
    private int mInnerLeft;//内部圆角矩形的距左边的距离
    private int mInnerTop;//内部圆角矩形的距上边的距离
    private int mInnerRight;//内部圆角矩形的右边的到屏幕左边的距离
    private int mInnerBottom;//内部圆角矩形的底边到屏幕上部的距离
    
    private int mInnerWidth;//内部圆角矩形的宽度
    private int mInnerHeight;//内部圆角矩形的高度
    
    private int mMarginLeft;//距离左边的Margin值
    private int mMarginTop;//距离上边的Margin值
    
    private int mPaddingLeft;//左边的Padding值
    private int mPaddingTop;//顶部的Padding值

    public MyRecProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecProgressBar(Context context) {
        this(context, null, 0);
    }

    public MyRecProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyRecProgressBar);

        mBackgroundColor = typedArray.getColor(R.styleable.MyRecProgressBar_backgroundColor, Color.BLACK);
        mProgressColor = typedArray.getColor(R.styleable.MyRecProgressBar_progressColor, Color.BLUE);
        mMaxProgress = typedArray.getInt(R.styleable.MyRecProgressBar_maxProgress, 0);
        mIsShowText = typedArray.getBoolean(R.styleable.MyRecProgressBar_isShowText, false);
        mTextSize = typedArray.getDimension(R.styleable.MyRecProgressBar_progressTextSize,12);
        mTextColor = typedArray.getColor(R.styleable.MyRecProgressBar_progressTextColor, Color.BLACK);
        
        Log.i(TAG, "BackgroundColor--------->" + mBackgroundColor);
        Log.i(TAG, "ProgressColor--------->" + mProgressColor);
        Log.i(TAG, "MaxProgress--------->" + mMaxProgress);
        Log.i(TAG, "InShowText--------->" + mIsShowText);
        Log.i(TAG, "TextSize---------->" + mTextSize);
        Log.i(TAG, "TextColor---------->" + mTextColor);
        typedArray.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mBackgroundRect = new Rect();
        mProgressRect = new RectF();
        mBackgroundRectF = new RectF();
        
        mMinimumHeight = 20;
        mMinimumWidth = getResources().getDisplayMetrics().widthPixels/3;
        
        mMinimunTextSize = 15;
        Log.i(TAG, "MinimumWidth----------->"+mMinimumWidth);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getMeasuredWidth() < mMinimumWidth ? mMinimumWidth : getMeasuredWidth();
        mHeight = getMeasuredHeight() < mMinimumHeight ? mMinimumHeight : getMeasuredHeight();
        
        Log.i(TAG, "Width---------->" + mWidth);
        Log.i(TAG,"Height---------->" + mHeight);
        
        
        mLeft = (int) getX();
        mTop = (int) getY();
        
        MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
        mMarginLeft = mlp.leftMargin;
        mMarginTop = mlp.topMargin;
        
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        
        if(mMarginLeft > mWidth/2) {
            mMarginLeft = mWidth/2;
        }
        
        if(mPaddingLeft > (mWidth - 2*mXOffSet)/2) {
            mPaddingLeft = (mWidth - 2*mXOffSet)/2;
        }
        
        if(mPaddingTop > (mHeight - 2*mYOffSet)/2) {
            mPaddingTop = (mHeight - 2*mYOffSet)/2;
        }
        
        mRight = mLeft + mWidth;
        mBottom = mTop + mHeight;
        
        mInnerLeft = mLeft + mXOffSet - mMarginLeft + mPaddingLeft;
        mInnerTop = mTop + mYOffSet + mPaddingTop;

        // Log.i(TAG, "Width--------->"+mWidth);
        // Log.i(TAG, "Height---------->"+mHeight);
        // Log.i(TAG, "Left---------->"+mLeft);
        // Log.i(TAG, "Top--------->"+mTop);

        mInnerRight = mRight - mXOffSet - mPaddingLeft;
        mInnerBottom = mBottom - mYOffSet - mMarginTop - mPaddingTop;
        
        mInnerWidth = mInnerRight - mInnerLeft;
        mInnerHeight = mInnerBottom - mInnerTop;
        

        // Log.i(TAG, "Right--------->"+mRight);
        // Log.i(TAG, "Bottm--------->"+mBottom);

        mPaint.setStyle(Style.FILL);

        /*****************画最外围背景*****************/
        mPaint.setColor(mBackgroundColor);
        mBackgroundRect.set(mLeft, mTop, mRight, mBottom);
        canvas.drawRect(mBackgroundRect, mPaint);


        /*****************画圆角背景*****************/
        mBackgroundRectF.set(mInnerLeft, mInnerTop, mInnerRight, mInnerBottom);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        canvas.drawRoundRect(mBackgroundRectF, 10, 10, mPaint);// 第二个参数是x半径，第三个参数是y半径
        
        mPaint.setColor(mProgressColor);
        //获取最新的进度
        mProgressLength = mInnerLeft + getProgressLength();
        if (mProgressLength > mInnerRight) {
            mProgressLength = mInnerRight;
        }
        //画最新进度
        mProgressRect.set(mInnerLeft, mInnerTop, mProgressLength, mInnerBottom);
        canvas.drawRoundRect(mProgressRect, 10, 10, mPaint);
        
        /*****************画文本，进度百分比*****************/
        mPaint.setColor(mTextColor);
        
        //字体大小不能大于内部宽度的2/3
        if(mTextSize > mInnerHeight*2/3) {
            mTextSize = mInnerHeight*2/3;
        }
        //字体大小不能小于最小大小
        if(mTextSize < mMinimunTextSize) {
            mTextSize = mMinimunTextSize;
        }
        mPaint.setTextSize(mTextSize);
        
        String progress = getPercent()+"%";
        
        Rect rect = new Rect();
        this.mPaint.getTextBounds(progress, 0,
                progress.length(), rect);
        int centerWidth = (int) (mLeft + mWidth/2 + 0.5);
        int centerHeight = (int) (mTop + mHeight/2 + 0.5);
        Log.i(TAG, "centerHeight------------->" + centerHeight);
//        float textWidth = mPaint.measureText(progress + "%");
//        int startX = (int) (centerWidth - textWidth/2 + 0.5);
//        int startY = (int) (centerHeight + mTextSize/4 + 0.5);
//        Log.i(TAG, "startY----------->" + startY);
        int x = centerWidth - rect.centerX();
        int y = centerHeight - rect.centerY() - mPaddingTop/2;
        if(mIsShowText) {
            canvas.drawText(progress, x, y, mPaint);
        }
    }
    
    /**
     * 获取当前进度的百分比
     * @return 0~100
     */
    public int getPercent() {
        return mProgress*100/mMaxProgress;
    }

    /**
     * 获取当前进度所占的宽度
     * @return 0~mInnerWidth
     */
    private int getProgressLength() {
        return mInnerWidth * mProgress / mMaxProgress;
    }

    /**
     * 设置最大
     * 
     * @param progress
     */
    public synchronized void setMaxProgress(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Progress should be positive");
        }
        this.mMaxProgress = size;
        Log.i(TAG, "MaxProgress----------->" + mMaxProgress);
    }

    /**
     * 设置当前进度，并重绘
     * 
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("Progress should be positive");
        }
        if (progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        this.mProgress = progress;
        postInvalidate();
    }
    
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public boolean isShowText() {
        return mIsShowText;
    }

    public void setIsShowText(boolean mIsShowText) {
        this.mIsShowText = mIsShowText;
    }
}
