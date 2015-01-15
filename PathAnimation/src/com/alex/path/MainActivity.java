package com.alex.path;

import java.util.ArrayList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private int[] resIds = { R.id.iv_a, R.id.iv_b, R.id.iv_c, R.id.iv_d,
			R.id.iv_e, R.id.iv_f, R.id.iv_g, R.id.iv_h, };
	
	private List<ImageView> mImgList = new ArrayList<ImageView>();
	
	private boolean flag = true;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}


	private void initView() {
		for(int i=0; i<resIds.length; i++) {
			ImageView img = (ImageView) findViewById(resIds[i]);
			img.setOnClickListener(this);
			mImgList.add(img);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_a:
			if(flag) {
				startAnimation();
			} else {
				closeAnimation();
			}
			break;
		default:
			Toast.makeText(MainActivity.this, "v:"+view.getId(), 1).show();
			break;
		}
	}

	@SuppressLint("NewApi")
	private void closeAnimation() {
		for (int i = 0; i < resIds.length; i++) {
			ObjectAnimator animator = ObjectAnimator.ofFloat(mImgList.get(i), "translationY", i*100F,0f);
			animator.setDuration(500);
			//回弹的加速器
			animator.setInterpolator(new BounceInterpolator());
			animator.setStartDelay(i*300);
			animator.start();
		}
		flag = true;
	}

	@SuppressLint("NewApi")
	private void startAnimation() {
		for (int i = 0; i < resIds.length; i++) {
			ObjectAnimator animator = ObjectAnimator.ofFloat(mImgList.get(i), "translationY", 0f,i*100F);
			animator.setDuration(500);
			animator.setInterpolator(new BounceInterpolator());
			animator.setStartDelay(i*300);
			animator.start();
		}
		flag = false;
	}
}
