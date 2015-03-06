package com.alex.tur.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;

import com.alex.tur.bean.ChatMessage;
import com.alex.tur.bean.ChatMessage.Type;
import com.alex.tur.bean.Result;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import android.util.Log;

public class HttpUtil {
	
	private static final String TAG = HttpUtil.class.getSimpleName();
	
	private static final String API_KEY = "238e3790e080328b02bdf39abfcbb651";
	private static final String URL = "http://www.tuling123.com/openapi/api";
	
	/**
	 * 发送一个消息
	 * @param msg
	 * @return
	 */
	public static ChatMessage sendMessage(String msg) {
		ChatMessage cm = new ChatMessage();
		String result = doGet(msg);
		Gson gson = new Gson();
		Result data = null; 
		try {
			data = gson.fromJson(result, Result.class);
			cm.msg = data.text;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			cm.msg = "服务器繁忙，请稍后再试!";
		}
		cm.date = new Date();
		cm.type = Type.INCOMING;
		return cm;
	}
	
	/**
	 * 访问服务器
	 * @param msg
	 * @return
	 */
	public static String doGet(String msg) {
		String result = "";
		String url = setParams(msg);
		ByteArrayOutputStream bos = null;
		InputStream is = null;
		try {
			java.net.URL get = new java.net.URL(url);
			HttpURLConnection con = (HttpURLConnection) get.openConnection();
			con.setConnectTimeout(5*1000);
			con.setReadTimeout(5*1000);
			con.setRequestMethod("GET");
			
			is = con.getInputStream();
			int len = -1;
			byte[] buff = new byte[128];
			bos = new ByteArrayOutputStream();
			while((len = is.read(buff)) != -1) {
				bos.write(buff, 0, len);
			}
			bos.flush();
			result = new String(bos.toByteArray());
			Log.i(TAG, "result->>"+result);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(bos != null) {
					bos.close();
				}
				if(is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private static String setParams(String msg) {
		String url = "";
		try {
			url = URL + "?key=" + API_KEY + "&info=" + URLEncoder.encode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	} 
}

