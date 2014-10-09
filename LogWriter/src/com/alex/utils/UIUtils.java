package com.alex.utils;

import android.content.Context;
import android.widget.Toast;

public class UIUtils {
	
	public static void showToast(Context context, String text, int duration) {
		Toast.makeText(context, text, duration).show();
	}

}
