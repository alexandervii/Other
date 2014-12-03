package com.alex.gif;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	private GifImageView mImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImage = (GifImageView) findViewById(R.id.gif);
		try {
			GifDrawable drawable = new GifDrawable(getAssets().open("Animated-Flag-Delaware.gif"));
			mImage.setImageDrawable(drawable);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
