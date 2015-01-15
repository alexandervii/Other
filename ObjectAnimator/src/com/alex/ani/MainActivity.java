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
		//��ͨ������ͼƬ�ĵ����������ԭʼλ�ô����������Ǹ���ͼƬ��λ�÷����ı�
//		TranslateAnimation ta = new TranslateAnimation(0, 200, 0, 0);
//		ta.setDuration(1000);
//		ta.setFillAfter(true);
//		mImage.startAnimation(ta);
		
		//�������������Ǵ�������ִ�еģ�����ͬʱ��ʼ�ģ������һ����϶���
//		ObjectAnimator.ofFloat(mImage,"translationX", 0,200F).setDuration(1000).start();
//		ObjectAnimator.ofFloat(mImage, "translationY", 0,200F).setDuration(1000).start();
//		ObjectAnimator.ofFloat(mImage, "Rotation", 0,360F).setDuration(1000).start();
		
		//�����������϶����������棬Ч�ʻ���ߣ���Դ���Ļ����
//		PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("translationX", 0,200F);
//		PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("translationY", 0,200F);
//		PropertyValuesHolder p3 = PropertyValuesHolder.ofFloat("rotation", 0,360F);
//		ObjectAnimator.ofPropertyValuesHolder(mImage,p1,p2,p3).setDuration(1000).start();
		
		//AnimatorSet
		ObjectAnimator o1 = ObjectAnimator.ofFloat(mImage,"translationX", 0,200F);
		ObjectAnimator o2 = ObjectAnimator.ofFloat(mImage,"translationY", 0,200F);
		ObjectAnimator o3 = ObjectAnimator.ofFloat(mImage,"rotation", 0,360F);
		
		AnimatorSet as = new AnimatorSet();
		//һ��ִ�ж���
		as.playTogether(o1,o2,o3);
//		�ֱ�ִ�ж���
		as.playSequentially(o1,o2,o3);
//		o2,o3����ͬʱִ�У����o1������o2����֮��ִ��
		as.play(o2).with(o3);
		as.play(o1).after(o2);
		as.setDuration(1000);
		as.start();
	}

}
