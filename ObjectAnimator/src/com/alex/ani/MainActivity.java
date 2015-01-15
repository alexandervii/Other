package com.alex.ani;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	private ImageView mImage;
	private Button mBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImage = (ImageView) findViewById(R.id.iv);
		mBtn = (Button) findViewById(R.id.btn);
	}
	
	public void click(View view) {
		Toast.makeText(this, "hello", 0).show();
	}
	
	public void click1(View view) {
		ObjectAnimator a = ObjectAnimator.ofFloat(view, "alpha", 0f,1f);
		a.setDuration(1000);
		a.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				Toast.makeText(MainActivity.this, "animation", 0).show();
			}
			
		});
//		a.addListener(new AnimatorListener() {
//			@Override
//			public void onAnimationStart(Animator animation) {
//				
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animator animation) {
//				
//			}
//			
//			@Override
//			public void onAnimationEnd(Animator animation) {
//				Toast.makeText(MainActivity.this, "animation", 0).show();
//			}
//			
//			@Override
//			public void onAnimationCancel(Animator animation) {
//				
//			}
//		});
		a.start();
	}

	public void move(View view) {
		//普通动画，图片的点击动画会在原始位置触发，而不是跟着图片的位置发生改变
//		TranslateAnimation ta = new TranslateAnimation(0, 200, 0, 0);
//		ta.setDuration(1000);
//		ta.setFillAfter(true);
//		mImage.startAnimation(ta);
		
		//三个动画并不是从上往下执行的，而是同时开始的，组成了一个组合动画
//		ObjectAnimator.ofFloat(mImage,"translationX", 0,200F).setDuration(1000).start();
//		ObjectAnimator.ofFloat(mImage, "translationY", 0,200F).setDuration(1000).start();
//		ObjectAnimator.ofFloat(mImage, "Rotation", 0,360F).setDuration(1000).start();
		
		//这个是上面组合动画的升级版，效率会提高，资源消耗会减少
//		PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationX", 0,200F);
//		PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationY", 0,200F);
//		PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("rotation", 0,360F);
//		ObjectAnimator.ofPropertyValuesHolder(mImage,p1,p2,p3).setDuration(1000).start();
		
		//AnimatorSet
		ObjectAnimator o1 = ObjectAnimator.ofFloat(mImage,"translationX", 0,200F);
		ObjectAnimator o2 = ObjectAnimator.ofFloat(mImage,"translationY", 0,200F);
		ObjectAnimator o3 = ObjectAnimator.ofFloat(mImage,"rotation", 0,360F);
		
		AnimatorSet as = new AnimatorSet();
		//一起执行动画
		as.playTogether(o1,o2,o3);
//		分别执行动画
		as.playSequentially(o1,o2,o3);
//		o2,o3动画同时执行，最后o1动画在o2动画之后执行
		as.play(o2).with(o3);
		as.play(o1).after(o2);
		as.setDuration(1000);
		as.start();
	}

}
