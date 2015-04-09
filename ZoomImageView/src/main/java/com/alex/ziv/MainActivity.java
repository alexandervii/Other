package com.alex.ziv;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alex.view.ZoomImageView;


public class MainActivity extends Activity {

    private ViewPager mViewPager;

    private int[] res = new int[] {
            R.mipmap.a,
            R.mipmap.b,
            R.mipmap.c,
            R.mipmap.d,
            R.mipmap.e,
            R.mipmap.f,
            R.mipmap.g
    };

    private ImageView[] mImgs = new ImageView[res.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return res.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ZoomImageView img = new ZoomImageView(getApplicationContext());
                img.setImageResource(res[position]);
                container.addView(img);
                mImgs[position] = img;
                return img;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImgs[position]);
            }
        });
    }


}
