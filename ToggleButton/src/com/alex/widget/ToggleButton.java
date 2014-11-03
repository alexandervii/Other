package com.alex.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ToggleButton extends View {
	
	
	public static final int STATUS_DRAGING = 0;
	public static final int STATUS_SWITCH_ON = 1;
	public static final int STATUS_SWITCH_OFF = 2;
	
	private int mStatus = STATUS_SWITCH_ON;
	
	
	private Paint mPaint;
	
	private RectF mBackRect;
	private RectF mBtnRect;
	
	private float mXRadius = 22;
	private float mYRadius = 22;
	
	private int mBackWidth = 100;
	private int mBackHeight = 50;
	
	
	/** 背景的边界*/
	private float mTop = 10;
	private float mLeft = 10;
	private float mRight = mLeft + mBackWidth;
	private float mBottom = mTop + mBackHeight;
	
	/** 圆形按钮的半径*/
	private int mButtonRadius = mBackHeight/2;
	
	/** 按钮的中心X坐标*/
	private float mBtnX = mLeft + mButtonRadius;
	/** 按钮的中心Y坐标*/
	private float mBtnY = mTop + mButtonRadius;
	
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
		mPaint = new Paint();
		mBackRect = new RectF();
		mBtnRect = new RectF();
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
		drawBackground(canvas, mStatus);
		drawButton(canvas, mStatus, 1);
	}
	
	private void drawButton(Canvas canvas, int status, int center) {
		mPaint.setColor(Color.RED);
		canvas.drawCircle(mBtnX, mBtnY, mButtonRadius, mPaint);
	}
	
	private void drawBackground(Canvas canvas, int status) {
		mPaint.setColor(Color.BLUE);
		mBackRect.set(mTop, mLeft, mRight, mBottom);
//		canvas.drawRoundRect(mBackRect, mXRadius, mYRadius, mPaint);
		canvas.drawRect(mBackRect, mPaint);
		canvas.drawCircle(mBtnX, mBtnY, mButtonRadius, mPaint);
	}

}
