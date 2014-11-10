package com.alex.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ToggleButton extends View {
	
	
	public static final int STATUS_DRAGING = 0;
	public static final int STATUS_SWITCH_ON = 1;
	public static final int STATUS_SWITCH_OFF = 2;
	
	private int mStatus = STATUS_SWITCH_ON;
	private String mContent = "匿名";
	
	
	private Paint mBackPaint;
	private Paint mBtnPaint;
	private Paint mTextPaint;
	
	private RectF mBackRect;
	
	private int mBackWidth = 80;
	private int mBackHeight = 80;
	
	private int mGap = 5;
	
	private float mMoveX = -1;
	
	/** 背景的边界*/
	private float mTop = 30;
	private float mLeft = 30;
	private float mRight = mLeft + mBackWidth + mBackHeight;
	private float mBottom = mTop + mBackHeight;
	
	/** 圆形按钮的半径*/
	private int mButtonRadius = mBackHeight/2;
	
	/** 左边按钮的中心X坐标*/
	private float mBtnLX = mLeft + mButtonRadius;
	/** 左边按钮的中心Y坐标*/
	private float mBtnLY = mTop + mButtonRadius;
	/**右边按钮的中心X坐标*/
	private float mBtnRX = mRight - mButtonRadius;
	/**右边按钮的中心Y坐标*/
	private float mBtnRY = mTop + mButtonRadius;
	/** 背景字体的大小*/
	private float mTextSize = 22;
	/** 字体的Y*/
	private float mTextY = mBtnLY + mGap;
	/**字体的X*/
	private float mTextX = (mRight - mBackHeight + mLeft - mTextSize)/2 - mGap;
	
	public ToggleButton(Context context) {
		super(context);
		init();
	}

	public ToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		mBackPaint = new Paint();
		mBtnPaint = new Paint();
		mTextPaint = new Paint();
		mBackRect = new RectF();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mStatus = STATUS_SWITCH_ON;
		drawBackground(canvas, mStatus);
		drawButton(canvas, mStatus, mMoveX);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		float lastX = -1;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//如果手点到了背景界内才会出反应
			if(x < mLeft || x > mRight || y < mTop || y > mBottom) {
				return false;
			}
			lastX = event.getX();
			mStatus = STATUS_DRAGING;
			break;
		case MotionEvent.ACTION_MOVE:
			float currentX = event.getX();
			//如果是滑动状态
			//如果出界(x轴边界)了，做出出界处理
			//根据currentX - lastX的正负，及绝对值来计算出滑块的移动方向和距离
			
			if(mStatus == STATUS_DRAGING) {
				if(x > mRight) {
					x = mRight;
				}
				if(x < mLeft) {
					x = mLeft;
				}
				
			}
			lastX = currentX;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			//对抬起时的点左边界检测，如果x坐标小于背景中点x，则滑块最终在左边，否则在右边
			//根据和上面的判断，重置滑块的状态
			break;
		}
		return super.onTouchEvent(event);
	}
	
	private void drawButton(Canvas canvas, int status, float centerX) {
		mBtnPaint.setColor(Color.RED);
		canvas.drawCircle(centerX, mBtnRY, mButtonRadius, mBtnPaint);
	}
	
	private void drawBackground(Canvas canvas, int status) {
		mBackPaint.setColor(Color.BLUE);
		mBackRect.set(mBtnLX, mTop, mBtnRX, mBottom);
		canvas.drawRect(mBackRect, mBackPaint);
		canvas.drawCircle(mBtnLX, mBtnLY, mButtonRadius, mBackPaint);
		canvas.drawCircle(mBtnRX, mBtnRY, mButtonRadius, mBackPaint);
		drawText(canvas, status);
	}
	
	private void drawText(Canvas canvas, int status) {
		mTextPaint.setTextSize(mTextSize);
		if(status == STATUS_SWITCH_ON) {
			canvas.drawText(mContent, mTextX, mTextY, mTextPaint);
		} 
	}
}
