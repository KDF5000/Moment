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
	
	private static final String appRoot = "moment";
	
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
	private static String getStorageRootDir(){
		String storageRootDir = "";
		if(isStorage()){
			storageRootDir = Environment.getExternalStorageDirectory().getPath();
		}
		return storageRootDir;
	}
	
	/**
	 * 创建app根目录
	 * @return
	 */
	public static boolean createAppRootDir(){
		boolean isSuccess = false;
		String appRootDir = getStorageRootDir() + "/" + appRoot;
		Log.i("tag", appRootDir);
		File createRootPath = new File(appRootDir);
		if (!createRootPath.exists()) {
			isSuccess = createRootPath.mkdirs();
		}
		return isSuccess;
	}
	
	/**
	 * 获取app根目录
	 * @return
	 */
	public static String getAppRootDir(){
		return getStorageRootDir() + "/" + appRoot;
	}
	
	/**
	 * 在app根目录中创建文件夹
	 * @param path:文件夹在app根目录中的相对路径。。。eg:/storage/emulated/0/moment/record,只需要输入record/
	 * @return
	 */
	public static boolean createDir(String path){
		boolean isSuccess = false;
		String dirPath = getAppRootDir() + "/" + path;
		Log.i("tag", dirPath);
		File createPath = new File(dirPath);
		if (!createPath.exists()) {
			isSuccess = createPath.mkdirs();
		}
		return isSuccess;
	}
	
	/**
	 * 获取一个文件夹的file对象
	 * @param path：文件夹在磁盘上的相对路径。。。eg:/storage/emulated/0/moment/record,只需要输入record/
	 * @return
	 */
	public static File getDir(String path){
		String wholePath = getAppRootDir() + "/" + path;
		File dirFile = new File(wholePath);
		return dirFile;
	}
	
	/**
	 * 删除指定路径的文件
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path){
		boolean isDelete = false;
		File file = new File(path);
		if(file.exists()){
			isDelete = file.delete();
		}
		return isDelete;
	}
}
