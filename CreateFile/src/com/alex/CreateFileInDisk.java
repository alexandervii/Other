package com.alex;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class CreateFileInDisk {

	private static File mLogFile;
	private static String mLogTime;
	
	public static void main(String[] args) {
		createFile();
	}
	
	private static String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date(System.currentTimeMillis());
		String time = sdf.format(date);
		return time;
	}
	
	private static void createFile() {
		mLogTime = getTime();
		String dir = "F:/a/b";
		mLogFile = new File(dir,mLogTime+".txt");
		if(!mLogFile.exists()) {
			try {
				mLogFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
