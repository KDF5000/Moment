package com.ktl.moment.utils;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {

	private SharedPreferences sharedPreferences;
	private static SharedPreferencesUtil sharedPreferencesUtil;

	/**
	 * 启动应用时初始化，以保证后续操作
	 * @param context
	 */
	public static synchronized void initSharedPreferences(Context context) {
		if(sharedPreferencesUtil == null){
			sharedPreferencesUtil = new SharedPreferencesUtil(context);
		}
	}
	
	public static synchronized SharedPreferencesUtil getInstance(){
		return sharedPreferencesUtil;
	}

	private SharedPreferencesUtil(Context context) {
		sharedPreferences = context.getSharedPreferences("moment_shared_pre",
				Context.MODE_PRIVATE | Context.MODE_APPEND);
	}
	
	/**
	 * 把对象数据写入文件
	 * @param obj
	 * @param key
	 */
	public synchronized void putObject(String key, Object obj){
		Editor editor = sharedPreferences.edit();
		String str = "";
		if(obj == null){
			obj = new Object();
		}
		try {
			str = SerializableUtil.obj2Str(obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.putString(key, str);
		editor.commit();
	}
	
	/**
	 * 把list数据写入文件
	 * @param obj
	 * @param key
	 */
	public synchronized<E> void putList(String key, List<E> list){
		Editor editor = sharedPreferences.edit();
		String str = "";
		if(list == null){
			list = new ArrayList<E>();
		}
		try {
			str = SerializableUtil.list2String(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.putString(key, str);
		editor.commit();
	}
	
	/**
	 * 写入字符数据
	 * @param string
	 * @param key
	 */
	public synchronized void putString(String key, String string){
		Editor editor = sharedPreferences.edit();
		editor.putString(key, string);
		editor.commit();
	}
	
	/**
	 * 读取object数据
	 * @param key
	 * @return
	 */
	public synchronized Object getObject(String key){
		Object obj = new Object();
		String str = sharedPreferences.getString(key, "");
		if(str==""){
			return null;
		}
		try {
			obj = SerializableUtil.str2Obj(str);
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 读取list数据
	 * @param <E>
	 * @param key
	 * @return
	 */
	public synchronized <E> List<E> getList(String key){
		List<E> list = new ArrayList<E>();
		String str = sharedPreferences.getString(key, "");
		try {
			list = SerializableUtil.string2List(str);
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 读取string数据
	 * @param key
	 * @return
	 */
	public synchronized String getString(String key){
		String str = "";
		str = sharedPreferences.getString(key, "");
		return str;
	}
	
	/**
	 * 删除数据
	 * @param key
	 */
	public synchronized void delete(String key){
		Editor editor = sharedPreferences.edit();
		editor.putString(key, "");
		editor.commit();
	}
	/**
	 * 设置boolean的变量
	 * @param key
	 * @param value
	 */
	public synchronized void setBoolean(String key,boolean value){
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	 /**
	  * 获得boolean类型的变量
	  * @param key
	  * @param defaultValue
	  * @return
	  */
	public synchronized boolean getBoolean(String key,boolean defValue){
		return sharedPreferences.getBoolean(key, defValue);
	}
	/**
	 * 保存int类型
	 * @param key
	 * @param value
	 */
	public synchronized void setInt(String key,int value){
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	 /**
	  * 获得int类型的变量
	  * @param key
	  * @param defaultValue
	  * @return
	  */
	public synchronized int getInt(String key,int defValue){
		return sharedPreferences.getInt(key, defValue);
	}
	
	
}
