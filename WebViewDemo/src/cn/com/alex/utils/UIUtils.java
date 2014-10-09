package cn.com.alex.utils;

import cn.com.alex.manager.WebApplication;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class UIUtils {
	
	
	public static Context getContext() {
		return WebApplication.getContext();
	}
	

	/**
	 * 根据context和Class跳转
	 * @param context
	 * @param clazz
	 */
	public static void jump(Context context, Class clazz) {
		Intent intent = new Intent(context,clazz);
		context.startActivity(intent);
	}
	
	/**
	 * 根据传递的Intent跳转
	 * @param Intent
	 */
	public static void jump(Intent intent) {
		getContext().startActivity(intent);
	}
	
	/**
	 * 获取屏幕宽度
	 * @return
	 */
	public static int getScreenWidth(WindowManager wm) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}
	
	/**
	 * 获取屏幕高度
	 * @return
	 */
	public static int getScreenHeight(WindowManager wm) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.heightPixels;
	}
}
