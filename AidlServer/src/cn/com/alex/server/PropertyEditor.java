package cn.com.alex.server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import android.content.Context;
import android.widget.Toast;

import cn.com.alex.server.domain.Person;

/**
 * 向本地缓存文件写入或者读取的类 单例
 * 
 * @author pengbosj
 * 
 */
public class PropertyEditor {

	/** Context */
	private static Context mContext;
	/** 向文件输出键值对 */
	private static Properties mOutProperties;
	/** 文件输出流 */
	private Writer mWriter;
	/** 文件路径*/
	private String mFilePath;

	/**
	 * 获取所有缓存信息
	 * 
	 * @return
	 */
	private Map<Object, Object> getAllInfos() {
		InputStream inputStream = PropertyEditor.class.getClassLoader()
				.getResourceAsStream("draft");
		Properties prop = new Properties();
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		Set<Entry<Object, Object>> entrySet = prop.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}
	
	/**
	 * 直接从文件获取值
	 * @param key
	 * @return
	 */
	public Object getValue(String key) {
		return getAllInfos().get(key);
	}

	/**
	 * 私有构造
	 * 
	 * @param context
	 */
	private PropertyEditor(Context context) {
		mContext = context;
		init();
	}

	/**
	 * 初始化操作
	 */
	private void init() {
		mOutProperties  = new Properties();
		mFilePath = PropertyEditor.class.getClassLoader().getResource(
				"draft").getPath();
	}

	private static PropertyEditor mEditor;

	/**
	 * 获取实例
	 * 
	 * @param context
	 * @return
	 */
	public static PropertyEditor getInstance(Context context) {
		synchronized (mEditor) {
			if (mEditor == null) {
				mEditor = new PropertyEditor(context);
			}
		}
		return mEditor;
	}

	/**
	 * 将键值对写入文件中
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue(String value) {
		saveToLocal(value);
	}

	/**
	 * 将person信息写入到文件中，每项信息以#分割 person的写入格式：key = value name = name#age#gender
	 * 
	 * @param info
	 */
	public void setPersonInfo(Person info) {
		saveToLocal(info.toString());
	}

	/**
	 * 添加唯一的key
	 */
	public String getUniqueKey() {
		int max = 0;
		for (Object obj : getAllInfos().keySet()) {
			if (obj instanceof String) {
				int temp = Integer.valueOf((String) obj);
				if(temp > max) {
					max = temp;
				}
			}
		}
		int big = max + 1;
		return big+"";
	}
	
	/**
	 * 判断该值是否已经存在
	 * @param value
	 * @return
	 */
	private boolean isExist(String value) {
		return getAllInfos().containsValue(value);
	}

	/**
	 * 将信息以键值对方式存入文件中
	 * 
	 * @param key
	 * @param value
	 */
	private synchronized void saveToLocal(String value) {
		if(isExist(value)) {
			Toast.makeText(mContext, "该值已经存在", Toast.LENGTH_SHORT).show();
			return ;
		}
		// 向文件中写
		try {
			mOutProperties.clear();
			mOutProperties.put(getUniqueKey(), value);
			mWriter = new OutputStreamWriter(new FileOutputStream(mFilePath, true));
			mOutProperties.store(mWriter, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (mWriter != null) {
				try {
					mWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
