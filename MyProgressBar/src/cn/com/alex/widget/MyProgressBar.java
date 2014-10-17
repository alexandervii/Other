package cn.com.alex.widget;

import cn.com.alex.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

public class MyProgressBar extends View {
    
    public static final String TAG = "Alex";
    public static final int mDefaultTextSize = 14;
    public static final int mDefaultTextColor = Color.BLACK;
    public static final int mDefaultBackColor = Color.BLUE;
    public static final int mDefaultForeColor = Color.YELLOW;
    
    private Paint mPaint;
    private int mMaxProgressBarWidth = 720; 
    private int mXGap = 10;
    private int mYGap = 7;
    private int mMinHeight = 100;
    
    private int mBarCount;
    private int mMaxBarNum = 22;
    private int mCurrentBarIndex = 0;
    private int mStairCount = 10;
    
    private final int mBarWidth = 20;
    private final int mBarHeight = 80;
    
    private int mMarginLeft = 0;
    private int mMarginTop = 0; 
    private int mPaddingLeft = 0;
    private int mPaddingTop = 0;
    
    private int mLeft;
    private int mRight;
    private int mTop;
    private int mBottom;
    
    private int mBackgoundProgressColor = -1;
    private int mForegroundProgressColor = -1;
    
    private int mTextLeft;
    private int mTextTop;
    private int mTextSize = 14;
    private int mMaxTextSize = mBarHeight - mYGap;
    private int mTextColor = -1;
    
    private int mCurrentProgress = 0;
    private int mMaxProgress = 100;
    
    public int mScreenWidth = 720;
    
    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }
    
    @SuppressLint("NewApi")
    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mMaxProgressBarWidth = mScreenWidth;
        
        //加载属性
        if(attrs != null) {
            TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.MyProgressBar);
            mTextColor = attr.getColor(R.styleable.MyProgressBar_textColor, mDefaultTextColor);
            mTextSize = (int)attr.getDimension(R.styleable.MyProgressBar_textSize, mDefaultTextSize);
            mMaxProgress = attr.getInt(R.styleable.MyProgressBar_maxProgress, 100);
            mBackgoundProgressColor = attr.getColor(R.styleable.MyProgressBar_backgroundProgressColor, mDefaultBackColor);
            mForegroundProgressColor = attr.getColor(R.styleable.MyProgressBar_foregroundProgressColor, mDefaultForeColor);
        }
        
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
        mMarginLeft = mlp.leftMargin;
        mMarginTop = mlp.topMargin;
        
        mPaddingTop = getPaddingTop();
        mPaddingLeft = getPaddingLeft();
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mBackgoundProgressColor == -1 ? mDefaultBackColor : mBackgoundProgressColor);
        mBarCount = drawProgress(canvas,mPaint,mMaxBarNum);
         
        mPaint.setColor(mForegroundProgressColor == -1 ? mDefaultForeColor : mForegroundProgressColor);
        drawProgress(canvas,mPaint,getProgressBarCount());
    }
    
    public void setMaxProgress(int max) {
        this.mMaxProgress = max;
    }
    
    public void setTextSize(int size) {
        if(size < 0) {
            throw new IllegalArgumentException("字体大小不能小于0");
        }
        if(size > mMaxTextSize) {
            size = mMaxTextSize;
        }
        this.mTextSize = size;
    }
    
    public void setTextColor(int color) {
        mTextColor = color;
    }
    
    public void setForeColor(int color) {
        mForegroundProgressColor = color;
    }
    
    public void setBackColor(int color) {
        mBackgoundProgressColor = color;
    }
    
    private int drawProgress(Canvas canvas, Paint paint, int barCount) {
        mCurrentBarIndex = mStairCount - 1;
        int totalBarCount = 0;
        for (int i = 0; i < barCount; i++) {
            mLeft = mBarWidth * i + mXGap * i + mMarginLeft + mPaddingLeft;
            if(i == 0) {
                mTextLeft = mLeft;
            }
            mRight = mLeft + mBarWidth;
            mTop = mMarginTop + mPaddingTop;
            mBottom = mTop + mBarHeight;
            if(i < mStairCount) {
                if(i == 0) {
                  mTextTop = mTop + (mCurrentBarIndex*mYGap)/2;
                  Log.i(TAG, "text top--->>>"+mTextTop);
                  Log.i(TAG, "first bar top--->>>>"+(mTop + mCurrentBarIndex*mYGap));
                  Log.i(TAG, "text size ------>>>>"+mTextSize);
                }
                mTop = mTop + mCurrentBarIndex*mYGap;
                mCurrentBarIndex --;
            } 
            totalBarCount++;
            if(mRight > mScreenWidth - mMarginLeft - mPaddingLeft) {
                break;
            }
            canvas.drawRect(mLeft, mTop, mRight, mBottom, mPaint);
            drawProgressText(canvas);
        }
        return totalBarCount;
    }
    
    private void drawProgressText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mTextColor == -1 ? Color.RED : mTextColor);
        paint.setTextSize(mTextSize);
        canvas.drawText(getProgress()+"%", mTextLeft, mTextTop, paint);
    }
    
    public void setProgress(int progress) {
        if(progress < 0) {
            throw new IllegalArgumentException("进度不能小于0");
        }
        if(progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        mCurrentProgress = progress;
        postInvalidate();
    }
    
    private int getProgress() {
        return 100*mCurrentProgress/mMaxProgress;
    }
    
    /**
     * 返回需要画的进度对应的Bar的个数
     * @return
     */
    private int getProgressBarCount() {
        int count = 0;
        if(mCurrentProgress <= mMaxProgress) {
            count = mBarCount * mCurrentProgress/mMaxProgress;
            Log.i(TAG, "bar count----->>>"+mBarCount);
        } else {
            count = mBarCount;
        }
        return count;
    }
}
