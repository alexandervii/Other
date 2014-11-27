package com.alex.widget;

import com.alex.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ToggleButton extends View {
	
	public static final String TAG = ToggleButton.class.getSimpleName();

	private float mRadiusLeft;
	private float mRadiusRight;
	
	private float mLeft;
	private float mRight;
	private float mTop;
	private float mBottom;
	
	private float mCenterX;
	
	private float mTextX;
	private float mTextY;
	private float mTextSize;
	private String mText;
	private boolean mIsShowText = false;
	
	private float mBackWidth;
	private float mBackHeight;
	
	private float mRadiusX;
	private float mRadiusY;
	private float mRadius;
	
	private Paint mDragPaint;
	private Paint mBackPaint;
	private Paint mTextPaint;
	
	private float mOldX;
	private float mOldY;
	
	private int mScreenWidth;
	private int mScreenHeight;
	
	private Status mStatus;
	private Drag mDragType = Drag.TOUCH;
	
	private float mTouchX,mTouchY;
	
	
	private OnStatusChangeListener mOnStatusChangeListener;
	
	public ToggleButton(Context context) {
		super(context);
		initView();
	}
	
	public ToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
		initAttributes(context,attrs);
	}

	public ToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initAttributes(context,attrs);
	}
	
	@SuppressLint("Recycle")
	private void initAttributes(Context context, AttributeSet attrs) {
		if(attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.ToggleButton);
			
			int status = ta.getInt(R.styleable.ToggleButton_status, 1);
			Log.i(TAG, "attribute------>>>status--->>"+status);
			if(status == 0) {
				mStatus = Status.ON;
			} else {
				mStatus = Status.OFF;
			}
			mIsShowText = ta.getBoolean(R.styleable.ToggleButton_show_text, false);
			Log.i(TAG, "attribute------>>>isShowText--->>"+mIsShowText);
			if(mIsShowText) {
				mText = ta.getString(R.styleable.ToggleButton_text);
				mText = mText == null ? "" : mText;
				Log.i(TAG, "attribute------>>>text--->>"+mText);
			}
			ta.recycle();
		}
	}
	
	public void setShowText(boolean show) {
		mIsShowText = show;
	}
	
	public void setText(String text) {
		if(mIsShowText) {
			mText = text;
		}
	}

	private void initView() {
		mDragPaint = new Paint();
		mBackPaint = new Paint();
		mTextPaint = new Paint();
		mScreenWidth = getResources().getDisplayMetrics().widthPixels;
		mScreenHeight = getResources().getDisplayMetrics().heightPixels;
		initData();
	}
	
	private void initData() {
		int width = getWidth();
		int height = getHeight();
		int top = getTop();
		int left = getLeft();
		Log.i(TAG, "width-->>height-->>top-->>left--->>>>"+width+","+height+","+top+","+left);
		/*******需要初始化的参数*********/
		mRadius = 50;
		mBackWidth = 130;
		mLeft = 50;
		mTop = 50;
		mStatus = Status.OFF;
		mTextSize = 35;
		/*******需要初始化的参数*********/
		
		mRadiusLeft = mLeft + mRadius;
		mRadiusRight = mRadiusLeft + mBackWidth;
		
		mBackHeight = mRadius * 2;
		mBottom = mTop + mBackHeight;
		
		mRadiusX = mRadiusLeft;
		mRadiusY = mTop + mRadius;
		
		mRight = mRadiusRight + mRadius;
		
		mCenterX = mRadiusLeft + mBackWidth / 2;
		
		mTextX = mLeft + mBackWidth / 2 - mTextSize;
		mTextY = mTop + mBackHeight / 2 + mTextSize / 3;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//		int width = MeasureSpec.getSize(widthMeasureSpec);
//		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//		int height = MeasureSpec.getSize(heightMeasureSpec);
//		height = getMeasuredHeight();
//		int width = height * 3;
//		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);
		drawText(canvas);
		drawButton(canvas);
	}
	
	private void drawText(Canvas canvas) {
		mTextPaint.setColor(Color.BLACK);
		mTextPaint.setTextSize(mTextSize);
		if(mRadiusX <= mTextX + mRadius) {
			return ;
		}
		if(mStatus == Status.ON && mIsShowText && !TextUtils.isEmpty(mText)) {
			canvas.drawText("Open", mTextX, mTextY, mTextPaint);
		}
	}

	private void drawBackground(Canvas canvas) {
		if(mStatus == Status.OFF) {
			mBackPaint.setColor(Color.GRAY);
		} else if(mStatus == Status.ON) {
			mBackPaint.setColor(Color.BLUE);
		}
		canvas.drawCircle(mRadiusLeft, mRadiusY, mRadius, mBackPaint);
		canvas.drawCircle(mRadiusRight, mRadiusY, mRadius, mBackPaint);
		canvas.drawRect(mRadiusLeft, mTop, mRadiusRight, mBottom, mBackPaint);
	}

	public void drawButton(Canvas canvas) {
		mDragPaint.setColor(Color.RED);
		canvas.drawCircle(mRadiusX, mRadiusY, mRadius, mDragPaint);
	}
	
	public enum Status {
		//开
		ON,
		//关
		OFF;
	}
	
	public enum Drag {
		//默认为点击到按钮状态
		TOUCH,
		//没有点击到按钮
		UN_TOUCH,
	}
	
	public void setOnStatusChangeListener(OnStatusChangeListener listener) {
		mOnStatusChangeListener = listener;
	}
	
	public interface OnStatusChangeListener {
		public void onChange(Status status);
	}
	
	/**
	 * 检查第一个点是否在以第二个点为中心，半径为radius的圆上
	 * @param fx 第一个点的x坐标
	 * @param fy 第一个点的y坐标
	 * @param nx 第二个点的x坐标
	 * @param ny 第二个点的y坐标
	 * @return true表示在圆上，false表示不在
	 */
	private boolean isOnCircle(float fx,float fy, float nx, float ny, float radius) {
		double powerRadius = Math.pow((fx - nx),2) + Math.pow((fy - ny), 2);
		if(powerRadius >= Math.pow(radius,2)) {
			return false;
		}
		return true;
	}
	
	public Status getStatus() {
		return mStatus;
	}
	
	public void setStatus(Status status) {
		mStatus = status;
		postInvalidate();
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDragType = Drag.TOUCH;
			mTouchX = -1;
			mTouchY = -1;
			mOldX = event.getX();
			mOldY = event.getY();
			if(!isOnCircle(mOldX,mOldY,mRadiusX,mRadiusY,mRadius)) {
				Log.i(TAG, "down--->>>未点中按钮");
				mDragType = Drag.UN_TOUCH;
				mTouchX = mOldX;
				mTouchY = mOldY;
				Log.i(TAG, "mTouchX ="+mTouchX);
				Log.i(TAG, "mTouchY ="+mTouchY);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float x = event.getX();
			float y = event.getY();
			if(mDragType == Drag.UN_TOUCH && x != mTouchX) {
				return true;
			}
			mRadiusX += x - mOldX;
//			mRadiusY += y - mOldY;
			if(mRadiusX < mRadiusLeft) {
				mRadiusX = mRadiusLeft;
			}
			if(mRadiusX >= mRadiusRight) {
				mRadiusX = mRadiusRight;
			}
			mOldX = x;
			mOldY = y;
			Log.i(TAG, "move");
			break;
		case MotionEvent.ACTION_UP:
			mOldX = event.getX();
			mOldY = event.getY();
			if(mDragType == Drag.TOUCH) {
				Log.i(TAG, "up---->>>拖动");
				//拖动转换状态
				if(mRadiusX < mCenterX) {
					Log.i(TAG, "拖动了->关");
					mRadiusX = mRadiusLeft;
					mStatus = Status.OFF;
				} else {
					Log.i(TAG, "拖动了->开");
					mRadiusX = mRadiusRight;
					mStatus = Status.ON;
				}
			} else if(mDragType == Drag.UN_TOUCH && mOldX == mTouchX && mOldY == mTouchY) {
				//是否点击的on
				if(isOnCircle(mOldX,mOldY,mRadiusRight,mRadiusY,mRadius)) {
					Log.i(TAG, "点击了->开");
					mRadiusX = mRadiusRight;
					mStatus = Status.ON; 
				} 
				//是否点击的off
				if(isOnCircle(mOldX,mOldY,mRadiusLeft,mRadiusY,mRadius)) {
					Log.i(TAG, "点击了->关");
					mRadiusX = mRadiusLeft;
					mStatus = Status.OFF;
				}
			} 
			if(mOnStatusChangeListener != null) {
				mOnStatusChangeListener.onChange(mStatus);
			}
			break;
		}
		postInvalidate();
		return true;
	}
	
}
