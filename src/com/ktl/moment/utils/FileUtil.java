package com.ktl.moment.utils;

import java.io.File;

import android.os.Environment;
import android.util.Log;

/**
 * 文件处理工具类
 * @author HUST_LH
 *
 */

public class FileUtil {
	
	public FileUtil(){
		
	}
	
	/**
	 * 判断磁盘是否存在
	 * @return
	 */
	private static boolean isStorage(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取磁盘根路径
	 * @return
	 */
	private static String getRootDir(){
		String rootDir = "";
		if(isStorage()){
			rootDir = Environment.getExternalStorageDirectory().getPath();
		}
		return rootDir;
	}
	
	/**
	 * 创建文件夹
	 * @param path:文件夹在磁盘上的相对路径。。。eg:/storage/emulated/0/moment/,只需要输入meoment/
	 * @return
	 */
	public static boolean createDir(String path){
		boolean isSuccess = false;
		String dirPath = getRootDir() + "/" + path;
		Log.i("tag", dirPath);
		File createPath = new File(dirPath);
		if (!createPath.exists()) {
			isSuccess = createPath.mkdirs();
		}
		return isSuccess;
	}
	
	/**
	 * 获取文件夹file
	 * @param path：文件夹在磁盘上的相对路径
	 * @return
	 */
	public static File getDir(String path){
		String wholePath = getRootDir() + "/" + path;
		File dirFile = new File(wholePath);
		return dirFile;
	}
}
