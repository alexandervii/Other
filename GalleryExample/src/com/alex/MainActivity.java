package com.alex;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Gallery mGallery;
	private TextView mText;
	private Context mContext;
	private Integer[] imgs = {
		R.drawable.icon1,
		R.drawable.icon2,
		R.drawable.icon3,
		R.drawable.icon4,
		R.drawable.icon5,
		R.drawable.icon6,
		R.drawable.icon7,
		R.drawable.icon8,
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mContext = getApplicationContext();
		initView();
	}

	private void initView() {
		mGallery = (Gallery) findViewById(R.id.gallery);
		mText = (TextView) findViewById(R.id.text);
		mGallery.setAdapter(new GalleryAdapter());
		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mText.setText("定位 " + (position+1) + "/" + imgs.length);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		
		});
	}
	
	private class GalleryAdapter extends BaseAdapter {

		private ImageView mIV = new ImageView(mContext);
		
		@Override
		public int getCount() {
			return imgs.length;
		}

		@Override
		public Object getItem(int position) {
			return imgs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup parent) {
			ImageView image = new ImageView(mContext);
			image.setImageResource(imgs[position]);
			image.setAdjustViewBounds(true);  
	        image.setLayoutParams(new Gallery.LayoutParams(  
	            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));  
			return image;
		}
	}
}
