package com.alex.volley;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageCache;

public class MainActivity extends Activity {
	
	private String mUrl = "http://221.176.31.206/mbeside/person/personinfo?userid=2004842856&ckey=uidfidlogicpool=2004842856=1761481315=30500&clienttype=16&version=3.4.0";
	private String mImageUrl = "http://imgh.fetionpic.com/group1/M00/78/50/wKjwKFRghN2BMcAFAAiUF6k6Q5w199.png";

	private ImageView mImageView;
	private NetworkImageView mNetworkImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init(); 
    } 
       
    private void init(){ 
        mImageView=(ImageView) findViewById(R.id.imageView); 
        mNetworkImageView=(NetworkImageView)findViewById(R.id.networkImageView); 
        getJSONByVolley(); 
        loadImageByVolley(); 
        showImageByNetworkImageView(); 
    } 
   
    /**
     * 利用Volley获取JSON数据
     */ 
    private void getJSONByVolley() { 
        RequestQueue requestQueue = Volley.newRequestQueue(this); 
        final ProgressDialog progressDialog = ProgressDialog.show(this, "This is title", "...Loading..."); 
   
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( 
                Request.Method.GET,  
                mUrl,  
                null, 
                new Response.Listener<JSONObject>() { 
                    @Override 
                    public void onResponse(JSONObject response) { 
                        System.out.println("response="+response); 
                        if (progressDialog.isShowing()&&progressDialog!=null) { 
                            progressDialog.dismiss(); 
                        } 
                    } 
                },  
                new Response.ErrorListener() { 
                    @Override 
                    public void onErrorResponse(VolleyError arg0) { 
                           System.out.println("sorry,Error"); 
                    } 
                }); 
        requestQueue.add(jsonObjectRequest); 
    } 
       
       
    /**
     * 利用Volley异步加载图片
     * 
     * 注意方法参数:
     * getImageListener(ImageView view, int defaultImageResId, int errorImageResId)
     * 第一个参数:显示图片的ImageView
     * 第二个参数:默认显示的图片资源
     * 第三个参数:加载错误时显示的图片资源
     */ 
    private void loadImageByVolley(){ 
        RequestQueue requestQueue = Volley.newRequestQueue(this); 
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20); 
        ImageCache imageCache = new ImageCache() { 
            @Override 
            public void putBitmap(String key, Bitmap value) { 
                lruCache.put(key, value); 
            } 
   
            @Override 
            public Bitmap getBitmap(String key) { 
                return lruCache.get(key); 
            } 
        }; 
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache); 
        ImageListener listener = ImageLoader.getImageListener(mImageView, R.drawable.ic_launcher,R.drawable.ic_launcher); 
        imageLoader.get(mImageUrl, listener); 
    } 
       
    /**
     * 利用NetworkImageView显示网络图片
     */ 
    private void showImageByNetworkImageView(){ 
        RequestQueue requestQueue = Volley.newRequestQueue(this); 
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20); 
        ImageCache imageCache = new ImageCache() { 
            @Override 
            public void putBitmap(String key, Bitmap value) { 
                lruCache.put(key, value); 
            } 
   
            @Override 
            public Bitmap getBitmap(String key) { 
                return lruCache.get(key); 
            } 
        }; 
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache); 
        mNetworkImageView.setTag("url"); 
        mNetworkImageView.setImageUrl(mImageUrl,imageLoader); 
    } 
   
}
