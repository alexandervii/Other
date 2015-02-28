package com.alex.ArcMenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.alex.ArcMenu.R;

/**
 * Created by Administrator on 2015/2/27.
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener {

    private static final int POS_LEFT_TOP = 0;
    private static final int POS_LEFT_BOTTOM = 1;
    private static final int POS_RIGHT_TOP = 2;
    private static final int POS_RIGHT_BOTTOM = 3;

    private Position mPosition = Position.LEFT_BOTTOM;
    private int mRadius;
    private Status mStatus = Status.CLOSE;
    private View mMainButton;
    private OnMenuItemClickListener mOnMenuItemClickListener;

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mOnMenuItemClickListener = listener;
    }

    public interface OnMenuItemClickListener {
        public void onClick(View view, int position);
    }

    public enum Status {
        OPEN,CLOSE;
    }

    public enum Position {
        LEFT_TOP,LEFT_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM;
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getResources().getDisplayMetrics());
        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcMenu,defStyle,0);
        int pos = ta.getInt(R.styleable.ArcMenu_Position,POS_LEFT_BOTTOM);
        switch (pos) {
            case POS_LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case POS_LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case POS_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case POS_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        mRadius = (int) ta.getDimension(R.styleable.ArcMenu_radius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        Log.i("Alex","position="+mPosition+", radius="+mRadius);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for(int i=0; i<childCount; i++) {
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed) {
            layoutMainButton();
            int childCount = getChildCount();
            for(int i=0; i<childCount - 1; i++) {
            	View child = getChildAt(i+1);
            	
            	child.setVisibility(View.GONE);
            	
            	int cl = (int) (mRadius*Math.sin(Math.PI/2/(childCount - 2)*i));
            	int ct = (int) (mRadius*Math.cos(Math.PI/2/(childCount - 2)*i));
            	
            	int cWidth = child.getMeasuredWidth();
            	int cHeight = child.getMeasuredHeight();
            	
            	//如果位置在左下，右下
            	if(mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM) {
            		ct = getMeasuredHeight() - cHeight - ct;
            	}
            	//如果位置在右上，右下
            	if(mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
            		cl = getMeasuredWidth() - cWidth - cl;
            	}
            	
            	child.layout(cl, ct, cl+cWidth, ct+cHeight);
            }
        }
    }

    private void layoutMainButton() {
        mMainButton = getChildAt(0);
        mMainButton.setOnClickListener(this);
        int l = 0;
        int t = 0;

        int width = mMainButton.getMeasuredWidth();
        int height = mMainButton.getMeasuredHeight();
        switch (mPosition) {
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }
        mMainButton.layout(l,t,l+width,t+height);
    }
    
    public boolean isOpen() {
    	return mStatus == Status.OPEN ? true : false;
    }

    @Override
    public void onClick(View v) {
//    	mMainButton = findViewById(R.id.id_button);
//    	if(mMainButton == null) {
//    		mMainButton = getChildAt(0);
//    	}
    	rotateMainButton(v,0f,360f,300);
    	toggleMenu(300);
    }

    /**
     * 切换菜单
     */
	public void toggleMenu(int duration) {
		//为所有的menu item添加平移动画和旋转动画
		int childCount = getChildCount();
		for(int i=0; i<childCount - 1; i++) {
			final View childView = getChildAt(i + 1);
			
			//针对当子按钮为隐藏状态，执行动画需要显示按钮
			childView.setVisibility(View.VISIBLE);
			
			//end 0,0
			//start
			int cl = (int) (mRadius*Math.sin(Math.PI/2/(childCount - 2)*i));//TODO
        	int ct = (int) (mRadius*Math.cos(Math.PI/2/(childCount - 2)*i));
        	
        	int xFlag = 1;
        	int yFlag = 1;
        	
        	if(mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM) {
        		xFlag = -1;
        	}
        	if(mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP) {
        		yFlag = -1;
        	}
			
        	AnimationSet animateSet = new AnimationSet(true);
        	Animation transAnim = null;
        	//to open
        	if(mStatus == Status.CLOSE) {
        		transAnim = new TranslateAnimation(xFlag*cl,0,yFlag*ct,0);
        		childView.setClickable(true);
        		childView.setFocusable(true);
        	} else {//to close
        		transAnim = new TranslateAnimation(0,xFlag*cl,0,yFlag*ct);
        		childView.setClickable(false);
        		childView.setFocusable(false);
        	}
        	transAnim.setFillAfter(true);
        	transAnim.setDuration(duration);
        	transAnim.setStartOffset((i*100) / childCount);
        	
        	transAnim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					//关闭动画结束后子按钮隐藏
					if(mStatus == Status.CLOSE) {
						childView.setVisibility(View.GONE);
					}
				}
			});
        	
        	//旋转动画
        	RotateAnimation rotateAnim = new RotateAnimation(0, 720,
    				Animation.RELATIVE_TO_SELF, 0.5f, 
    				Animation.RELATIVE_TO_SELF, 0.5f);
        	rotateAnim.setDuration(duration);
        	rotateAnim.setFillAfter(true);
    		
        	animateSet.addAnimation(rotateAnim);
    		animateSet.addAnimation(transAnim);
    		
    		childView.startAnimation(animateSet);
    		final int pos = i + 1;
    		childView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mOnMenuItemClickListener != null) {
						mOnMenuItemClickListener.onClick(childView, pos);
					}
					menuItemAnim(pos-1);
					changeStatus();
				}
			});
		}
		//切换菜单状态
		changeStatus();
	}
	
	/**
	 * menu item的点击动画
	 * @param pos
	 */
	private void menuItemAnim(int pos) {
		for(int i=0; i<getChildCount() - 1; i++) {
			View childView = getChildAt(i+1);
			if(i == pos) {
				childView.startAnimation(scaleBigAnim(300));
			} else {
				childView.startAnimation(scaleSmallAnim(300));
			}
			childView.setClickable(false);
			childView.setFocusable(false);
		}
	}

	/**
	 * 为其它未点击的item设置缩小和透明度降低动画
	 * @param duration
	 * @return
	 */
	private Animation scaleSmallAnim(int duration) {
		AnimationSet animateSet = new AnimationSet(true);
		
		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0);
		
		animateSet.addAnimation(scaleAnim);
		animateSet.addAnimation(alphaAnim);
		
		animateSet.setDuration(duration);
		animateSet.setFillAfter(true);
		
		return animateSet;
	}

	/**
	 * 为当前点击的item设置变大和透明度降低的动画
	 * @param duration
	 * @return
	 */
	private Animation scaleBigAnim(int duration) {
		AnimationSet animateSet = new AnimationSet(true);
		
		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0);
		
		animateSet.addAnimation(scaleAnim);
		animateSet.addAnimation(alphaAnim);
		
		animateSet.setDuration(duration);
		animateSet.setFillAfter(true);
		
		return animateSet;
	}

	private void changeStatus() {
		mStatus = mStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE; 
	}

	/**
	 * 主按钮的旋转动画
	 * @param v
	 * @param start
	 * @param end
	 * @param duration
	 */
	private void rotateMainButton(View v, float start, float end, int duration) {
		RotateAnimation ra = new RotateAnimation(start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(duration);
		ra.setFillAfter(true);
		v.startAnimation(ra);
	}
}
