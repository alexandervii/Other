package com.alex.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.alex.utils.UIUtils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LogService extends Service {

	private static final String TAG = "LogWriter";
	private static final String FILETAG = "AllFiles";
	/*文件分隔符*/
	private static final String SEPARATOR = File.separator;
	private static final String FILESIZE = "FileSize";
	
	private Context mContext;
	/*一个日志文件最大容量为5M*/
	private static final long MAX_LOG_SIZE = 5*1024*1024;
	/*日志文件目录下所有文件的容量总和最大值为100M*/
	private static final long MAX_LOG_SUM_SIZE = MAX_LOG_SIZE * 2;
	
	/*log标志：true表示输出log，false表示停止输出log*/
	private boolean mFlag = true;
	/*日志文件*/
	private File mLogFile = null;
	/*log产生的时间，用来给log命名*/
	private String mLogName = null;
	/*log的目录*/
	private String mLogDir = null;
	/*log目录的父目录，这里是getPackageName()*/
	private String mAllLogDir = null;
	/*所有日志文件*/
	private List<File> mFiles;
	
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;

	/**
	 * 用来判断是产生日志目录还是日志文件
	 */
	enum LOG_TYPE{
		LOG_DIR,//如果是产生文件分类目录
		LOG_FILE;//如果是产生文件
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		init();
		createLogFile();
		new Thread() {
			public void run() {
				while (mFlag) {
					writeToLocal(mLogName);
				}
			}
		}.start();
	}

	private void init() {
		mFiles = new ArrayList<File>();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mFlag = false;
	}

	private void createLogFile() {
		//创建当天log存放的目录
		String mLogDir = getLogInfo(LOG_TYPE.LOG_DIR);
		//获取log的名称
		mLogName = getLogInfo(LOG_TYPE.LOG_FILE);
		String packageName = getPackageName();

		mAllLogDir =  Environment
				.getExternalStorageDirectory() + SEPARATOR + "ALog" + SEPARATOR + packageName;
		//log文件在sdcard的目录
		File dir = new File(mAllLogDir + SEPARATOR + mLogDir);

		Log.i(TAG, dir.getPath());
		//log全名
		String fileName = mLogName + ".txt";
		Log.i(TAG, fileName);

		if (!dir.exists()) {
			//创建目录
			boolean mkdirs = dir.mkdirs();
			Log.i(TAG, "mkdirs:" + mkdirs);
			if(mkdirs) {
				UIUtils.showToast(getApplicationContext(), "日志目录创建成功!", Toast.LENGTH_LONG);
			} else {
				UIUtils.showToast(getApplicationContext(), "日志目录创建失败!", Toast.LENGTH_LONG);
			}
		}
		mLogFile = new File(dir, fileName);
		if (!mLogFile.exists()) {
			try {
				//创建日志文件
				boolean newFile = mLogFile.createNewFile();
				Log.i(TAG, "createNewFile:" + newFile);
				if(newFile) {
					UIUtils.showToast(getApplicationContext(), "日志文件创建成功!", Toast.LENGTH_LONG);
				} else {
					UIUtils.showToast(getApplicationContext(), "日志文件创建失败!", Toast.LENGTH_LONG);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getLogInfo(LOG_TYPE type) {
		String format = "";
		switch (type) {
		case LOG_DIR:
			//如果是文件目录，则是要年月日
			format = "yyyyMMdd";
			break;
		case LOG_FILE:
			//如果是文件，则要年月日时分秒
			format = "yyyyMMddHHmmss";
			break;
		default:
			break;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date(System.currentTimeMillis());
		String time = sdf.format(date);
		return time;
	}

	/**
	 * 将文件写入到sdcard中
	 * @param time
	 */
	protected void writeToLocal(String time) {
		if(!mLogFile.exists()) {
			Log.i(TAG, "日志文件不存在");
			return ;
		}
		try {
			Process process = Runtime.getRuntime().exec("logcat -d");
			bufferedReader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(mLogFile,true)));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				//判断文件大小
				checkFileSize();
				bufferedWriter.write(line);
				bufferedWriter.flush();
				bufferedWriter.newLine();
			}
		} catch (Exception e) {

		} finally {

			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
					bufferedWriter = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (bufferedReader != null) {
				try {
					bufferedReader.close();
					bufferedReader = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 检查日志文件大小，如果大于5MB，则另起一个文件
	 * 
	 */
	private void checkFileSize() {
		if(mLogFile.length() > MAX_LOG_SIZE) {
			createLogFile();
		}
		
		File allFile = new File(mAllLogDir);
//		Log.i(FILESIZE, allFile.length()+"");
		
		getAllFiles(allFile);
		Collections.sort(mFiles, new FileComparator());
		for(File f : mFiles) {
			System.out.println(f.getName());
		}
	}
	
	private class FileComparator implements Comparator<File> {
		@Override
		public int compare(File f1, File f2) {
			long n1 = Long.valueOf(f1.getName());
			long n2 = Long.valueOf(f2.getName());
			if(n1 > n2) {
				return 1;
			} else if(n1 < n2) {
				return -1;
			}
			return 0;
		}
	}
	
	/**
	 * 获取一个目录下的所有文件
	 * @param file 目录
	 */
	private void getAllFiles(File file) {
		File[] files = file.listFiles();
		for (File file2 : files) {
			if(file2.length() == 0) {
				file2.delete();
				files = file.listFiles();
			}
			if(file2.isDirectory()) {
				getAllFiles(file2);
			} else {
				mFiles.add(file2);	
			}
		} 
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
}
