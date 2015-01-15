package com.alex.va;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@SuppressLint("NewApi")
	public void click(View view) {
		final Button btn = (Button)view;
		ValueAnimator va = ValueAnimator.ofInt(1,100);
		va.setDuration(5000);
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer value = (Integer) animation.getAnimatedValue();
				btn.setText(value+"");
			}
		});
		va.start();
		
		
//		ValueAnimator va = ValueAnimator.ofObject(new TypeEvaluator<PointF>() {
//			@Override
//			public PointF evaluate(float fraction, PointF startValue,
//					PointF endValue) {
//				return null;
//			}
//			
//		});
	}
}
